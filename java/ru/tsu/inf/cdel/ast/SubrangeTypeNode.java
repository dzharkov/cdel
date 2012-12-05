package ru.tsu.inf.cdel.ast;

import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;

public class SubrangeTypeNode extends ASTNode {
    TerminalNode from, to;

    public SubrangeTypeNode(TerminalNode from, TerminalNode to) {
        this.from = from;
        this.to = to;
    }

    public TerminalNode getFrom() {
        return from;
    }

    public TerminalNode getTo() {
        return to;
    }
    
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}