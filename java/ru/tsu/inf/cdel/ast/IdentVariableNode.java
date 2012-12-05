package ru.tsu.inf.cdel.ast;

import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;

public class IdentVariableNode extends VariableNode {
    TerminalNode ident;

    public IdentVariableNode(TerminalNode ident) {
        this.ident = ident;
    }

    public TerminalNode getIdent() {
        return ident;
    }
    
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }    
}
