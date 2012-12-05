package ru.tsu.inf.cdel.semantical.function;

import java.util.List;
import ru.tsu.inf.cdel.ast.ASTNode;
import ru.tsu.inf.cdel.semantical.SemanticalError;
import ru.tsu.inf.cdel.semantical.Variables;
import ru.tsu.inf.cdel.semantical.type.Type;

public class SrcFunction extends Function {
    private Variables params;
    private Variables declaredVariables;
    private ASTNode block;

    public SrcFunction(Variables params, Variables declaredVariables, ASTNode block, String name, Type returnType) {
        super(name, returnType);
        this.params = params;
        this.declaredVariables = declaredVariables;
        this.block = block;
    }

    public ASTNode getBlock() {
        return block;
    }

    public Variables getDeclaredVariables() {
        return declaredVariables;
    }
    
    public Variables getAllVariables() {
        return declaredVariables.merge(params);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    public Variables getParams() {
        return params;
    }

    @Override
    public Type getReturnType() {
        return super.getReturnType();
    }

    @Override
    public boolean checkApplicability(Type[] received, List< String > errors ) {
        if (received.length != params.getAmount()) {
            errors.add("parameters amount is not right");
            return false;
        }
        
        for (int i = 0; i < params.getAmount(); i++) {
            if (!received[i].canBeCastedTo(params.getTypeByIndex(i))) {
               errors.add("parameter " + String.valueOf(i+1) +  " error: " + received[i] + " can't be casted to " + params.getTypeByIndex(i));
            }
        }
        
        return errors.isEmpty();
    }

    @Override
    public String toString() {
        return super.toString() + "( params=" + params + ", vars=" + declaredVariables + ")";
    }
}
