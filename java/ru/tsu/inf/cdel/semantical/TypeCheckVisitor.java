package ru.tsu.inf.cdel.semantical;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import ru.tsu.inf.cdel.ast.*;
import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;
import ru.tsu.inf.cdel.semantical.function.Function;
import ru.tsu.inf.cdel.semantical.function.SrcFunction;
import ru.tsu.inf.cdel.semantical.type.PrimitiveType;
import ru.tsu.inf.cdel.semantical.type.PrimitiveTypeManager;
import ru.tsu.inf.cdel.semantical.type.Type;
import ru.tsu.inf.cdel.semantical.type.VoidType;

public class TypeCheckVisitor extends ASTNodeVisitor {
    private DeclarationsVisitor declars;
    
    private Variables globalVariables;
    private Variables context;
    private List< SemanticalError > errors;
    private HashMap< ASTNode, Type > nodeType;
    
    private Type lastType;
    
    public TypeCheckVisitor(DeclarationsVisitor declars) {
        this.declars = declars;
        errors = declars.getErrors();
        nodeType = new HashMap<>();
    }
    
    private void lastWasVoid() {
        lastType = new VoidType();
    }
    
    private Type getTypeOfNode(ASTNode node) {
        if (nodeType.containsKey(node)) {
            return nodeType.get(node);
        }
        
        node.accept(this);
        
        nodeType.put(node, lastType);
        
        return lastType;
    }
    
    public Type getType(ASTNode node) {
        if (nodeType.containsKey(node)) {
            return nodeType.get(node);
        }
        
        return null;
    }

    public List<SemanticalError> getErrors() {
        return errors;
    }
    
    private void addCastError(Type from, Type to, ASTNode node) {
        errors.add(new SemanticalError("value of " + from + " type can't be casted to " +to, node));
    }

    @Override
    public void visit(ProgramNode node) {
        globalVariables = declars.getVars();
        context = globalVariables;
        errors = new LinkedList<>();
        
        node.getStatement().accept(this);
        
        for (ASTNode funcs : node.getProcAndFunc().getChildren()) {
            funcs.accept(this);
        }
        
        lastWasVoid();
    }
    
    

    @Override
    public void visit(FunctionDeclarationNode node) {
        this.visit((ProcedureDeclarationNode)node);
    }

    @Override
    public void visit(ProcedureDeclarationNode node) {
        String name = node.getIdent().getLexemeValue().toLowerCase();
        
        if (!declars.getFuncMap().containsKey(name)) {
            return;
        }
        
        Function func = declars.getFuncMap().get(name);
        
        if (func instanceof SrcFunction) {
            context = globalVariables.merge(((SrcFunction)func).getAllVariables());
            node.getBlock().getStatement().accept(this);
        }
    }

    @Override
    public void visit(AssignmentStatementNode node) {
        Type right = getTypeOfNode(node.getExpression());
        Type left = getTypeOfNode(node.getVariable());
        if (!right.canBeCastedTo(left)) {
            addCastError(right, left, node.getAssOp());
        }
        
        lastWasVoid();
    }

    @Override
    public void visit(BinaryOperatorNode node) {
        Type aType = getTypeOfNode(node.getA());
        Type bType = getTypeOfNode(node.getB());
        Operator op = Operator.createByString(node.getOp().getLexemeValue());
        
        if (!aType.canBeBinaryOperatorApplied(op, bType)) {
            errors.add(new SemanticalError("there is now binary operation '" + node.getOp().getLexemeValue() + "' defined for " + aType + " and " + bType, node.getOp()));
            lastWasVoid();
            return;
        }
        
        lastType = aType.getTypeAfterBinaryOperatopApplied(op, bType);
    }

    @Override
    public void visit(UnaryOperatorNode node) {
        Type aType = getTypeOfNode(node.getB());
        Operator op = Operator.createByString(node.getOp().getLexemeValue());
        
        if (!aType.canBeUnaryOperatorApplied(op)) {
            errors.add(new SemanticalError("there is now unary operation '" + node.getOp().toString() + "' defined for " + aType, node.getOp()));
            lastWasVoid();
            return;
        }
        
        lastType = aType.getTypeAfterUnaryOperatopApplied(op);
    }
    
