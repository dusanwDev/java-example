package com.javalearning.basics;

import java.util.Random;

/**
 * MATH, RANDOM NUMBERS, AND STRINGS
 *
 * Topics covered:
 * - Random numbers (#8)
 * - Math class (#9)
 * - String methods (#13)
 * - Substrings (#14)
 *
 * REAL-WORLD USE CASE:
 * Math: Calculations, rounding, data processing
 * Random: Testing, game development, ID generation
 * Strings: Text processing, validation, parsing (99% of applications)
 * Frequency: 100% - String manipulation is used in every application
 *
 * TYPESCRIPT COMPARISON:
 * Math class is very similar between Java and TypeScript
 * String methods have MANY similarities but some differences
 * Random is different (Java has Random class, TS uses Math.random())
 */
public class MathAndStrings {

    /**
     * RANDOM NUMBERS (#8)
     * Different from TypeScript's Math.random()
     */
    public static void demonstrateRandomNumbers() {
        System.out.println("\n=== RANDOM NUMBERS ===");

        // Method 1: Using Random class (recommended)
        Random random = new Random();

        // Random integer
        int randomInt = random.nextInt(100); // 0 to 99
        System.out.println("Random int (0-99): " + randomInt);

        // Random integer in range
        int dice = random.nextInt(6) + 1; // 1 to 6
        System.out.println("Dice roll: " + dice);

        // Random double (0.0 to 1.0)
        double randomDouble = random.nextDouble();
        System.out.println("Random double: " + randomDouble);

        // Random boolean
        boolean randomBoolean = random.nextBoolean();
        System.out.println("Random boolean: " + randomBoolean);

        // Method 2: Using Math.random() (like TypeScript)
        double tsStyleRandom = Math.random(); // 0.0 to 1.0
        System.out.println("Math.random() (TS style): " + tsStyleRandom);

        int randomInRange = (int)(Math.random() * 100) + 1; // 1 to 100
        System.out.println("Random 1-100 (TS style): " + randomInRange);

        /* TypeScript comparison:
         * TypeScript: Math.random() * max
         * Java (recommended): new Random().nextInt(max)
         * Java (TS-like): (int)(Math.random() * max)
         */

        /* REAL-WORLD USE:
         * - Generating test data
         * - Game mechanics (loot drops, critical hits)
         * - Random sampling
         * - UUID/ID generation
         * - A/B testing (random user assignment)
         * Frequency: Moderate (few times per week)
         */
    }

    /**
     * MATH CLASS (#9)
     * Nearly identical to TypeScript's Math object
     */
    public static void demonstrateMathClass() {
        System.out.println("\n=== MATH CLASS ===");

        double num = 9.7;

        // Rounding methods
        System.out.println("ceil(" + num + "): " + Math.ceil(num));     // 10.0
        System.out.println("floor(" + num + "): " + Math.floor(num));   // 9.0
        System.out.println("round(" + num + "): " + Math.round(num));   // 10

        // Basic operations
        System.out.println("abs(-15): " + Math.abs(-15));                // 15
        System.out.println("max(10, 20): " + Math.max(10, 20));          // 20
        System.out.println("min(10, 20): " + Math.min(10, 20));          // 10
        System.out.println("pow(2, 3): " + Math.pow(2, 3));              // 8.0
        System.out.println("sqrt(16): " + Math.sqrt(16));                // 4.0

        // Trigonometry
        System.out.println("sin(30°): " + Math.sin(Math.toRadians(30))); // 0.5
        System.out.println("cos(60°): " + Math.cos(Math.toRadians(60))); // 0.5

        // Constants
        System.out.println("PI: " + Math.PI);                             // 3.14159...
        System.out.println("E: " + Math.E);                               // 2.71828...

        /* TypeScript comparison:
         * Almost identical! Both languages have same Math methods
         * Math.ceil(), Math.floor(), Math.round(), Math.abs(), etc.
         */

        /* REAL-WORLD USE:
         * - Financial calculations (rounding prices)
         * - Graphics/animations (trigonometry)
         * - Data analysis
         * - Physics simulations
         * - Distance calculations (Pythagorean theorem)
         * Frequency: Very common (daily in data-heavy applications)
         */
    }

