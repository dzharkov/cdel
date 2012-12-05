package ru.tsu.inf.cdel.ast;

import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;

public class FormalParameterSectionNode extends ASTNode {
    ListNode list;
    TerminalNode type;

    public FormalParameterSectionNode(ListNode list, TerminalNode type) {
        this.list = list;
        this.type = type;
    }

    public TerminalNode getType() {
        return type;
    }

    public ListNode getList() {
        return list;
    }
    
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}