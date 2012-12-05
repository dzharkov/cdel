package ru.tsu.inf.cdel.ast;

import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;

public class BuiltinTypeNode extends ASTNode {
    private TerminalNode ident;
    private ListNode typesList;

    public BuiltinTypeNode(TerminalNode ident, ListNode typesList) {
        this.ident = ident;
        this.typesList = typesList;
    }

    public TerminalNode getIdent() {
        return ident;
    }

    public ListNode getTypesList() {
        return typesList;
    }
    
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
