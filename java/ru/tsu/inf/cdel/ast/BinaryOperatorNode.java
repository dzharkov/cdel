package ru.tsu.inf.cdel.ast;

import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;

public class BinaryOperatorNode extends ExpressionNode {
    ASTNode a;
    TerminalNode op;
    ASTNode b;

    public BinaryOperatorNode(ASTNode a, TerminalNode op, ASTNode b) {
        this.a = a;
        this.op = op;
        this.b = b;
    }

    public ASTNode getA() {
        return a;
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