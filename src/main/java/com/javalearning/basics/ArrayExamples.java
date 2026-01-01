package com.javalearning.basics;

import java.util.Arrays;
import java.util.Scanner;

/**
 * ARRAY EXAMPLES
 *
 * Topics covered:
 * - Arrays (#31)
 * - Enter user input into an array (#32)
 * - Search an array (#33)
 * - 2D arrays (#35)
 *
 * REAL-WORLD USE CASE:
 * Arrays store multiple values of the same type
 * Used for: lists, tables, game boards, image data, etc.
 * Frequency: 100% - Arrays and collections are fundamental
 *
 * TYPESCRIPT COMPARISON:
 * Java arrays are FIXED SIZE (unlike TypeScript arrays)
 * Java: int[] arr = new int[5]; // Fixed at 5 elements
 * TypeScript: let arr: number[] = []; // Dynamic size
 * For dynamic arrays in Java, use ArrayList (covered in OOP section)
 */
public class ArrayExamples {

    /**
     * ARRAYS (#31)
     * Fixed-size collections (different from TypeScript dynamic arrays)
     */
    public static void demonstrateArrays() {
        System.out.println("\n=== ARRAYS ===");

        // Declaration and initialization - Method 1
        int[] numbers = {1, 2, 3, 4, 5};
        System.out.println("Array created with values: " + Arrays.toString(numbers));

        // Declaration and initialization - Method 2
        String[] fruits = new String[3]; // Create array of size 3
        fruits[0] = "Apple";
        fruits[1] = "Banana";
        fruits[2] = "Cherry";
        System.out.println("Fruits array: " + Arrays.toString(fruits));

        // Accessing elements (same as TypeScript)
        System.out.println("First fruit: " + fruits[0]);
        System.out.println("Last fruit: " + fruits[fruits.length - 1]);

        // Array length (same as TypeScript)
        System.out.println("Array length: " + fruits.length);

        // Iterating over arrays
        System.out.println("\nIterating with for loop:");
        for (int i = 0; i < numbers.length; i++) {
            System.out.print(numbers[i] + " ");
        }

        System.out.println("\n\nIterating with for-each:");
        for (String fruit : fruits) {
            System.out.print(fruit + " ");
        }
        System.out.println();

        /* TypeScript comparison:
         * TypeScript arrays are dynamic:
         * let arr = [1, 2, 3];
         * arr.push(4); // Can grow
         *
         * Java arrays are fixed:
         * int[] arr = new int[3]; // Size is fixed
         * arr[3] = 4; // ERROR: ArrayIndexOutOfBoundsException
         *
         * For dynamic arrays in Java, use ArrayList
         */

        /* REAL-WORLD USE:
         * - Storing form field values
         * - Processing batch data
         * - Table rows/columns
         * - Game board states
         * Frequency: Very common (daily, but ArrayList more common)
         */
    }

    /**
     * ENTER USER INPUT INTO AN ARRAY (#32)
     * Similar to capturing form data in Angular
     */
    public static void demonstrateUserInputArray() {
        System.out.println("\n=== USER INPUT INTO ARRAY ===");

        // Simulated user input (in real app, you'd use Scanner)
        System.out.println("Simulating user entering 5 student grades:");

        int[] grades = new int[5];
        int[] simulatedInput = {85, 92, 78, 88, 95};

        for (int i = 0; i < grades.length; i++) {
            grades[i] = simulatedInput[i];
            System.out.println("Student " + (i + 1) + " grade: " + grades[i]);
        }

        // Calculate average
        int sum = 0;
        for (int grade : grades) {
            sum += grade;
        }
        double average = (double) sum / grades.length;
        System.out.println("\nClass average: " + average);

        /* Real implementation with Scanner (commented):
        Scanner scanner = new Scanner(System.in);
        int[] grades = new int[5];

        for (int i = 0; i < grades.length; i++) {
            System.out.print("Enter grade " + (i + 1) + ": ");
            grades[i] = scanner.nextInt();
        }
        */

        /* REAL-WORLD USE:
         * - Form submissions (Angular FormArray)
         * - Batch data entry
         * - Survey responses
         * - Quiz answers
         * Frequency: Common (weekly)
         */
    }

    /**
     * SEARCH AN ARRAY (#33)
     * Linear and binary search
     */
    public static void demonstrateArraySearch() {
        System.out.println("\n=== SEARCH AN ARRAY ===");

        String[] users = {"Alice", "Bob", "Charlie", "David", "Eve"};

        // Linear search (works on unsorted arrays)
        String searchName = "Charlie";
        int index = linearSearch(users, searchName);

        if (index != -1) {
            System.out.println("Found '" + searchName + "' at index: " + index);
        } else {
            System.out.println("'" + searchName + "' not found");
        }

        // Binary search (requires sorted array)
        int[] sortedNumbers = {10, 20, 30, 40, 50, 60, 70, 80, 90};
        int searchNumber = 50;
        int resultIndex = Arrays.binarySearch(sortedNumbers, searchNumber);

        System.out.println("\nBinary search for " + searchNumber + ": index " + resultIndex);

        /* TypeScript comparison:
         * TypeScript: array.indexOf(item) or array.find(item => ...)
         * Java: Manual loop or Arrays.binarySearch() for sorted arrays
         *
         * TypeScript has built-in array methods
         * Java requires manual implementation or utility classes
         */

        /* REAL-WORLD USE:
         * - Finding users by ID/email
         * - Searching products in inventory
         * - Autocomplete features
         * - Data lookup operations
         * Frequency: Very common (daily)
         */
    }

