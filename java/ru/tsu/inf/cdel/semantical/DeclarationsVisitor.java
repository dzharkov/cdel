package ru.tsu.inf.cdel.semantical;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import ru.tsu.inf.cdel.ast.*;
import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;
import ru.tsu.inf.cdel.semantical.function.Function;
import ru.tsu.inf.cdel.semantical.function.SrcFunction;
import ru.tsu.inf.cdel.semantical.type.*;

public class DeclarationsVisitor extends ASTNodeVisitor {
    private List< SemanticalError > errors = new LinkedList<>(); 
    private HashMap< String, Function > funcMap = new HashMap<>();
    private Variables vars = new Variables();
    private ASTNode block;
    
    private Type curType;
    private Variables currentParams;
    
    private int curRangeFrom,curRangeTo;
    
    public DeclarationsVisitor() {
    }

    @Override
    public void visit(ProgramNode node) {
        errors = new LinkedList<>();
        funcMap = new HashMap<>();
        
        for (ASTNode funcDeclaration : node.getProcAndFunc().getChildren()) {
            funcDeclaration.accept(this);
        }
        
        vars = new Variables();
        BlockNode blockNode = (BlockNode)node;
        visit(blockNode);
    }
    
    private Type getTypeFromTerminalNode(ASTNode node) {
        try {
            Type type = PrimitiveTypeManager.getInstance().getTypeByName(super.getTerminalValue(node));
            return type;
        } catch(SemanticalError e) {
            errors.add(new SemanticalError(e.getMessage(), node));
            return null;
        }
    }
    
    @Override
    public void visit(FormalParameterSectionNode node) {
        Type type = getTypeFromTerminalNode(node.getType());
        if (type == null) {
            return;
        }
        for (ASTNode param : node.getList().getChildren()) {
            String name = this.getTerminalValue(param);
            if (currentParams.ifExists(name)) {
                errors.add(new SemanticalError("parameter " + name + " is already exist", node));
            } else {
                currentParams.setVariable(name, type);
            }
        }
    }

    @Override
    public void visit(TerminalNode node) {
        super.visit(node);
    }

    @Override
    public void visit(SimpleTypeNode node) {
        curType = getTypeFromTerminalNode(node.getIdent());
    }

    @Override
    public void visit(SubrangeTypeNode node) {
       curRangeFrom = Integer.valueOf(getTerminalValue(node.getFrom()));
       curRangeTo = Integer.valueOf(getTerminalValue(node.getTo()));
    }
    
    @Override
    public void visit(ArrayTypeNode node) {
        node.getType().accept(this);
        Type ofType = curType;
        curType=null;
        
        ASTNode[] subrangeNodes = node.getList().getChildren();
        ArrayDimension[] dims = new ArrayDimension[subrangeNodes.length];
        
        for (int i = 0; i < subrangeNodes.length; i++) {
            subrangeNodes[i].accept(this);
            if (curRangeFrom >= curRangeTo) {
                errors.add(new SemanticalError("wrong array sub range", subrangeNodes[i].getChildren()[0]));
                return;
            }
            
            dims[i] = new ArrayDimension(curRangeFrom, curRangeTo);
        }
        
        curType = new ArrayType(dims, ofType);
    }

    @Override
    public void visit(BuiltinTypeNode node) {
        String typeIdent = getTerminalValue(node.getIdent());
        
        if (typeIdent.equalsIgnoreCase("map")) {
            ASTNode[] list = node.getTypesList().getChildren();
            if (list.length != 2) {
                errors.add(new SemanticalError("wrong parameters amount", node.getIdent()));
                return;
            }
            Type keyType = getTypeFromTerminalNode(list[0]);
            Type valueType = getTypeFromTerminalNode(list[1]);
            
            if (keyType == null || valueType == null) {
                return;
            }
            
            curType = new HashMapType(keyType, valueType);
        } else {
            errors.add(new SemanticalError("There is no complex type " + typeIdent, node.getIdent()));
        }
        
    }

    @Override
    public void visit(VariableDeclarationNode node) {
        curType = null;
        node.getType().accept(this);
        Type type = curType;
        
        if (type == null) {
            return ;
        }
        
        for (ASTNode identNode : node.getIdentifiers().getChildren()) {
            String ident = this.getTerminalValue(identNode);
            if (currentParams != null && currentParams.ifExists(ident)) {
                errors.add(new SemanticalError("naming intersection " + ident, identNode));
                continue;
            }
            if (vars.ifExists(ident)) {
                errors.add(new SemanticalError("naming intersection " + ident, identNode));
                continue;
            }
            vars.setVariable(ident, type);
        }
    }
    
    @Override
    public void visit(BlockNode node) {
        
        for (ASTNode varDeclar : node.getDeclar().getChildren()) {
            varDeclar.accept(this);
        }
        block = node.getStatement();
    }
    
    private void buildCurrentFunc(ProcedureDeclarationNode node, Type returnType) {
        vars = Function.createVariblesForReturnType(returnType);
        currentParams = new Variables();
        
        for (ASTNode paramSection : node.getParams().getChildren()) {
            paramSection.accept(this);
        }
        
        node.getBlock().accept(this);
        
        Function curFunc = new SrcFunction(currentParams, vars, block, getTerminalValue(node.getIdent()), returnType);
                
        currentParams = null;
        
        if (funcMap.containsKey(curFunc.getName().toLowerCase())) {
            errors.add(new SemanticalError("function redeclaring", node.getIdent()));
            return;
        }
        
        funcMap.put(curFunc.getName().toLowerCase(), curFunc);
    }
    
    @Override
    public void visit(ProcedureDeclarationNode node) {
        buildCurrentFunc(node, new VoidType());
    }

    @Override
    public void visit(FunctionDeclarationNode node) {
        Type returnType = getTypeFromTerminalNode(node.getType());
        buildCurrentFunc(node, returnType);
    }

    public ASTNode getBlock() {
        return block;
    }

    public List<SemanticalError> getErrors() {
        return errors;
    }

    public HashMap<String, Function> getFuncMap() {
        return funcMap;
    }

    public Variables getVars() {
        return vars;
    }
}
