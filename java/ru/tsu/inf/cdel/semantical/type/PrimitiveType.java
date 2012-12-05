package ru.tsu.inf.cdel.semantical.type;

import ru.tsu.inf.cdel.semantical.Operator;

public class PrimitiveType extends Type {
    public static final int INTEGER = 0;
    public static final int DOUBLE = 1;
    public static final int STRING = 2;
    public static final int BOOL = 3;
    private Integer typeId;

    public PrimitiveType(Integer typeId) {
        this.typeId = typeId;
    }

    @Override
    public String getId() {
        return "primitive_" + typeId.toString();
    }

    public Integer getTypeId() {
        return typeId;
    }

    @Override
    public boolean canBeBinaryOperatorApplied(Operator op, Type p) {
        return getTypeAfterBinaryOperatopApplied(op, p) != null;
    }

    @Override
    public Type getTypeAfterBinaryOperatopApplied(Operator op, Type p) {
        if (!(p instanceof PrimitiveType)) {
            return null;
        }
        
        return PrimitiveTypeManager.getInstance().getTypeOfBinaryOperatorApplication(op, typeId, ((PrimitiveType)p).getTypeId());
    }

    @Override
    public String toString() {
        if (typeId == DOUBLE) {
            return "double";
        }
        if (typeId == BOOL) {
            return "bool";
        }
        if (typeId == INTEGER) {
            return "integer";
        }
        return "string";       
    }    
}
