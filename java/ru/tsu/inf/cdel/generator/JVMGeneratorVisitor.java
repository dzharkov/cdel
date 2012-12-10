package ru.tsu.inf.cdel.generator;

import java.io.IOException;
import org.apache.bcel.generic.*;
import org.apache.bcel.Constants;

import ru.tsu.inf.cdel.ast.*;
import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;
import ru.tsu.inf.cdel.semantical.DeclarationsVisitor;
import ru.tsu.inf.cdel.semantical.Operator;
import ru.tsu.inf.cdel.semantical.TypeCheckVisitor;
import ru.tsu.inf.cdel.semantical.Variables;
import ru.tsu.inf.cdel.semantical.function.Function;
import ru.tsu.inf.cdel.semantical.function.ReadFunction;
import ru.tsu.inf.cdel.semantical.function.WriteFunction;
import ru.tsu.inf.cdel.semantical.type.PrimitiveType;
import ru.tsu.inf.cdel.semantical.type.PrimitiveTypeManager;

public class JVMGeneratorVisitor extends ASTNodeVisitor {
    private TypeCheckVisitor types;
    private DeclarationsVisitor declars;
    private Variables globals, locals;
    private ClassGen cg;
    private InstructionList il;
    private InstructionFactory insF;
    private String className;
    private ASTNode assignExpression = null;
    
    private Type getJVMTypeByOurType(ru.tsu.inf.cdel.semantical.type.Type type) {
        if (type instanceof PrimitiveType) {
            if (type.equals(PrimitiveTypeManager.getInstance().getTypeByName("double"))) {
                return Type.DOUBLE;
            }
            if (type.equals(PrimitiveTypeManager.getInstance().getTypeByName("string"))) {
                return Type.STRING;
            }
            return Type.INT;
        }
        if (type instanceof ru.tsu.inf.cdel.semantical.type.ArrayType) {
            ru.tsu.inf.cdel.semantical.type.ArrayType arrayType = (ru.tsu.inf.cdel.semantical.type.ArrayType)type;
            return new ArrayType(getJVMTypeByOurType(arrayType.getOfType()), arrayType.getDims().length);
        }
        return new ObjectType("java.lang.Object");
    }
    
    private Type getTypeOfNode(ASTNode node) {
        return getJVMTypeByOurType(types.getType(node));
    }
    
    private void addField(String name, Type jvmType) {
        FieldGen fg = new FieldGen(Constants.ACC_PUBLIC | Constants.ACC_STATIC, jvmType, name, cg.getConstantPool());
        cg.addField(fg.getField());
    }
    
    private void addField(String name, ru.tsu.inf.cdel.semantical.type.Type type) {
        Type jvmType = getJVMTypeByOurType(type);
        
        addField(name, jvmType);
    }
    
    private InstructionHandle appendInstruction(Instruction ins) {
        InstructionHandle result = il.append(ins);
        il.update();
        return result;
    }
    
    private InstructionHandle appendInstruction(CompoundInstruction ins) {
        InstructionHandle result = il.append(ins);
        il.update();
        return result;
    }
    
    public void generate(String sourceFilename, String className, String path, ASTNode root, DeclarationsVisitor declars, TypeCheckVisitor types) throws IOException{
        this.cg = new ClassGen(className, "java.lang.Object",
                                    sourceFilename, Constants.ACC_PUBLIC | Constants.ACC_SUPER,
                                    null);
        
        this.insF = new InstructionFactory(cg);
        
        this.types = types;
        this.declars = declars;
        this.className = className;
        
        for (int i = 0; i < declars.getVars().getAmount(); i++) {
            addField(declars.getVars().getNameByIndex(i), declars.getVars().getTypeByIndex(i));
        }
        
        globals = declars.getVars();
        locals = new Variables();
        
        root.accept(this);
        cg.getJavaClass().dump(path + "/" + className + ".class");
    }
    
    private void appendArrayCreation(ru.tsu.inf.cdel.semantical.type.ArrayType type) {
        for (int i = 0; i < type.getDims().length; i++) {
            appendInstruction(insF.createConstant(type.getDims()[i].getLength()));
            
        }
               
        appendInstruction(new MULTIANEWARRAY(cg.getConstantPool().addArrayClass((ArrayType)getJVMTypeByOurType(type)), (short)type.getDims().length));
    }
 
