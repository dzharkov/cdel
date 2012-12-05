package ru.tsu.inf.cdel.ast;

import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;

public class ProcedureStatementNode extends ASTNode {
    TerminalNode ident;
    ListNode list;

    public ProcedureStatementNode(TerminalNode ident, ListNode list) {
        this.ident = ident;
        this.list = list;
    }

    public TerminalNode getIdent() {
        return ident;
    }

    public ListNode getList() {
        return list;
    }
    
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}