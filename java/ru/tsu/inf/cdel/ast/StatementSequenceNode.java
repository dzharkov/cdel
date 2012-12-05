package ru.tsu.inf.cdel.ast;

import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;

public class StatementSequenceNode extends ListNode {

    public StatementSequenceNode(ASTNode item, ListNode list) {
        super(item, list);
    }
    
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
    
}