    @Override
    public void visit(ProgramNode node) {
        il = new InstructionList();
        addField("_input", new ObjectType("java.util.Scanner"));
        
        appendInstruction(insF.createNew("java.util.Scanner"));
        appendInstruction(InstructionConstants.DUP);
        appendInstruction(insF.createFieldAccess("java.lang.System", "in", new ObjectType("java.io.InputStream"),
                                        Constants.GETSTATIC));
        appendInstruction(insF.createInvoke("java.util.Scanner", "<init>", Type.VOID,
                                 new Type[] {new ObjectType("java.io.InputStream")},
                                 Constants.INVOKESPECIAL));
        appendInstruction(insF.createPutStatic(className, "_input", new ObjectType("java.util.Scanner")));
        
        for (int i = 0; i < globals.getAmount(); i++) {
            ru.tsu.inf.cdel.semantical.type.Type type = globals.getTypeByIndex(i);
            if (type instanceof ru.tsu.inf.cdel.semantical.type.ArrayType) {
                ru.tsu.inf.cdel.semantical.type.ArrayType arrayType = (ru.tsu.inf.cdel.semantical.type.ArrayType)type;
                appendArrayCreation(arrayType);
                appendInstruction(insF.createPutStatic(className, globals.getNameByIndex(i), getJVMTypeByOurType(type)));
            }
        }
        
        node.getStatement().accept(this);
        appendInstruction(InstructionConstants.RETURN);
        MethodGen mg = new MethodGen(Constants.ACC_PUBLIC | Constants.ACC_STATIC,
                                     Type.VOID, new Type[] { 
                                       new ArrayType(Type.STRING, 1) 
                                     }, new String[] { "argv" },
                                     "main", this.className, il, cg.getConstantPool());
        mg.setMaxStack();
        cg.addMethod(mg.getMethod());
        
        il.dispose();
        
    }

    @Override
    public void visit(StatementSequenceNode node) {
        for (ASTNode item : node.getChildren()) {
            item.accept(this);
        }
    }

    @Override
    public void visit(LiteralNode node) {
        PUSH ins;
        if (node.getType().equals(LiteralNode.Type.DOUBLE)) {
            double value =  Double.valueOf(node.getTerminal().getLexemeValue());
            ins = new PUSH(cg.getConstantPool(), value);
        } else if (node.getType().equals(LiteralNode.Type.INTEGER)) {
            int value =  Integer.valueOf(node.getTerminal().getLexemeValue());
            ins = new PUSH(cg.getConstantPool(), value);
        }else {
            ins = new PUSH(cg.getConstantPool(), node.getTerminal().getLexemeValue());
        }
        
        appendInstruction(ins);
    }
    
    private void pushExpressions(ASTNode[] expressions) {
        for (ASTNode expr : expressions) {
            expr.accept(this);
        }
    }
    
    private void writeFunctionCall(ASTNode expr) {
        appendInstruction(insF.createFieldAccess("java.lang.System", "out", new ObjectType("java.io.PrintStream"),
                                        Constants.GETSTATIC));
        expr.accept(this);
        appendInstruction(insF.createInvoke("java.io.PrintStream", "println", Type.VOID, 
                             new Type[] { getTypeOfNode(expr) },
                             Constants.INVOKEVIRTUAL));
    }
    
    private void functionCall(String name, ASTNode[] params) {      
        Function func = declars.getFuncMap().get(name);

        if (func instanceof ReadFunction) {
            appendInstruction(insF.createFieldAccess(className, "_input", new ObjectType("java.util.Scanner"),
                                        Constants.GETSTATIC));
            
            String postfix = "";
            
            if (func.getName().equalsIgnoreCase("read_integer")) {
                postfix = "Int";
            }
            
            if (func.getName().equalsIgnoreCase("read_double")) {
                postfix = "Double";
            }
            
            appendInstruction(insF.createInvoke("java.util.Scanner", "next" + postfix, getJVMTypeByOurType(func.getReturnType()), 
                                 new Type[] {  },
                                 Constants.INVOKEVIRTUAL));
            return;
        }
        if (func instanceof WriteFunction) {
            for (ASTNode param : params) {
                writeFunctionCall(param);
            }
            return;
        }
    }

    @Override
    public void visit(FunctionDesignatorNode node) {
        functionCall(node.getIdent().getLexemeValue(), node.getParameterList().getChildren());
    }
    
