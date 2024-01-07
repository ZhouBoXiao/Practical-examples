package com.calculator.core.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class ListenerBasedCalculatorTest {

    @DisplayName("evaluate simple mathematical expressions")
    @ParameterizedTest
    @MethodSource("com.calculator.core.parser.MathExamples#mathExamples")
    void evaluateMathExpressions(String expression, Double expectedResult) {
        Calculator calculator = new ListenerBasedCalculator();
        Double result = calculator.calculate(expression);
        assertEquals(expectedResult, result);
    }
}