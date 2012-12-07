package ru.tsu.inf.cdel.semantical.function;

import java.util.List;
import ru.tsu.inf.cdel.semantical.type.Type;
import ru.tsu.inf.cdel.semantical.type.VoidType;

public class WriteFunction extends Function {

    public WriteFunction() {
        super("write", new VoidType());
    }

    @Override
    public boolean checkApplicability(Type[] received, List<String> errors) {
        if (received.length != 1) {
            errors.add("wrong arguments amount");
        }
        for (Type t: received) {
            if (!t.getId().contains("primitive_")) {
                errors.add("Arguments should be primitive");
                return false;
            }
        }
        return true;
    }
    
}