    @Override
    public void visit(ProcedureStatementNode node) {
        functionCall(node.getIdent().getLexemeValue(), node.getList().getChildren());
    }

    @Override
    public void visit(IdentVariableNode node) {
        String name = node.getIdent().getLexemeValue();
        
        boolean is_local = false;
        
        if (locals.ifExists(name)) {
            is_local = true;
        }
        
        if (assignExpression != null) {
            ASTNode expr = assignExpression;
            assignExpression = null;
            
            expr.accept(this);
            
            if (is_local) {
                
            } else {
                appendInstruction(insF.createPutStatic(className, name, getTypeOfNode(expr)));
            }
        } else {
            if (is_local) {
                
            } else {
                appendInstruction(insF.createGetStatic(className, name, getTypeOfNode(node)));
            }
        }
    }

    @Override
    public void visit(IndexedVariableNode node) {
        ASTNode expr = assignExpression;
        assignExpression = null;
        
        ru.tsu.inf.cdel.semantical.type.Type type = types.getType(node.getVariable());
        
        if (type instanceof ru.tsu.inf.cdel.semantical.type.ArrayType) {
            ru.tsu.inf.cdel.semantical.type.ArrayType arrayType = (ru.tsu.inf.cdel.semantical.type.ArrayType)type;
            
            node.getVariable().accept(this);
            
            ASTNode[] indexes = node.getList().getChildren();
            int i;
            for (i = 0; i < indexes.length-1; i++) {
                indexes[i].accept(this);
                appendInstruction(insF.createConstant(arrayType.getDims()[i].getFrom()));
                appendInstruction(new ISUB());
                appendInstruction(new AALOAD());
            }
            
            indexes[i].accept(this);
            appendInstruction(insF.createConstant(arrayType.getDims()[i].getFrom()));
            appendInstruction(new ISUB());
            
            Instruction lastInstruction = null;
            
            if (expr!=null) {
                expr.accept(this);

                Type exprType = getTypeOfNode(expr);
                               
                if (exprType.equals(Type.INT)) {
                    lastInstruction = new IASTORE();
                } else if (exprType.equals(Type.DOUBLE)) {
                    lastInstruction = new DASTORE();
                } else {
                    lastInstruction = new AASTORE();
                }

            } else {
                Type typeOf = getJVMTypeByOurType(arrayType.getTypeAfterIndexing(indexes.length));
                
                if (typeOf.equals(Type.INT)) {
                    lastInstruction = new IALOAD();
                } else if (typeOf.equals(Type.DOUBLE)) {
                    lastInstruction = new DALOAD();
                } else {
                    lastInstruction = new AALOAD();
                }
                
            }
            
            appendInstruction(lastInstruction);
        }
        
    }
    
    private void addCMPInstruction(BranchInstruction ins, int trueValue) {
        trueValue = 1-trueValue;
        il.append(ins);
        il.append(new ICONST(trueValue));
        final GOTO gt = new GOTO(null);
        il.append(gt);
        il.append(new ICONST(1 - trueValue));
        ins.setTarget(il.getEnd());
        il.addObserver(new InstructionListObserver() {
            @Override
            public void notify(InstructionList il) {
                if (gt.getTarget() == null) {
                    gt.setTarget(il.getEnd());
                }
            }
        });
    }

