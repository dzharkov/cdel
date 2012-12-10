package ru.tsu.inf.cdel.semantical.type;

import java.util.List;

public class ArrayType extends Type {
    
   
    
    private ArrayDimension[] dims;
    private Type ofType;

    public ArrayType(ArrayDimension[] dims, Type ofType) {
        this.dims = dims;
        this.ofType = ofType;
    }

    @Override
    public String getId() {
        return String.valueOf(dims.length) + "-d array of " + ofType;
    }

    public ArrayDimension[] getDims() {
        return dims;
    }

    public Type getOfType() {
        return ofType;
    }
    
    public Type getTypeAfterIndexing(int times) {
        if (times == dims.length) {
            return ofType;
        }
        
        ArrayDimension[] newDims = new ArrayDimension[dims.length - times];
        
        for (int i = times; i < dims.length; i++) {
            newDims[i - times]=dims[i];
        }
        
        return new ArrayType(newDims, ofType);
    }

    @Override
    public Type getTypeAfterIndexing(Type[] types, List< String > errors) {
        if (types.length > dims.length) {
            errors.add("variable can't be indexed for " +types.length + " times");
            return null;
        }
        
        for (Type type : types) {
            if (!(type instanceof PrimitiveType) || ((PrimitiveType)type).getTypeId() != PrimitiveType.INTEGER) {
                errors.add("value should be integer for indexing");
            } 
        }
        
        if (!errors.isEmpty()) {
            return null;
        }
        
        return getTypeAfterIndexing(types.length);
    }
}
