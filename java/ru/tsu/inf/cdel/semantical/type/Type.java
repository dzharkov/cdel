package ru.tsu.inf.cdel.semantical.type;

import java.util.HashMap;
import java.util.List;
import ru.tsu.inf.cdel.semantical.Operator;
import ru.tsu.inf.cdel.semantical.SemanticalError;

public abstract class Type {

    abstract public String getId();
    
    public boolean canBeCastedTo(Type type) {
        return this.getId().equalsIgnoreCase(type.getId());
    }
    
    public String toString() {
        return getId();
    }
    
    public boolean equals(Type a) {
        return getId().equals(a.getId());
    }
    
    public boolean canBeBinaryOperatorApplied(Operator op, Type p) {
        return false;
    }
    
    public Type getTypeAfterBinaryOperatopApplied(Operator op, Type p) {
        return null;
    }
    
    public boolean canBeUnaryOperatorApplied(Operator op) {
        return false;
    }
    
    public Type getTypeAfterUnaryOperatopApplied(Operator op) {
        return null;
    }
    
    public Type getTypeAfterIndexing(Type[] types,  List< String > errors) {
        return null;
    }
}