    /**
     * STRING METHODS (#13)
     * Many similarities to TypeScript, some differences
     */
    public static void demonstrateStringMethods() {
        System.out.println("\n=== STRING METHODS ===");

        String text = "  Hello, Java Developer!  ";
        String email = "user@example.com";

        // Length (same as TypeScript)
        System.out.println("Length: " + text.length());

        // Trimming (like TypeScript's trim())
        System.out.println("Trimmed: '" + text.trim() + "'");

        // Case conversion (same as TypeScript)
        System.out.println("Uppercase: " + text.toUpperCase());
        System.out.println("Lowercase: " + text.toLowerCase());

        // Contains check (similar to TypeScript's includes())
        System.out.println("Contains 'Java': " + text.contains("Java"));

        // Starts/Ends with (same as TypeScript)
        System.out.println("Starts with 'Hello': " + text.trim().startsWith("Hello"));
        System.out.println("Ends with '!': " + text.trim().endsWith("!"));

        // Replace (similar to TypeScript)
        String replaced = text.replace("Java", "TypeScript");
        System.out.println("Replaced: " + replaced);

        // Split (same as TypeScript)
        String[] parts = email.split("@");
        System.out.println("Email parts: " + parts[0] + " | " + parts[1]);

        // Character at index (like TypeScript's charAt or [index])
        System.out.println("Character at index 2: " + text.charAt(2));

        // Index of (same as TypeScript)
        System.out.println("Index of 'Java': " + text.indexOf("Java"));

        // String comparison
        String s1 = "hello";
        String s2 = "hello";
        String s3 = "Hello";

        System.out.println("equals (case-sensitive): " + s1.equals(s2));         // true
        System.out.println("equals (different case): " + s1.equals(s3));         // false
        System.out.println("equalsIgnoreCase: " + s1.equalsIgnoreCase(s3));      // true

        /* TypeScript comparison:
         * Java:       string.contains("text")
         * TypeScript: string.includes("text")
         *
         * Java:       string1.equals(string2)
         * TypeScript: string1 === string2
         *
         * Java strings are IMMUTABLE (like TypeScript strings)
         */

        /* REAL-WORLD USE:
         * - Form validation (email, phone, etc.)
         * - Data parsing (CSV, JSON)
         * - Text processing
         * - URL manipulation
         * - User input sanitization
         * Frequency: Extremely common (multiple times daily)
         */
    }

    /**
     * SUBSTRINGS (#14)
     * Similar to TypeScript's substring() and slice()
     */
    public static void demonstrateSubstrings() {
        System.out.println("\n=== SUBSTRINGS ===");

        String email = "john.doe@company.com";

        // Extract substring (starting index to end)
        String domain = email.substring(9); // From index 9 to end
        System.out.println("Domain: " + domain); // company.com

        // Extract substring (range)
        String username = email.substring(0, 8); // From 0 to 8 (exclusive)
        System.out.println("Username: " + username); // john.doe

        // Extract file extension
        String filename = "document.pdf";
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        System.out.println("File extension: " + extension); // pdf

        // Practical example: Extract initials
        String fullName = "John Michael Doe";
        String[] names = fullName.split(" ");
        String initials = "";
        for (String name : names) {
            initials += name.substring(0, 1).toUpperCase();
        }
        System.out.println("Initials: " + initials); // JMD

        /* TypeScript comparison:
         * Java:       string.substring(start, end)
         * TypeScript: string.substring(start, end) OR string.slice(start, end)
         *
         * Both are 0-indexed
         * End index is exclusive in both
         */

        /* REAL-WORLD USE:
         * - Parsing structured data
         * - Email validation (extracting domain)
         * - Text truncation (previews)
         * - Data extraction from strings
         * Frequency: Very common (daily)
         */
    }

    /**
     * PRACTICAL STRING EXAMPLE
     * Email validation - common in Angular forms
     */
    public static void demonstrateEmailValidation() {
        System.out.println("\n=== PRACTICAL EXAMPLE: Email Validation ===");

        String email = "user@example.com";

        boolean isValid = validateEmail(email);
        System.out.println("Is '" + email + "' valid? " + isValid);

        // Test various emails
        String[] testEmails = {
            "valid@email.com",
            "invalid.email",
            "@nodomain.com",
            "no@.com",
            "good@domain.co.uk"
        };

        for (String testEmail : testEmails) {
            System.out.println(testEmail + " -> " + validateEmail(testEmail));
        }
    }

    public static boolean validateEmail(String email) {
        // Basic validation (similar to Angular Validators.email)
        if (email == null || email.isEmpty()) {
            return false;
        }

        if (!email.contains("@")) {
            return false;
        }

        String[] parts = email.split("@");
        if (parts.length != 2) {
            return false;
        }

        String local = parts[0];
        String domain = parts[1];

        if (local.isEmpty() || domain.isEmpty()) {
            return false;
        }

        if (!domain.contains(".")) {
            return false;
        }

        return true;
    }

    /**
     * Run all demonstrations
     */
    public static void runAllDemos() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   MATH & STRINGS DEMONSTRATIONS        ║");
        System.out.println("╚════════════════════════════════════════╝");

        demonstrateRandomNumbers();
        demonstrateMathClass();
        demonstrateStringMethods();
        demonstrateSubstrings();
        demonstrateEmailValidation();

        System.out.println("\n✓ All math & strings demonstrations completed!");
    }

    public static void main(String[] args) {
        runAllDemos();
    }
}
