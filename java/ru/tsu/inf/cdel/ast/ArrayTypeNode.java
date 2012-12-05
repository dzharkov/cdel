package ru.tsu.inf.cdel.ast;

import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;

public class ArrayTypeNode extends ASTNode {
    ListNode list;
    ASTNode type;

    public ArrayTypeNode(ListNode list, ASTNode typeNode) {
        this.list = list;
        this.type = typeNode;
    }

    public ListNode getList() {
        return list;
    }

    public ASTNode getType() {
        return type;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}