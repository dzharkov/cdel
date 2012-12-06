package ru.tsu.inf.cdel.semantical.function;

import java.util.List;
import ru.tsu.inf.cdel.semantical.type.Type;

public class ReadFunction extends Function {
    public ReadFunction(Type type) {
        super("read_" + type.toString(), type);
    }

    @Override
    public boolean checkApplicability(Type[] received, List<String> errors) {
        if (received.length > 0) {
            errors.add("there should be no parameters for " + getName());
            return false;
        }
        return true;
    }
}
