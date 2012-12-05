package ru.tsu.inf.cdel.ast;

import ru.tsu.inf.cdel.ast.visitor.ASTNodeVisitor;

public class AssignmentStatementNode extends ASTNode {
    VariableNode variable;
    ExpressionNode expression;
    
    TerminalNode assOp;

    public AssignmentStatementNode(VariableNode variable, ExpressionNode expression, TerminalNode assOp) {
        this.variable = variable;
        this.expression = expression;
        this.assOp = assOp;
    }

    public TerminalNode getAssOp() {
        return assOp;
    }

    public VariableNode getVariable() {
        return variable;
    }

    public ExpressionNode getExpression() {
        return expression;
    }
    
    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}