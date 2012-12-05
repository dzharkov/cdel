package ru.tsu.inf.cdel.ast;

import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;

public class IndexedVariableNode extends VariableNode {
    VariableNode variable;
    ListNode list;
    TerminalNode leftBracket;

    public IndexedVariableNode(VariableNode variable, ListNode list, TerminalNode leftBracket) {
        this.variable = variable;
        this.list = list;
        this.leftBracket = leftBracket;
    }

    public TerminalNode getLeftBracket() {
        return leftBracket;
    }

    public ListNode getList() {
        return list;
    }

    public VariableNode getVariable() {
        return variable;
    }
    
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
    
}