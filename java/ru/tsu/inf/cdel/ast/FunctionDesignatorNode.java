package ru.tsu.inf.cdel.ast;

import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;

public class FunctionDesignatorNode extends ExpressionNode {
    TerminalNode ident;
    ListNode parameterList;

    public FunctionDesignatorNode(TerminalNode ident, ListNode parameterList) {
        this.ident = ident;
        this.parameterList = parameterList;
    }

    public TerminalNode getIdent() {
        return ident;
    }

    public ListNode getParameterList() {
        return parameterList;
    }
    
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}