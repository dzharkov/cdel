package ru.tsu.inf.cdel.ast;

import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;

public class SimpleTypeNode extends ASTNode {
    private TerminalNode ident;

    public SimpleTypeNode(TerminalNode ident) {
        this.ident = ident;
    }

    public TerminalNode getIdent() {
        return ident;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
