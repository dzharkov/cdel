package ru.tsu.inf.cdel.generator;

import java.io.IOException;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import org.apache.bcel.Constants;

public class JVMClassGenerator {
    public void generateTo(String className, String filename) {
        InstructionList il = new InstructionList();

        ClassGen  cg = new ClassGen(className, "java.lang.Object",
                                    filename, Constants.ACC_PUBLIC | Constants.ACC_SUPER,
                                    null);
        

        InstructionFactory f  = new InstructionFactory(cg);
        
        /*FieldGen fg = new FieldGen(Constants.ACC_PUBLIC | Constants.ACC_STATIC | Constants.ACC_FINAL, Type.STRING, "field", cg.getConstantPool());
        fg.setInitValue("Hello World!");
        
        cg.addField(fg.getField());
        */
        //il.append(f.createConstant("Hello it!"));
        //il.append(f.createPutStatic(className, "field", Type.STRING));
        
        ObjectType p_stream = new ObjectType("java.io.PrintStream");

 /*              
        il.append(f.createFieldAccess(className, "field1", Type.STRING, Constants.GETSTATIC));
   */     
il.append(f.createFieldAccess("java.lang.System", "out", p_stream,
                                        Constants.GETSTATIC));
  il.append(new PUSH(cg.getConstantPool(), "Please enter your name> "));
  il.append(f.createInvoke("java.io.PrintStream", "print", Type.VOID, 
                                 new Type[] { Type.STRING },
                                 Constants.INVOKEVIRTUAL));
  il.append(InstructionConstants.RETURN);
        
       // cg.addMethod(mg.getMethod());
        
        il.dispose(); // Reuse instruction handles of list
        try {
            cg.getJavaClass().dump(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
