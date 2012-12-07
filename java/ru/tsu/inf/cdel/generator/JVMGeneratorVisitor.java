package ru.tsu.inf.cdel.generator;

import java.io.IOException;
import org.apache.bcel.generic.*;
import org.apache.bcel.Constants;

import ru.tsu.inf.cdel.ast.*;
import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;
import ru.tsu.inf.cdel.semantical.DeclarationsVisitor;
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
    
    private Type getJVMTypeByOurType(ru.tsu.inf.cdel.semantical.type.Type type) {
        if (type instanceof PrimitiveType) {
            if (type.equals(PrimitiveTypeManager.getInstance().getTypeByName("integer"))) {
                return Type.INT;
            }
            if (type.equals(PrimitiveTypeManager.getInstance().getTypeByName("double"))) {
                return Type.DOUBLE;
            }
            if (type.equals(PrimitiveTypeManager.getInstance().getTypeByName("string"))) {
                return Type.STRING;
            }
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
 
    @Override
    public void visit(ProgramNode node) {
        il = new InstructionList();
        addField("_input", new ObjectType("java.util.Scanner"));
        
        il.append(insF.createNew("java.util.Scanner"));
        il.append(InstructionConstants.DUP);
        il.append(insF.createFieldAccess("java.lang.System", "in", new ObjectType("java.io.InputStream"),
                                        Constants.GETSTATIC));
        il.append(insF.createInvoke("java.util.Scanner", "<init>", Type.VOID,
                                 new Type[] {new ObjectType("java.io.InputStream")},
                                 Constants.INVOKESPECIAL));
        il.append(insF.createPutStatic(className, "_input", new ObjectType("java.util.Scanner")));
        node.getStatement().accept(this);
        il.append(InstructionConstants.RETURN);
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
        
        il.append(ins);
    }
    
    private void pushExpressions(ASTNode[] expressions) {
        for (ASTNode expr : expressions) {
            expr.accept(this);
        }
    }
    
    private void writeFunctionCall(ASTNode expr) {
        il.append(insF.createFieldAccess("java.lang.System", "out", new ObjectType("java.io.PrintStream"),
                                        Constants.GETSTATIC));
        expr.accept(this);
        il.append(insF.createInvoke("java.io.PrintStream", "println", Type.VOID, 
                             new Type[] { getTypeOfNode(expr) },
                             Constants.INVOKEVIRTUAL));
    }
    
    private void functionCall(String name, ASTNode[] params) {      
        Function func = declars.getFuncMap().get(name);

        if (func instanceof ReadFunction) {
            il.append(insF.createFieldAccess(className, "_input", new ObjectType("java.util.Scanner"),
                                        Constants.GETSTATIC));
            
            String postfix = "";
            
            if (func.getName().equalsIgnoreCase("read_integer")) {
                postfix = "Int";
            }
            
            if (func.getName().equalsIgnoreCase("read_double")) {
                postfix = "Double";
            }
            
            il.append(insF.createInvoke("java.util.Scanner", "next" + postfix, getJVMTypeByOurType(func.getReturnType()), 
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
    
}
