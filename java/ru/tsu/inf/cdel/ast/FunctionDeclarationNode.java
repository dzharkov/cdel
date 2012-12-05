package ru.tsu.inf.cdel.ast;

import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;

public class FunctionDeclarationNode extends ProcedureDeclarationNode {
    private TerminalNode type;

    public FunctionDeclarationNode(TerminalNode ident, ListNode params, TerminalNode type, BlockNode block) {
        super(ident, params, block);
        this.type = type;
    }

    public TerminalNode getType() {
        return type;
    }
    
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}