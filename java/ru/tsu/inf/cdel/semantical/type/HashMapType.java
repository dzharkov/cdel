package ru.tsu.inf.cdel.semantical.type;

import java.util.List;

public class HashMapType extends Type {
    private Type keyType,valueType;

    public HashMapType(Type keyType, Type valueType) {
        this.keyType = keyType;
        this.valueType = valueType;
    }

    @Override
    public String getId() {
        return "HashMap< " + keyType.getId() + ", " + valueType.getId() + ">";
    }

    public Type getKeyType() {
        return keyType;
    }

    public Type getValueType() {
        return valueType;
    }

    @Override
    public Type getTypeAfterIndexing(Type[] types, List<String> errors) {
        if (types.length != 1) {
            errors.add("Wrong indexing parameters amount for map");
            return null;
        }
        
        return types[0].equals(keyType) ? valueType : null;
    }

}
