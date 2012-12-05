package ru.tsu.inf.cdel.ast;

import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;

public class VariableDeclarationNode extends ASTNode {
    ASTNode type;
    ListNode identifiers;

    public VariableDeclarationNode(ASTNode type, ListNode identifiers) {
        this.type = type;
        this.identifiers = identifiers;
    }

    public ASTNode getType() {
        return type;
    }

    public ListNode getIdentifiers() {
        return identifiers;
    }
    
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}