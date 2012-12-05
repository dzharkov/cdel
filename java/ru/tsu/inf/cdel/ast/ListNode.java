package ru.tsu.inf.cdel.ast;

import java.util.LinkedList;
import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;

public class ListNode extends ASTNode {
    protected ASTNode item;
    protected ListNode list;

    public ListNode(ASTNode item, ListNode list) {
        super();
        this.item = item;
        this.list = list;
    }

    @Override
    public ASTNode[] getChildren() {
        LinkedList< ASTNode > listResult = new LinkedList<>();
        if (item != null) {
            listResult.add(item);
        }
        if (list != null) {
            for (ASTNode node : list.getChildren()) {
                listResult.add(node);
            }
        }
        
        ASTNode[] result = new ASTNode[listResult.size()];
        
        return listResult.toArray(result);
    }

    public ListNode getList() {
        return list;
    }

    public ASTNode getItem() {
        return item;
    }
    
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
