package ru.tsu.inf.cdel.semantical.type;

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
}
