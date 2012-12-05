package ru.tsu.inf.cdel.semantical.function;

import java.util.List;
import ru.tsu.inf.cdel.semantical.SemanticalError;
import ru.tsu.inf.cdel.semantical.Variables;
import ru.tsu.inf.cdel.semantical.type.Type;
import ru.tsu.inf.cdel.semantical.type.VoidType;

abstract public class Function {
    
    public static Variables createVariblesForReturnType(Type returnType) {
        Variables vars = new Variables();
        if (!(returnType instanceof VoidType)) {
            vars.setVariable("result", returnType);
        }
        
        return vars;
    }
    
    private String name;
    private Type returnType;

    public Function(String name, Type returnType) {
        this.name = name;
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public Type getReturnType() {
        return returnType;
    }
    
    public String toString() {
        return getName() + ":" + getReturnType();
    }
    
    abstract public boolean checkApplicability(Type[] received, List< String > errors);
}
