package ru.tsu.inf.cdel.ast;

import java.lang.reflect.Field;
import java.util.LinkedList;
import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;


abstract public class ASTNode {
    protected ASTNode[] children;
    
    protected Integer leftLine,leftColumn,rightLine,rightColumn;
    
    public ASTNode() {

    }
    
    public ASTNode(ASTNode[] chldrn) {
        children = chldrn;
    }

    public ASTNode[] getChildren() {
        LinkedList< ASTNode > childrenList = new LinkedList<>();
        Class curClass = this.getClass();
        while (!curClass.getName().equals(ASTNode.class.getName())) {
            for (Field f : curClass.getDeclaredFields() ) {
                if (ASTNode.class.isAssignableFrom(f.getType())) {
                    f.setAccessible(true);
                    try {
                        Object o = f.get(this);
                        if (o != null) {
                            childrenList.add((ASTNode)f.get(this));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    f.setAccessible(false);
                }
            }
            curClass = curClass.getSuperclass();
        }
        
        children = new ASTNode[childrenList.size()];
        
        childrenList.toArray(children);
        return children;
    }
    
    public String getNodeInfo() {
        return this.getClass().getSimpleName();
    }

    public Integer getLeftColumn() {
        return leftColumn;
    }

    public Integer getLeftLine() {
        return leftLine;
    }

    public Integer getRightColumn() {
        return rightColumn;
    }

    public Integer getRightLine() {
        return rightLine;
    }
    
    public String getPosition() {
        return "line " + getLeftLine().toString() + ", column " + getLeftColumn().toString(); 
    }
    
    abstract public void accept(ASTNodeVisitor visitor);
    
}
