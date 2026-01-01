package com.javalearning.basics;

import java.util.Scanner;

/**
 * CONTROL FLOW EXAMPLES
 *
 * Topics covered:
 * - If statements (#7)
 * - Nested if statements (#12)
 * - Ternary operator (#16)
 * - Logical operators (#20)
 * - Enhanced switches (#18)
 *
 * REAL-WORLD USE CASE:
 * Control flow is used in EVERY application for decision making.
 * Frequency: 100% - You'll use these daily in Java development.
 *
 * TYPESCRIPT COMPARISON:
 * Java and TypeScript have very similar control flow, but Java is stricter with types.
 * In TS: if (value) works with truthy/falsy values
 * In Java: if statements MUST evaluate to boolean (true/false)
 */
public class ControlFlow {

    /**
     * IF STATEMENTS (#7)
     * Similar to TypeScript, but stricter type checking
     */
    public static void demonstrateIfStatements() {
        System.out.println("\n=== IF STATEMENTS ===");

        int age = 25;

        // Simple if
        if (age >= 18) {
            System.out.println("You are an adult");
        }

        // if-else
        if (age >= 65) {
            System.out.println("Senior citizen");
        } else if (age >= 18) {
            System.out.println("Adult"); // This will execute
        } else {
            System.out.println("Minor");
        }

        /* TypeScript comparison:
         * TypeScript: if (user) { } // Works with null/undefined
         * Java: if (user != null) { } // Must be explicit boolean
         */
    }

    /**
     * NESTED IF STATEMENTS (#12)
     * Common in validation logic - similar to Angular form validation
     */
    public static void demonstrateNestedIf() {
        System.out.println("\n=== NESTED IF STATEMENTS ===");

        String username = "john_doe";
        String password = "secure123";
        boolean isEmailVerified = true;

        // Nested if - similar to Angular authentication guards
        if (username != null && !username.isEmpty()) {
            if (password != null && password.length() >= 8) {
                if (isEmailVerified) {
                    System.out.println("✓ Login successful!");
                    System.out.println("Welcome, " + username);
                } else {
                    System.out.println("✗ Please verify your email first");
                }
            } else {
                System.out.println("✗ Password must be at least 8 characters");
            }
        } else {
            System.out.println("✗ Username is required");
        }

        /* REAL-WORLD USE:
         * - Form validation (like Angular reactive forms)
         * - Permission checks (role-based access control)
         * - Multi-step workflow validation
         */
    }

    /**
     * TERNARY OPERATOR (#16)
     * Identical to TypeScript - great for conditional assignments
     */
    public static void demonstrateTernaryOperator() {
        System.out.println("\n=== TERNARY OPERATOR ===");

        int score = 85;

        // Basic ternary (same as TypeScript)
        String result = (score >= 60) ? "Pass" : "Fail";
        System.out.println("Result: " + result);

        // Nested ternary (use sparingly, like in TypeScript)
        String grade = (score >= 90) ? "A" :
                      (score >= 80) ? "B" :
                      (score >= 70) ? "C" :
                      (score >= 60) ? "D" : "F";
        System.out.println("Grade: " + grade);

        // Common use in Angular templates: {{ user ? user.name : 'Guest' }}
        String userName = null;
        String displayName = (userName != null) ? userName : "Guest";
        System.out.println("Display name: " + displayName);

        /* REAL-WORLD USE:
         * - Default values (like Angular pipes: user?.name || 'Unknown')
         * - Quick status checks
         * - Conditional rendering logic
         * Frequency: Very common (daily use)
         */
    }

    /**
     * LOGICAL OPERATORS (#20)
     * Same as TypeScript: && (AND), || (OR), ! (NOT)
     */
    public static void demonstrateLogicalOperators() {
        System.out.println("\n=== LOGICAL OPERATORS ===");

        boolean isLoggedIn = true;
        boolean hasPermission = true;
        boolean isAdmin = false;

        // AND operator (&&) - both must be true
        if (isLoggedIn && hasPermission) {
            System.out.println("✓ Access granted");
        }

        // OR operator (||) - at least one must be true
        if (isAdmin || hasPermission) {
            System.out.println("✓ Can view dashboard");
        }

        // NOT operator (!) - inverts boolean
        if (!isAdmin) {
            System.out.println("Regular user");
        }

        // Combined operators (like Angular *ngIf directives)
        boolean canEdit = isLoggedIn && (isAdmin || hasPermission);
        System.out.println("Can edit: " + canEdit);

        // Short-circuit evaluation (same as TypeScript)
        String data = null;
        if (data != null && data.length() > 0) {
            // Won't throw NullPointerException due to short-circuit
            System.out.println("Data: " + data);
        }

        /* REAL-WORLD USE:
         * - Route guards in Angular: canActivate()
         * - Complex form validation
         * - Permission systems
         * Frequency: Extremely common (multiple times per day)
         */
    }

    /**
     * ENHANCED SWITCHES (#18)
     * Java 14+ has modern switch expressions (similar to TypeScript switch)
     */
    public static void demonstrateEnhancedSwitch() {
        System.out.println("\n=== ENHANCED SWITCHES ===");

        // Traditional switch (still common in existing codebases)
        String day = "MONDAY";
        String dayType;

        switch (day) {
            case "MONDAY":
            case "TUESDAY":
            case "WEDNESDAY":
            case "THURSDAY":
            case "FRIDAY":
                dayType = "Weekday";
                break;
            case "SATURDAY":
            case "SUNDAY":
                dayType = "Weekend";
                break;
            default:
                dayType = "Invalid day";
                break;
        }
        System.out.println("Traditional switch: " + day + " is a " + dayType);

        // Enhanced switch expression (Java 14+) - more like TypeScript
        String dayType2 = switch (day) {
            case "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY" -> "Weekday";
            case "SATURDAY", "SUNDAY" -> "Weekend";
            default -> "Invalid day";
        };
        System.out.println("Enhanced switch: " + day + " is a " + dayType2);

        // Switch with yield (for complex logic)
        int month = 3;
        String season = switch (month) {
            case 12, 1, 2 -> "Winter";
            case 3, 4, 5 -> "Spring";
            case 6, 7, 8 -> "Summer";
            case 9, 10, 11 -> "Fall";
            default -> {
                System.out.println("Invalid month: " + month);
                yield "Unknown";
            }
        };
        System.out.println("Season for month " + month + ": " + season);

        /* TypeScript comparison:
         * Both languages have similar switch syntax
         * Java's enhanced switch is more concise (no break needed with ->)
         *
         * REAL-WORLD USE:
         * - State management (similar to NgRx reducers)
         * - HTTP status code handling
         * - Router logic / navigation
         * - Event handling
         * Frequency: Common (several times per week)
         */
    }

    /**
     * Demonstrates all control flow concepts
     */
    public static void runAllDemos() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║     CONTROL FLOW DEMONSTRATIONS        ║");
        System.out.println("╚════════════════════════════════════════╝");

        demonstrateIfStatements();
        demonstrateNestedIf();
        demonstrateTernaryOperator();
        demonstrateLogicalOperators();
        demonstrateEnhancedSwitch();

        System.out.println("\n✓ All control flow demonstrations completed!");
    }

    public static void main(String[] args) {
        runAllDemos();
    }
}
