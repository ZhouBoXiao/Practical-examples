package com.calculator.core.evaluator;

public class CalculationVisitor extends CalculatorBaseVisitor<Double> {

    @Override
    public Double visitNumber(CalculatorParser.NumberContext ctx) {
        return Double.parseDouble(ctx.NUMBER().getText());
    }

    @Override
    public Double visitNegation(CalculatorParser.NegationContext ctx) {
        return -1 * this.visit(ctx.right);
    }

    @Override
    public Double visitAdditionOrSubtraction(CalculatorParser.AdditionOrSubtractionContext ctx) {
        if (ctx.operator.getText().equals("+")) {
            return this.visit(ctx.left) + this.visit(ctx.right);
        }
        return this.visit(ctx.left) - this.visit(ctx.right);
    }

    @Override
    public Double visitMultiplicationOrDivision(CalculatorParser.MultiplicationOrDivisionContext ctx) {
        if (ctx.operator.getText().equals("*")) {
            return this.visit(ctx.left) * this.visit(ctx.right);
        }
        return this.visit(ctx.left) / this.visit(ctx.right);
    }

    @Override
    public Double visitParentheses(CalculatorParser.ParenthesesContext ctx) {
        return this.visit(ctx.inner);
    }

    @Override
    public Double visitPower(CalculatorParser.PowerContext ctx) {
        return Math.pow(this.visit(ctx.left), this.visit(ctx.right));
    }
}
