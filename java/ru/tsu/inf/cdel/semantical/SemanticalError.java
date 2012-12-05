package ru.tsu.inf.cdel.semantical;

import ru.tsu.inf.cdel.ast.ASTNode;

public class SemanticalError extends Error {
    
    public SemanticalError(String message) {
        super(message);
    }
    
    public SemanticalError(String message, String position) {
        this(message + " at " + position);
    }
    
    public SemanticalError(String message, ASTNode node) {
       this(message, node.getPosition());
    }
    
}
