package ru.tsu.inf.cdel.ast;

import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;

public class BlockNode extends ASTNode {
    ListNode declaredVariables;
    ASTNode statement;

    public BlockNode(ListNode declar, ASTNode statement) {
        this.declaredVariables = declar;
        this.statement = statement;
    }

    public ListNode getDeclar() {
        return declaredVariables;
    }

    public ASTNode getStatement() {
        return statement;
    }
    
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}