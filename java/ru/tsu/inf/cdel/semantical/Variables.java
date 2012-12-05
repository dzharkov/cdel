package ru.tsu.inf.cdel.semantical;

import java.util.ArrayList;
import java.util.HashMap;
import ru.tsu.inf.cdel.semantical.type.Type;

public class Variables {
    ArrayList< Type > types;
    ArrayList< String > names;
    HashMap< String, Integer > variablesMap;
    
    public Variables() {
        types = new ArrayList<>();
        variablesMap = new HashMap<>();
        names = new ArrayList<>();
    }
    
    public void setVariable(String ident, Type type) {
        String lowerIdent = ident.toLowerCase();
        if (variablesMap.containsKey(lowerIdent)) {
            types.set(variablesMap.get(lowerIdent), type);
            return;
        }
        
        int newIndex = variablesMap.size();
        variablesMap.put(lowerIdent, newIndex);
        
        types.add(type); 
        names.add(ident);
    }
    
    public Type getTypeByIndex(int index) {
        return types.get(index);
    }
    
    public Type getTypeByName(String name) {
        return getTypeByIndex(variablesMap.get(name));
    }
    
    public int getAmount() {
        return types.size();
    }
    
    public boolean ifExists(String ident) {
        return variablesMap.containsKey(ident.toLowerCase());
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < names.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(names.get(i) + ": " + types.get(i));
        }
        
        return sb.toString();
    }
    
    public Variables merge(Variables a) {
        
        Variables result = new Variables();
        
        for (int i = 0; i < names.size(); i++) {
            result.setVariable(names.get(i), types.get(i));
        }
        
        for (int i = 0; i < a.names.size(); i++) {
            result.setVariable(a.names.get(i), a.types.get(i));
        }
        
        return result;
    }
}
