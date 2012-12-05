package ru.tsu.inf.cdel.ast.visitor;

import ru.tsu.inf.cdel.ast.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

abstract public class ASTNodeVisitor {
    public void visit(ArrayTypeNode node) {
        throw new NotImplementedException();
    }
    public void visit(AssignmentStatementNode node) {
        throw new NotImplementedException();
    }
    public void visit(BinaryOperatorNode node) {
        throw new NotImplementedException();
    }
    public void visit(BlockNode node) {
        throw new NotImplementedException();
    }
    public void visit(BuiltinTypeNode node) {
        throw new NotImplementedException();
    }
    public void visit(ForStatementNode node) {
        throw new NotImplementedException();
    }
    public void visit(FormalParameterSectionNode node) {
        throw new NotImplementedException();
    }
    public void visit(FunctionDeclarationNode node) {
        throw new NotImplementedException();
    }
    public void visit(FunctionDesignatorNode node) {
        throw new NotImplementedException();
    }
    public void visit(IdentVariableNode node) {
        throw new NotImplementedException();
    }
    public void visit(IfStatementNode node) {
        throw new NotImplementedException();
    }
    public void visit(IndexedVariableNode node) {
        throw new NotImplementedException();
    }
    public void visit(ListNode node) {
        throw new NotImplementedException();
    }
    public void visit(ProcedureDeclarationNode node) {
        throw new NotImplementedException();
    }
    public void visit(ProcedureStatementNode node) {
        throw new NotImplementedException();
    }
    public void visit(ProgramNode node) {
        throw new NotImplementedException();
    }
    public void visit(StatementSequenceNode node) {
        throw new NotImplementedException();
    }
    public void visit(SubrangeTypeNode node) {
        throw new NotImplementedException();
    }
    protected String curTerminalValue;
    public void visit(TerminalNode node) {
        curTerminalValue = node.getLexemeValue();
    }
    
    protected String getTerminalValue(ASTNode node) {
        node.accept(this);
        return curTerminalValue;
    }
    
    public void visit(UnaryOperatorNode node) {
        throw new NotImplementedException();
    }
    public void visit(VariableDeclarationNode node) {
        throw new NotImplementedException();
    }
    public void visit(SimpleTypeNode node) {
        throw new NotImplementedException();
    }
    public void visit(LiteralNode node) {
        throw new NotImplementedException();
    }
}