    private void askParts(ASTNode[] parts) {
        for (ASTNode part : parts) {
            if (part != null) {
                part.accept(this);
            }
        }
    }
    
    private void checkConditionalStatement(ASTNode condition, ASTNode at) {
        if (!getTypeOfNode(condition).canBeCastedTo(PrimitiveTypeManager.getInstance().getTypeById(PrimitiveType.BOOL))) {
            errors.add(new SemanticalError("expression should be an conditon", at));
        }
    }

    @Override
    public void visit(ForStatementNode node) {
        askParts(new ASTNode[] { node.getInitialStatement(), node.getLoopStatement(), node.getPostStatement()});
        
        checkConditionalStatement(node.getCondition(), node.getForWord());
        
        lastWasVoid();
    }

    @Override
    public void visit(StatementSequenceNode node) {
        for (ASTNode statement : node.getChildren()) {
            statement.accept(this);
        }
    }
    
    
    private Type[] getParamsTypesByListNode(ListNode list) {
        ASTNode[] argNodes = list.getChildren();
        Type[] argsType = new Type[argNodes.length];
        
        for (int i = 0; i < argNodes.length; i++) {
            argsType[i] = getTypeOfNode(argNodes[i]);
        }
        return argsType;
    }
    
    private void checkProcedureCall(TerminalNode ident, ListNode list) {
        String name = ident.getLexemeValue().toLowerCase();
        lastWasVoid();
        if (!declars.getFuncMap().containsKey(name)) {
            errors.add(new SemanticalError("invocation to undeclared function '" + name + "' ",ident));
            return;
        }
        
        Function func = declars.getFuncMap().get(name);
        
        
        
        List< String > callErrors = new LinkedList<>();
        
        if (!func.checkApplicability(getParamsTypesByListNode(list), callErrors)) {
            for (String message : callErrors) {
                errors.add(new SemanticalError(message, ident));
            }
        }
        
        lastType = func.getReturnType();
    }
    
    @Override
    public void visit(ProcedureStatementNode node) {
        checkProcedureCall(node.getIdent(), node.getList());
    }

    @Override
    public void visit(FunctionDesignatorNode node) {
        checkProcedureCall(node.getIdent(), node.getParameterList());
    }

    @Override
    public void visit(IfStatementNode node) {
        askParts(new ASTNode[] {node.getThenStatement(), node.getElseStatement()});
        
        checkConditionalStatement(node.getCondition(), node.getIfWord());
        
        lastWasVoid();
    }

    @Override
    public void visit(IndexedVariableNode node) {
        Type varType = getTypeOfNode(node.getVariable());

        List< String > indexingErrors = new LinkedList<>();
        Type newType = varType.getTypeAfterIndexing(getParamsTypesByListNode(node.getList()), indexingErrors);
        
        if (newType == null) {
            if (indexingErrors.isEmpty()) {
                errors.add(new SemanticalError("variable can't be indexed", node.getLeftBracket()));
            }
            for (String message : indexingErrors) {
                errors.add(new SemanticalError(message, node.getLeftBracket()));
            }
            lastWasVoid();
            return;
        }
        
        lastType = newType;
    }
    
    @Override
    public void visit(IdentVariableNode node) {
        String name = node.getIdent().getLexemeValue().toLowerCase();
        
        if (!context.ifExists(name)) {
            errors.add(new SemanticalError("referencing to undefined variable '" + name + "'", node.getIdent()));
            return;
        }
        
        lastType = context.getTypeByName(name);
    }

    @Override
    public void visit(LiteralNode node) {
        int typeId = PrimitiveType.STRING;
        if (node.getType().equals(LiteralNode.Type.DOUBLE)) {
            typeId = PrimitiveType.DOUBLE;
        }
        if (node.getType().equals(LiteralNode.Type.INTEGER)) {
            typeId = PrimitiveType.INTEGER;
        }
        
        lastType = PrimitiveTypeManager.getInstance().getTypeById(typeId);
    }
}
