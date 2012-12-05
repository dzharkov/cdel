package ru.tsu.inf.cdel.ast;

import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;

public class UnaryOperatorNode extends ExpressionNode {
    TerminalNode op;
    ASTNode b;

    public UnaryOperatorNode(TerminalNode op, ASTNode b) {
        this.op = op;
        this.b = b;
    }

    public ASTNode getB() {
        return b;
    }

    public TerminalNode getOp() {
        return op;
    }
    
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}