    @Override
    public void visit(BinaryOperatorNode node) {
        Operator op = Operator.createByString(node.getOp().getLexemeValue(), false);
        
        Type type = getTypeOfNode(node.getA());
        
        node.getA().accept(this);
        node.getB().accept(this);
        
        if (op.getType() == Operator.LESS 
            || op.getType() == Operator.LESSOREQUAL 
            || op.getType() == Operator.EQUALS
            || op.getType() == Operator.MORE
            || op.getType() == Operator.MOREOREQUAL
            || op.getType() == Operator.NOTEQUALS) {
            
            if (type.equals(Type.INT)) {
                BranchInstruction ins = null;
                
                switch (op.getType()) {
                    case Operator.LESS:
                        ins = new IF_ICMPLT(null);
                        break;
                    case Operator.LESSOREQUAL:
                        ins = new IF_ICMPLE(null);
                        break;
                    case Operator.EQUALS:
                        ins = new IF_ICMPEQ(null);
                        break;
                    case Operator.MORE:
                        ins = new IF_ICMPGT(null);
                        break;
                    case Operator.MOREOREQUAL:
                        ins = new IF_ICMPGE(null);
                        break;
                    case Operator.NOTEQUALS:
                        ins = new IF_ICMPNE(null);
                        break;
                }
                
                addCMPInstruction(ins, 1);
            }
            
            if (type.equals(Type.DOUBLE)) {
                appendInstruction(new DCMPL());
                int compareValue;
                int trueValue;
                switch (op.getType()) {
                    case Operator.LESS:
                        compareValue = -1; trueValue = 1;
                        break;
                    case Operator.LESSOREQUAL:
                        compareValue = 1; trueValue = 0;
                        break;
                    case Operator.EQUALS:
                        compareValue = 0; trueValue = 1;
                        break;
                    case Operator.MORE:
                        compareValue = 1; trueValue = 1;
                        break;
                    case Operator.MOREOREQUAL:
                        compareValue = -1; trueValue = 0;
                        break;
                    case Operator.NOTEQUALS:
                        compareValue = 0; trueValue = 0;
                        break;
                    default:
                        compareValue = 0; trueValue = 0;
                        break;
                }
                
                appendInstruction(new ICONST(compareValue));
                
                addCMPInstruction(new IF_ICMPEQ(null), trueValue);
            }
            
            //TODO: strings
            return;
        }
        Instruction opIns = null;
        
        if (type.equals(Type.INT)) {
            switch (op.getType()) {
                case Operator.AND:
                    opIns = new IAND();
                    break;
                case Operator.OR:
                    opIns = new IOR();
                    break;
                case Operator.DIV:
                    opIns = new IDIV();
                    break;
                case Operator.DIVIDE:
                    opIns = new IDIV();
                    break;
                case Operator.MINUS:
                    opIns = new ISUB();
                    break;
                case Operator.MOD:
                    opIns = new IREM();
                    break;
                case Operator.MULTIPLY:
                    opIns = new IMUL();
                    break;
                case Operator.PLUS:
                    opIns = new IADD();
                    break;
                default:
                    opIns = null;
                    break;
            }
        }
        
        if (type.equals(Type.DOUBLE)) {
            switch (op.getType()) {
                case Operator.DIVIDE:
                    opIns = new DDIV();
                    break;
                case Operator.MINUS:
                    opIns = new DSUB();
                    break;
                case Operator.MULTIPLY:
                    opIns = new DMUL();
                    break;
                case Operator.PLUS:
                    opIns = new DADD();
                    break;
                default:
                    opIns = null;
                    break;
            }
        }
        
        appendInstruction(opIns);
    }

    @Override
    public void visit(UnaryOperatorNode node) {
        Type type = getTypeOfNode(node);
        
        node.getB().accept(this);
        
        Operator op = Operator.createByString(node.getOp().getLexemeValue(), true);
        
        Instruction ins = null;
        
        if (op.getType() == Operator.UNARY_MINUS) {
            if (type.equals(Type.DOUBLE)) {
                ins = new DNEG();
            } else {
                ins = new INEG();
            }
        } else if (op.getType() == Operator.NOT) {
            appendInstruction(new ICONST(1));
            ins = new IXOR();
        }
        
        appendInstruction(ins);
    }

    @Override
    public void visit(AssignmentStatementNode node) {
        assignExpression = node.getExpression();
        node.getVariable().accept(this);
    }

    @Override
    public void visit(IfStatementNode node) {
        node.getCondition().accept(this);
        
        appendInstruction(new ICONST(0));
        
        final IF_ICMPEQ ins = new IF_ICMPEQ(null);
        
        il.append(ins);
        
        node.getThenStatement().accept(this);
        
        if (node.getElseStatement() != null) {
            final GOTO beforeElse = new GOTO(null);
            InstructionHandle beforeElseHandle = il.append(beforeElse);
            
            node.getElseStatement().accept(this);
            
            il.addObserver(new InstructionListObserver() {

                @Override
                public void notify(InstructionList il) {
                    if (beforeElse.getTarget() == null) {
                        beforeElse.setTarget(il.getEnd());
                    }
                }
            });
            
            ins.setTarget(beforeElseHandle.getNext());
        } else {
            il.addObserver(new InstructionListObserver() {

                @Override
                public void notify(InstructionList il) {
                    if (ins.getTarget() == null) {
                        ins.setTarget(il.getEnd());
                    }
                }
            });
        }
        
    }
}