    /**
     * Linear search implementation
     */
    public static int linearSearch(String[] array, String target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(target)) {
                return i;
            }
        }
        return -1; // Not found
    }

    /**
     * 2D ARRAYS (#35)
     * Like matrices or tables (similar to TypeScript nested arrays)
     */
    public static void demonstrate2DArrays() {
        System.out.println("\n=== 2D ARRAYS ===");

        // Creating 2D array (like a table or grid)
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };

        System.out.println("3x3 Matrix:");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%3d", matrix[i][j]);
            }
            System.out.println();
        }

        // Practical example: Seating chart
        String[][] seatingChart = new String[3][4]; // 3 rows, 4 seats each

        // Initialize with seat numbers
        for (int row = 0; row < seatingChart.length; row++) {
            for (int seat = 0; seat < seatingChart[row].length; seat++) {
                seatingChart[row][seat] = "Empty";
            }
        }

        // Assign some seats
        seatingChart[0][0] = "Alice";
        seatingChart[1][2] = "Bob";
        seatingChart[2][3] = "Charlie";

        System.out.println("\nSeating Chart:");
        for (int row = 0; row < seatingChart.length; row++) {
            System.out.print("Row " + (row + 1) + ": ");
            for (int seat = 0; seat < seatingChart[row].length; seat++) {
                System.out.printf("%-10s", seatingChart[row][seat]);
            }
            System.out.println();
        }

        /* TypeScript comparison:
         * TypeScript: let matrix: number[][] = [[1,2], [3,4]];
         * Java:       int[][] matrix = {{1,2}, {3,4}};
         *
         * Very similar syntax!
         */

        /* REAL-WORLD USE:
         * - Spreadsheet data (Excel-like)
         * - Game boards (chess, tic-tac-toe)
         * - Image pixels (2D grid of colors)
         * - Seating arrangements
         * - Calendar layouts
         * Frequency: Common (few times per week)
         */
    }

    /**
     * PRACTICAL EXAMPLE: Tic-Tac-Toe Board
     */
    public static void demonstrateTicTacToe() {
        System.out.println("\n=== PRACTICAL EXAMPLE: Tic-Tac-Toe ===");

        char[][] board = {
            {'X', 'O', 'X'},
            {'O', 'X', 'O'},
            {'O', 'X', 'X'}
        };

        System.out.println("Game Board:");
        printBoard(board);

        // Check winner (simple check for first row)
        if (board[0][0] == board[0][1] && board[0][1] == board[0][2]) {
            System.out.println("\nWinner: " + board[0][0]);
        }
    }

    public static void printBoard(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(" " + board[i][j]);
                if (j < board[i].length - 1) {
                    System.out.print(" |");
                }
            }
            System.out.println();
            if (i < board.length - 1) {
                System.out.println("---+---+---");
            }
        }
    }

    /**
     * ARRAY UTILITY METHODS
     * Java's Arrays class provides many useful methods
     */
    public static void demonstrateArrayUtilities() {
        System.out.println("\n=== ARRAY UTILITY METHODS ===");

        int[] numbers = {5, 2, 8, 1, 9, 3};

        // Original array
        System.out.println("Original: " + Arrays.toString(numbers));

        // Sort (in-place)
        Arrays.sort(numbers);
        System.out.println("Sorted: " + Arrays.toString(numbers));

        // Fill array with value
        int[] filled = new int[5];
        Arrays.fill(filled, 7);
        System.out.println("Filled with 7: " + Arrays.toString(filled));

        // Copy array
        int[] copy = Arrays.copyOf(numbers, numbers.length);
        System.out.println("Copy: " + Arrays.toString(copy));

        // Compare arrays
        boolean areEqual = Arrays.equals(numbers, copy);
        System.out.println("Are arrays equal? " + areEqual);

        /* TypeScript comparison:
         * TypeScript: arr.sort(), arr.fill(), [...arr] (spread)
         * Java:       Arrays.sort(), Arrays.fill(), Arrays.copyOf()
         *
         * TypeScript has more built-in array methods (map, filter, reduce)
         * Java uses Streams API for similar functionality (covered later)
         */
    }

    /**
     * Run all demonstrations
     */
    public static void runAllDemos() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║      ARRAY DEMONSTRATIONS              ║");
        System.out.println("╚════════════════════════════════════════╝");

        demonstrateArrays();
        demonstrateUserInputArray();
        demonstrateArraySearch();
        demonstrate2DArrays();
        demonstrateTicTacToe();
        demonstrateArrayUtilities();

        System.out.println("\n✓ All array demonstrations completed!");
    }

    public static void main(String[] args) {
        runAllDemos();
    }
}
