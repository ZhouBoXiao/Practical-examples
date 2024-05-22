package com.example.spring.test;

import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DynamicTestsWithStreamDemo {

    @TestFactory
    Stream<DynamicTest> dynamicTestsFromStream() {
        return IntStream.iterate(0, n -> n + 2).limit(5)
                .mapToObj(n -> DynamicTest.dynamicTest("Test " + n, () -> assertTrue(isEven(n))));
    }

    boolean isEven(int number) {
        return number % 2 == 0;
    }

    @TestFactory
    Collection<DynamicTest> dynamicTests() {
        return Arrays.asList(
                DynamicTest.dynamicTest("Test 1", () -> assertTrue(isEven(2))),
                DynamicTest.dynamicTest("Test 2", () -> assertTrue(isEven(4))),
                DynamicTest.dynamicTest("Test 3", () -> assertTrue(isEven(6)))
        );
    }

    @TestFactory
    Stream<DynamicContainer> dynamicContainers() {
        return Stream.of(
                DynamicContainer.dynamicContainer("Even Tests",
                        Arrays.asList(
                                DynamicTest.dynamicTest("Test 2", () -> assertTrue(isEven(2))),
                                DynamicTest.dynamicTest("Test 4", () -> assertTrue(isEven(4)))
                        )
                ),
                DynamicContainer.dynamicContainer("Odd Tests",
                        Arrays.asList(
                                DynamicTest.dynamicTest("Test 1", () -> assertTrue(isOdd(1))),
                                DynamicTest.dynamicTest("Test 3", () -> assertTrue(isOdd(3)))
                        )
                )
        );
    }

    boolean isOdd(int number) {
        return number % 2 != 0;
    }
}