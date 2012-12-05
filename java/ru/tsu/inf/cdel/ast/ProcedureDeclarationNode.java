package ru.tsu.inf.cdel.ast;

import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;

public class ProcedureDeclarationNode extends ASTNode {
    TerminalNode ident;
    ListNode params;
    BlockNode block;

    public ProcedureDeclarationNode(TerminalNode ident, ListNode params, BlockNode block) {
        this.ident = ident;
        this.params = params;
        this.block = block;
    }

    public TerminalNode getIdent() {
        return ident;
    }

    public ListNode getParams() {
        return params;
    }

    public BlockNode getBlock() {
        return block;
    }
    
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}