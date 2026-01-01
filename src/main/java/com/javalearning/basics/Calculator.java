package com.javalearning.basics;

import java.util.Scanner;

/**
 * CALCULATOR PROGRAM (#19)
 *
 * This practical application combines:
 * - User input
 * - Methods
 * - Switch statements
 * - Math operations
 * - While loops
 * - Error handling basics
 *
 * REAL-WORLD USE CASE:
 * Demonstrates how to build a simple interactive console application
 * Similar to building a basic Angular component with user interaction
 * Frequency: Concepts used here are applied daily
 *
 * TYPESCRIPT COMPARISON:
 * In Angular, you'd use:
 * - Forms for user input (ngModel, FormControl)
 * - Event handlers for button clicks
 * - Service methods for calculations
 *
 * In Java console:
 * - Scanner for user input
 * - While loop for continuous operation
 * - Methods for calculations
 */
public class Calculator {

    /**
     * Main calculator method with continuous operation
     */
    public static void runCalculator() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║        JAVA CALCULATOR v1.0            ║");
        System.out.println("╚════════════════════════════════════════╝");

        while (running) {
            try {
                System.out.println("\n=== CALCULATOR MENU ===");
                System.out.println("1. Addition (+)");
                System.out.println("2. Subtraction (-)");
                System.out.println("3. Multiplication (×)");
                System.out.println("4. Division (÷)");
                System.out.println("5. Power (x^y)");
                System.out.println("6. Square Root (√)");
                System.out.println("7. Percentage (%)");
                System.out.println("0. Exit");
                System.out.print("\nChoose operation: ");

                int choice = scanner.nextInt();

                if (choice == 0) {
                    System.out.println("Thank you for using Calculator!");
                    running = false;
                    continue;
                }

                // Perform operation based on choice
                performOperation(choice, scanner);

            } catch (Exception e) {
                System.out.println("❌ Invalid input! Please enter a number.");
                scanner.nextLine(); // Clear buffer
            }
        }
    }

    /**
     * Perform calculation based on user choice
     */
    public static void performOperation(int choice, Scanner scanner) {
        double num1, num2, result;

        switch (choice) {
            case 1: // Addition
                System.out.print("Enter first number: ");
                num1 = scanner.nextDouble();
                System.out.print("Enter second number: ");
                num2 = scanner.nextDouble();
                result = add(num1, num2);
                System.out.println("✓ Result: " + num1 + " + " + num2 + " = " + result);
                break;

            case 2: // Subtraction
                System.out.print("Enter first number: ");
                num1 = scanner.nextDouble();
                System.out.print("Enter second number: ");
                num2 = scanner.nextDouble();
                result = subtract(num1, num2);
                System.out.println("✓ Result: " + num1 + " - " + num2 + " = " + result);
                break;

            case 3: // Multiplication
                System.out.print("Enter first number: ");
                num1 = scanner.nextDouble();
                System.out.print("Enter second number: ");
                num2 = scanner.nextDouble();
                result = multiply(num1, num2);
                System.out.println("✓ Result: " + num1 + " × " + num2 + " = " + result);
                break;

            case 4: // Division
                System.out.print("Enter first number: ");
                num1 = scanner.nextDouble();
                System.out.print("Enter second number: ");
                num2 = scanner.nextDouble();

                if (num2 == 0) {
                    System.out.println("❌ Error: Cannot divide by zero!");
                } else {
                    result = divide(num1, num2);
                    System.out.println("✓ Result: " + num1 + " ÷ " + num2 + " = " + result);
                }
                break;

            case 5: // Power
                System.out.print("Enter base: ");
                num1 = scanner.nextDouble();
                System.out.print("Enter exponent: ");
                num2 = scanner.nextDouble();
                result = power(num1, num2);
                System.out.println("✓ Result: " + num1 + "^" + num2 + " = " + result);
                break;

            case 6: // Square Root
                System.out.print("Enter number: ");
                num1 = scanner.nextDouble();

                if (num1 < 0) {
                    System.out.println("❌ Error: Cannot calculate square root of negative number!");
                } else {
                    result = squareRoot(num1);
                    System.out.println("✓ Result: √" + num1 + " = " + result);
                }
                break;

            case 7: // Percentage
                System.out.print("Enter number: ");
                num1 = scanner.nextDouble();
                System.out.print("Enter percentage: ");
                num2 = scanner.nextDouble();
                result = percentage(num1, num2);
                System.out.println("✓ Result: " + num2 + "% of " + num1 + " = " + result);
                break;

            default:
                System.out.println("❌ Invalid choice! Please select 0-7.");
                break;
        }
    }

    // ============================================
    // CALCULATION METHODS
    // ============================================

    /**
     * Addition
     */
    public static double add(double a, double b) {
        return a + b;
    }

    /**
     * Subtraction
     */
    public static double subtract(double a, double b) {
        return a - b;
    }

    /**
     * Multiplication
     */
    public static double multiply(double a, double b) {
        return a * b;
    }

    /**
     * Division
     */
    public static double divide(double a, double b) {
        return a / b;
    }

    /**
     * Power (exponentiation)
     */
    public static double power(double base, double exponent) {
        return Math.pow(base, exponent);
    }

    /**
     * Square root
     */
    public static double squareRoot(double num) {
        return Math.sqrt(num);
    }

    /**
     * Percentage calculation
     */
    public static double percentage(double number, double percent) {
        return (number * percent) / 100;
    }

    /**
     * Demonstration mode (non-interactive)
     */
    public static void runDemonstration() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║    CALCULATOR DEMONSTRATION            ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println("\n=== Basic Operations ===");
        System.out.println("10 + 5 = " + add(10, 5));
        System.out.println("10 - 5 = " + subtract(10, 5));
        System.out.println("10 × 5 = " + multiply(10, 5));
        System.out.println("10 ÷ 5 = " + divide(10, 5));

        System.out.println("\n=== Advanced Operations ===");
        System.out.println("2^8 = " + power(2, 8));
        System.out.println("√64 = " + squareRoot(64));
        System.out.println("20% of 150 = " + percentage(150, 20));

        System.out.println("\n=== Real-World Examples ===");

        // Calculate shopping discount
        double originalPrice = 99.99;
        double discountPercent = 25;
        double discountAmount = percentage(originalPrice, discountPercent);
        double finalPrice = subtract(originalPrice, discountAmount);

        System.out.println("Shopping Cart Calculation:");
        System.out.println("  Original price: $" + originalPrice);
        System.out.println("  Discount: " + discountPercent + "%");
        System.out.println("  Discount amount: $" + discountAmount);
        System.out.println("  Final price: $" + finalPrice);

        // Calculate compound interest
        double principal = 1000;
        double rate = 1.05; // 5% interest (1 + 0.05)
        int years = 5;
        double finalAmount = principal * power(rate, years);

        System.out.println("\nCompound Interest Calculation:");
        System.out.println("  Principal: $" + principal);
        System.out.println("  Rate: 5% per year");
        System.out.println("  Time: " + years + " years");
        System.out.println("  Final amount: $" + String.format("%.2f", finalAmount));

        /* REAL-WORLD APPLICATIONS:
         * - E-commerce: price calculations, discounts, taxes
         * - Finance: interest calculations, loan payments
         * - Gaming: damage calculations, score multipliers
         * - Analytics: statistical calculations
         * - Engineering: physics simulations
         */

        System.out.println("\n✓ Calculator demonstration completed!");
    }

    /**
     * Main method
     * Uncomment runCalculator() for interactive mode
     */
    public static void main(String[] args) {
        // Run demonstration (non-interactive)
        runDemonstration();

        // For interactive calculator, uncomment this line:
        // runCalculator();
    }
}
