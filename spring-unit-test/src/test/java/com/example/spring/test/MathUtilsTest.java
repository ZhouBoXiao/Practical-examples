package com.example.spring.test;

import com.example.spring.test.utils.MathUtils;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MathUtilsTest {

    private final MathUtils mathUtils = new MathUtils();

    @TestFactory
    Collection<DynamicTest> dynamicTestsForMathOperations() {
        return Arrays.asList(
                DynamicTest.dynamicTest("Addition Test", () -> assertEquals(5, mathUtils.add(2, 3))),
                DynamicTest.dynamicTest("Subtraction Test", () -> assertEquals(1, mathUtils.subtract(3, 2))),
                DynamicTest.dynamicTest("Multiplication Test", () -> assertEquals(6, mathUtils.multiply(2, 3))),
                DynamicTest.dynamicTest("Division Test", () -> assertEquals(2, mathUtils.divide(6, 3))),
                DynamicTest.dynamicTest("Division by zero Test", () -> {
                    Executable executable = () -> mathUtils.divide(1, 0);
                    assertThrows(IllegalArgumentException.class, executable);
                })
        );
    }

    @TestFactory
    Stream<DynamicTest> dynamicTestsForMultipleScenarios() {
        int[][] addData = {
                {1, 1, 2},
                {2, 2, 4},
                {3, 3, 6},
                {4, 4, 8}
        };

        int[][] subtractData = {
                {5, 3, 2},
                {10, 5, 5},
                {15, 10, 5},
                {20, 15, 5}
        };

        return Stream.concat(
                Arrays.stream(addData).map(data -> DynamicTest.dynamicTest(
                        "Add: " + data[0] + " + " + data[1] + " = " + data[2],
                        () -> assertEquals(data[2], mathUtils.add(data[0], data[1]))
                )),
                Arrays.stream(subtractData).map(data -> DynamicTest.dynamicTest(
                        "Subtract: " + data[0] + " - " + data[1] + " = " + data[2],
                        () -> assertEquals(data[2], mathUtils.subtract(data[0], data[1]))
                ))
        );
    }
}