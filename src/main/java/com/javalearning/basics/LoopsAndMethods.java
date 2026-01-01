package com.javalearning.basics;

import java.util.Scanner;

/**
 * LOOPS AND METHODS
 *
 * Topics covered:
 * - While loops (#21)
 * - For loops (#23)
 * - Break & Continue (#24)
 * - Nested loops (#25)
 * - Methods (#26)
 * - Overloaded methods (#27)
 * - Variable scope (#28)
 * - Varargs (#34)
 *
 * REAL-WORLD USE CASE:
 * Loops: Processing arrays, lists, database results, API responses
 * Methods: Code reusability, similar to functions in TypeScript
 * Frequency: 100% - Used constantly in every application
 *
 * TYPESCRIPT COMPARISON:
 * Java methods are like TypeScript functions but with stricter typing
 * Java has for-each loops similar to TS for...of
 * Method overloading works differently than TS function overloads
 */
public class LoopsAndMethods {

    /**
     * WHILE LOOPS (#21)
     * Same concept as TypeScript, but stricter boolean condition
     */
    public static void demonstrateWhileLoops() {
        System.out.println("\n=== WHILE LOOPS ===");

        // Basic while loop
        int count = 1;
        System.out.println("Counting to 5:");
        while (count <= 5) {
            System.out.print(count + " ");
            count++;
        }
        System.out.println();

        // Do-while loop (executes at least once)
        int userInput;
        Scanner scanner = new Scanner(System.in);

        /* Example commented out to avoid waiting for input in demo:
        do {
            System.out.print("Enter a number between 1-10: ");
            userInput = scanner.nextInt();
        } while (userInput < 1 || userInput > 10);
        */

        System.out.println("Do-while ensures execution at least once");

        /* REAL-WORLD USE:
         * - Polling for data updates (like RxJS interval in Angular)
         * - Processing streams/iterators
         * - Game loops
         * - Validation loops (retry logic)
         * Frequency: Moderate (for loops are more common)
         */
    }

    /**
     * FOR LOOPS (#23)
     * Very similar to TypeScript
     */
    public static void demonstrateForLoops() {
        System.out.println("\n=== FOR LOOPS ===");

        // Traditional for loop (same as TypeScript)
        System.out.println("Traditional for loop:");
        for (int i = 0; i < 5; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        // For-each loop (like TypeScript for...of)
        String[] frameworks = {"Angular", "React", "Vue", "Svelte"};
        System.out.println("\nFor-each loop (enhanced for):");
        for (String framework : frameworks) {
            System.out.println("  - " + framework);
        }

        /* TypeScript comparison:
         * Java: for (String item : items)
         * TS:   for (const item of items)
         * TS:   items.forEach(item => ...) // No direct equivalent in basic Java
         */

        /* REAL-WORLD USE:
         * - Iterating over collections (like *ngFor in Angular)
         * - Processing API responses
         * - Data transformation
         * Frequency: Extremely common (daily, multiple times)
         */
    }

    /**
     * BREAK & CONTINUE (#24)
     * Identical to TypeScript
     */
    public static void demonstrateBreakContinue() {
        System.out.println("\n=== BREAK & CONTINUE ===");

        // Break - exits the loop entirely
        System.out.println("Using break (find first even number > 10):");
        for (int i = 1; i <= 20; i++) {
            if (i > 10 && i % 2 == 0) {
                System.out.println("Found: " + i);
                break; // Exit loop
            }
        }

        // Continue - skips current iteration
        System.out.println("\nUsing continue (print odd numbers only):");
        for (int i = 1; i <= 10; i++) {
            if (i % 2 == 0) {
                continue; // Skip even numbers
            }
            System.out.print(i + " ");
        }
        System.out.println();

        /* REAL-WORLD USE:
         * - Early exit from search operations
         * - Skipping invalid data in processing
         * - Filtering operations
         * Frequency: Common (few times per week)
         */
    }

    /**
     * NESTED LOOPS (#25)
     * Common in matrix operations, table generation
     */
    public static void demonstrateNestedLoops() {
        System.out.println("\n=== NESTED LOOPS ===");

        // Multiplication table
        System.out.println("Multiplication table (3x3):");
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                System.out.printf("%4d", i * j);
            }
            System.out.println();
        }

