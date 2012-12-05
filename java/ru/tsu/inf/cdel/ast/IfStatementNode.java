package ru.tsu.inf.cdel.ast;

import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;

public class IfStatementNode extends ASTNode {
    ExpressionNode condition;
    ASTNode thenStatement, elseStatement;
    
    TerminalNode ifWord;

    public IfStatementNode(ExpressionNode condition, ASTNode thenStatement, ASTNode elseStatement, TerminalNode ifWord) {
        this.condition = condition;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
        this.ifWord = ifWord;
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public ASTNode getThenStatement() {
        return thenStatement;
    }

    public ASTNode getElseStatement() {
        return elseStatement;
    }

    public TerminalNode getIfWord() {
        return ifWord;
    }
       
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}