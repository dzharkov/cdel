package ru.tsu.inf.cdel.ast;

import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;

public class ProgramNode extends BlockNode {
    private ListNode procAndFunc;

    public ProgramNode(ListNode procAndFunc, ListNode declaredVariables, ASTNode statement) {
        super(declaredVariables, statement);
        this.procAndFunc = procAndFunc;
    }

    public ListNode getProcAndFunc() {
        return procAndFunc;
    }
    
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}