        // Pattern printing
        System.out.println("\nPyramid pattern:");
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print("* ");
            }
            System.out.println();
        }

        /* REAL-WORLD USE:
         * - Processing 2D data (tables, grids)
         * - Generating reports
         * - Matrix operations
         * - Nested data structures (like nested *ngFor in Angular)
         * Frequency: Moderate (weekly)
         */
    }

    /**
     * METHODS (#26)
     * Like TypeScript functions, but must specify return type
     */
    public static void demonstrateMethods() {
        System.out.println("\n=== METHODS ===");

        // Calling various methods
        greet("John");

        int sum = add(5, 3);
        System.out.println("5 + 3 = " + sum);

        int product = multiply(4, 7);
        System.out.println("4 × 7 = " + product);

        /* TypeScript comparison:
         * TypeScript: function add(a: number, b: number): number { return a + b; }
         * Java:       public static int add(int a, int b) { return a + b; }
         *
         * Key differences:
         * - Java requires access modifier (public/private)
         * - Java requires static for class-level methods
         * - Return type comes before method name in Java
         */
    }

    // Helper methods for demonstration
    public static void greet(String name) {
        System.out.println("Hello, " + name + "!");
    }

    public static int add(int a, int b) {
        return a + b;
    }

    public static int multiply(int a, int b) {
        return a * b;
    }

    /**
     * OVERLOADED METHODS (#27)
     * Similar to TypeScript function overloads, but implementation differs
     */
    public static void demonstrateOverloadedMethods() {
        System.out.println("\n=== OVERLOADED METHODS ===");

        // Same method name, different parameters
        System.out.println("Area of square: " + calculateArea(5));
        System.out.println("Area of rectangle: " + calculateArea(4, 6));
        System.out.println("Area of circle: " + calculateArea(3.0));

        /* TypeScript comparison:
         * In TS, you declare multiple signatures but ONE implementation
         * In Java, you write SEPARATE implementations for each signature
         *
         * TypeScript:
         * function calculate(x: number): number;
         * function calculate(x: number, y: number): number;
         * function calculate(x: number, y?: number): number { ... }
         *
         * Java: (see below - separate methods)
         */

        /* REAL-WORLD USE:
         * - Constructor variations (different ways to create objects)
         * - Flexible APIs (like Angular service methods)
         * - Utility functions with different parameter combinations
         * Frequency: Very common (daily)
         */
    }

    // Overloaded calculateArea methods
    public static int calculateArea(int side) {
        // Square
        return side * side;
    }

    public static int calculateArea(int length, int width) {
        // Rectangle
        return length * width;
    }

    public static double calculateArea(double radius) {
        // Circle
        return Math.PI * radius * radius;
    }

    /**
     * VARARGS (#34)
     * Like TypeScript rest parameters (...args)
     */
    public static void demonstrateVarargs() {
        System.out.println("\n=== VARARGS ===");

        // Variable number of arguments
        System.out.println("Sum of 2 numbers: " + sum(5, 3));
        System.out.println("Sum of 4 numbers: " + sum(1, 2, 3, 4));
        System.out.println("Sum of 6 numbers: " + sum(10, 20, 30, 40, 50, 60));

        // Works with any type
        printItems("Apple", "Banana", "Cherry");
        printItems("Red", "Green", "Blue", "Yellow");

        /* TypeScript comparison:
         * TypeScript: function sum(...numbers: number[]): number
         * Java:       public static int sum(int... numbers)
         *
         * Both use similar syntax (... for varargs/rest parameters)
         */

        /* REAL-WORLD USE:
         * - Logging utilities (multiple arguments)
         * - Flexible APIs
         * - Builder patterns
         * Frequency: Moderate (few times per week)
         */
    }

    // Varargs method - accepts variable number of integers
    public static int sum(int... numbers) {
        int total = 0;
        for (int num : numbers) {
            total += num;
        }
        return total;
    }

    // Varargs with generic type
    public static void printItems(String... items) {
        System.out.print("Items: ");
        for (String item : items) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    /**
     * VARIABLE SCOPE (#28)
     * Same concept as TypeScript (block scope, function scope)
     */
    public static void demonstrateVariableScope() {
        System.out.println("\n=== VARIABLE SCOPE ===");

        // Method/function scope
        int methodLevel = 100;
        System.out.println("Method level variable: " + methodLevel);

        // Block scope (if statement)
        if (true) {
            int blockLevel = 200;
            System.out.println("Block level variable: " + blockLevel);
            System.out.println("Can access method level: " + methodLevel);
        }
        // System.out.println(blockLevel); // ERROR: blockLevel not accessible here

        // Loop scope
        for (int i = 0; i < 3; i++) {
            int loopLevel = i * 10;
            System.out.println("Loop iteration " + i + ", loopLevel: " + loopLevel);
        }
        // System.out.println(i); // ERROR: i not accessible here
        // System.out.println(loopLevel); // ERROR: loopLevel not accessible here

        /* TypeScript comparison:
         * Both use block scope (introduced in TS with let/const)
         * Java doesn't have var/let/const - just type declaration
         *
         * TypeScript: let x = 5; const y = 10;
         * Java:       int x = 5; final int y = 10; // final is like const
         */

        /* REAL-WORLD USE:
         * - Preventing variable conflicts
         * - Managing memory efficiently
         * - Encapsulation
         * Frequency: Understanding this is critical (affects daily coding)
         */
    }

    /**
     * Run all demonstrations
     */
    public static void runAllDemos() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   LOOPS & METHODS DEMONSTRATIONS       ║");
        System.out.println("╚════════════════════════════════════════╝");

        demonstrateWhileLoops();
        demonstrateForLoops();
        demonstrateBreakContinue();
        demonstrateNestedLoops();
        demonstrateMethods();
        demonstrateOverloadedMethods();
        demonstrateVarargs();
        demonstrateVariableScope();

        System.out.println("\n✓ All loops & methods demonstrations completed!");
    }

    public static void main(String[] args) {
        runAllDemos();
    }
}
