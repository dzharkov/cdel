package ru.tsu.inf.cdel.ast;

import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;

public class ForStatementNode extends ASTNode {
    ASTNode initialStatement, postStatement, loopStatement;
    ExpressionNode condition;
    
    TerminalNode forWord;

    public ForStatementNode(ASTNode initialStatement, ExpressionNode condition,ASTNode postStatement, ASTNode loopStatement, TerminalNode forWord) {
        this.initialStatement = initialStatement;
        this.postStatement = postStatement;
        this.loopStatement = loopStatement;
        this.condition = condition;
        this.forWord = forWord;
    }

    public ASTNode getInitialStatement() {
        return initialStatement;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public ASTNode getPostStatement() {
        return postStatement;
    }

    public ASTNode getLoopStatement() {
        return loopStatement;
    }

    public TerminalNode getForWord() {
        return forWord;
    }
    
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}