package ru.tsu.inf.cdel.ast;

import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;

public class LiteralNode extends ExpressionNode {
    public static enum Type { INTEGER, DOUBLE, STRING };
    
    private Type type;
    private TerminalNode terminal;

    public LiteralNode(Type type, TerminalNode terminal) {
        this.type = type;
        this.terminal = terminal;
    }

    public TerminalNode getTerminal() {
        return terminal;
    }

    public Type getType() {
        return type;
    }     
    
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
    
}
