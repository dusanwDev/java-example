package com.javalearning;

import com.javalearning.basics.*;
import com.javalearning.taskmanagement.TaskManagementApp;
import com.javalearning.banking.BankingApp;
import com.javalearning.ecommerce.EcommerceApp;
import com.javalearning.threading.ThreadingApp;
import com.javalearning.jpa.demos.*;

import java.util.Scanner;

/**
 * MAIN APPLICATION - Entry Point
 *
 * Interactive menu to run all Java learning examples
 * Covers all topics from basic to advanced
 *
 * FOR TYPESCRIPT/ANGULAR DEVELOPERS:
 * This is like the main.ts file in an Angular application
 * or index.ts in a Node.js application
 */
public class MainApp {

    /**
     * Display main menu
     */
    public static void displayMenu() {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║     JAVA LEARNING EXAMPLES - FOR TS DEVELOPERS        ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println("\n📚 BASICS:");
        System.out.println("  1. Control Flow (if, switch, ternary, logical ops)");
        System.out.println("  2. Loops & Methods (for, while, methods, varargs)");
        System.out.println("  3. Math & Strings (random, math, string methods)");
        System.out.println("  4. Arrays (1D, 2D, search)");
        System.out.println("  5. Calculator Program (practical example)");

        System.out.println("\n🏢 REAL-WORLD APPLICATIONS:");
        System.out.println("  6. Task Management System (OOP, file I/O, exceptions)");
        System.out.println("  7. Banking System (inheritance, polymorphism, interfaces)");
        System.out.println("  8. E-commerce Inventory (generics, hashmaps, enums)");
        System.out.println("  9. Threading & Concurrency (threads, timers)");

        System.out.println("\n💾 JPA/HIBERNATE (PERSISTENCE):");
        System.out.println("  12. JPA Relationships Demo (One-to-One, Many-to-Many, etc.)");
        System.out.println("  13. Fetch Strategies (EAGER vs LAZY, N+1 problem)");
        System.out.println("  14. CRUD Operations (Create, Read, Update, Delete)");
        System.out.println("  15. HQL Examples (Queries, Joins, Aggregations)");
        System.out.println("  16. Caching Demo (L1, L2, Query cache)");
        System.out.println("  17. Entity Lifecycle (States and transitions)");

        System.out.println("\n🎯 ALL EXAMPLES:");
        System.out.println("  18. Run ALL Basic Examples");
        System.out.println("  19. Run ALL Applications");
        System.out.println("  20. Run ALL JPA/Hibernate Demos");

        System.out.println("\n  0. Exit");
        System.out.println("\n" + "─".repeat(56));
        System.out.print("Choose an option (0-20): ");
    }

    /**
     * Run all basic examples
     */
    public static void runAllBasics() {
        System.out.println("\n\n" + "═".repeat(60));
        System.out.println("RUNNING ALL BASIC EXAMPLES");
        System.out.println("═".repeat(60));

        ControlFlow.runAllDemos();
        LoopsAndMethods.runAllDemos();
        MathAndStrings.runAllDemos();
        ArrayExamples.runAllDemos();
        Calculator.runDemonstration();

        System.out.println("\n\n✅ ALL BASIC EXAMPLES COMPLETED!");
        System.out.println("═".repeat(60));
    }

    /**
     * Run all applications
     */
    public static void runAllApplications() {
        System.out.println("\n\n" + "═".repeat(60));
        System.out.println("RUNNING ALL REAL-WORLD APPLICATIONS");
        System.out.println("═".repeat(60));

        TaskManagementApp.runDemo();
        BankingApp.main(null);
        EcommerceApp.main(null);
        ThreadingApp.main(null);

        System.out.println("\n\n✅ ALL APPLICATIONS COMPLETED!");
        System.out.println("═".repeat(60));
    }

    /**
     * Run all JPA/Hibernate demos
     */
    public static void runAllJPADemos() {
        System.out.println("\n\n" + "═".repeat(60));
        System.out.println("RUNNING ALL JPA/HIBERNATE DEMONSTRATIONS");
        System.out.println("═".repeat(60));

        RelationshipsDemo.runAllDemos();
        FetchStrategiesDemo.runAllDemos();
        CrudOperationsDemo.runAllDemos();
        HqlExamplesDemo.runAllDemos();
        CachingDemo.runAllDemos();
        EntityLifecycleDemo.runAllDemos();

        System.out.println("\n\n✅ ALL JPA/HIBERNATE DEMOS COMPLETED!");
        System.out.println("═".repeat(60));
    }

    /**
     * Display topic coverage summary
     */
    public static void displayTopicSummary() {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║              TOPICS COVERED                            ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");

        System.out.println("\n📌 FUNDAMENTALS:");
        System.out.println("  ✓ If statements (#7)");
        System.out.println("  ✓ Random numbers (#8)");
        System.out.println("  ✓ Math class (#9)");
        System.out.println("  ✓ Nested if statements (#12)");
        System.out.println("  ✓ String methods (#13)");
        System.out.println("  ✓ Substrings (#14)");
        System.out.println("  ✓ Ternary operator (#16)");
        System.out.println("  ✓ Enhanced switches (#18)");
        System.out.println("  ✓ Calculator program (#19)");
        System.out.println("  ✓ Logical operators (#20)");
        System.out.println("  ✓ While loops (#21)");
        System.out.println("  ✓ For loops (#23)");
        System.out.println("  ✓ Break & continue (#24)");
        System.out.println("  ✓ Nested loops (#25)");
        System.out.println("  ✓ Methods (#26)");
        System.out.println("  ✓ Overloaded methods (#27)");
        System.out.println("  ✓ Variable scope (#28)");
        System.out.println("  ✓ Arrays (#31)");
        System.out.println("  ✓ User input into array (#32)");
        System.out.println("  ✓ Search an array (#33)");
        System.out.println("  ✓ Varargs (#34)");
        System.out.println("  ✓ 2D arrays (#35)");

        System.out.println("\n🏗️ OBJECT-ORIENTED PROGRAMMING:");
        System.out.println("  ✓ OOP basics (#39)");
        System.out.println("  ✓ Constructors (#40)");
        System.out.println("  ✓ Overloaded constructors (#41)");
        System.out.println("  ✓ Array of objects (#42)");
        System.out.println("  ✓ Static keyword (#43)");
        System.out.println("  ✓ Inheritance (#44)");
        System.out.println("  ✓ Super keyword (#45)");
        System.out.println("  ✓ Method overriding (#46)");
        System.out.println("  ✓ toString method (#47)");
        System.out.println("  ✓ Abstraction (#48)");
        System.out.println("  ✓ Interfaces (#49)");
        System.out.println("  ✓ Polymorphism (#50)");
        System.out.println("  ✓ Runtime polymorphism (#51)");
        System.out.println("  ✓ Getters and setters (#52)");
        System.out.println("  ✓ Aggregation (#53)");
        System.out.println("  ✓ Composition (#54)");

        System.out.println("\n📦 COLLECTIONS & ADVANCED:");
        System.out.println("  ✓ Wrapper classes (#55)");
        System.out.println("  ✓ ArrayLists (#56)");
        System.out.println("  ✓ Exception handling (#57)");
        System.out.println("  ✓ Write files (#58)");
        System.out.println("  ✓ Read files (#59)");
        System.out.println("  ✓ Dates & times (#62)");
        System.out.println("  ✓ Anonymous classes (#63)");
        System.out.println("  ✓ TimerTasks (#64)");
        System.out.println("  ✓ Generics (#66)");
        System.out.println("  ✓ HashMaps (#67)");
        System.out.println("  ✓ Enums (#68)");
        System.out.println("  ✓ Threading (#69)");
        System.out.println("  ✓ Multithreading (#70)");

        System.out.println("\n💾 JPA/HIBERNATE (PERSISTENCE):");
        System.out.println("  ✓ Entity mapping (JPA annotations)");
        System.out.println("  ✓ Relationships (One-to-One, One-to-Many, Many-to-Many)");
        System.out.println("  ✓ Fetch strategies (EAGER vs LAZY)");
        System.out.println("  ✓ CRUD operations (persist, find, merge, remove)");
        System.out.println("  ✓ HQL queries (SELECT, WHERE, JOIN, aggregations)");
        System.out.println("  ✓ Caching (First-level, Second-level, Query cache)");
        System.out.println("  ✓ Entity lifecycle (Transient, Persistent, Detached, Removed)");
        System.out.println("  ✓ get() vs load() vs find()");
        System.out.println("  ✓ 35+ Hibernate-specific annotations");

        System.out.println("\n📊 TOTAL: 60+ Java concepts covered!");
        System.out.println("All examples include TypeScript/Angular comparisons!");
    }

    /**
     * Main method - Program entry point
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // Display topic summary at startup
        displayTopicSummary();

        while (running) {
            displayMenu();

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                System.out.println(); // Blank line for readability

                switch (choice) {
                    case 0:
                        System.out.println("👋 Thank you for learning Java!");
                        System.out.println("Keep coding! 🚀");
                        running = false;
                        break;

                    case 1:
                        System.out.println("🎯 Running Control Flow examples...");
                        ControlFlow.runAllDemos();
                        break;

                    case 2:
                        System.out.println("🔄 Running Loops & Methods examples...");
                        LoopsAndMethods.runAllDemos();
                        break;

                    case 3:
                        System.out.println("🔢 Running Math & Strings examples...");
                        MathAndStrings.runAllDemos();
                        break;

                    case 4:
                        System.out.println("📊 Running Array examples...");
                        ArrayExamples.runAllDemos();
                        break;

                    case 5:
                        System.out.println("🖩 Running Calculator program...");
                        Calculator.runDemonstration();
                        break;

                    case 6:
                        System.out.println("📝 Running Task Management System...");
                        TaskManagementApp.runDemo();
                        break;

                    case 7:
                        System.out.println("🏦 Running Banking System...");
                        BankingApp.main(null);
                        break;

                    case 8:
                        System.out.println("🛒 Running E-commerce Inventory...");
                        EcommerceApp.main(null);
                        break;

                    case 9:
                        System.out.println("🧵 Running Threading examples...");
                        ThreadingApp.main(null);
                        break;

                    case 12:
                        System.out.println("💾 Running JPA Relationships Demo...");
                        RelationshipsDemo.runAllDemos();
                        break;

                    case 13:
                        System.out.println("⚡ Running Fetch Strategies Demo...");
                        FetchStrategiesDemo.runAllDemos();
                        break;

                    case 14:
                        System.out.println("📝 Running CRUD Operations Demo...");
                        CrudOperationsDemo.runAllDemos();
                        break;

                    case 15:
                        System.out.println("🔍 Running HQL Examples Demo...");
                        HqlExamplesDemo.runAllDemos();
                        break;

                    case 16:
                        System.out.println("🗃️ Running Caching Demo...");
                        CachingDemo.runAllDemos();
                        break;

                    case 17:
                        System.out.println("🔄 Running Entity Lifecycle Demo...");
                        EntityLifecycleDemo.runAllDemos();
                        break;

                    case 18:
                        runAllBasics();
                        break;

                    case 19:
                        runAllApplications();
                        break;

                    case 20:
                        runAllJPADemos();
                        break;

                    default:
                        System.out.println("❌ Invalid choice. Please select 0-20.");
                        break;
                }

                if (running && choice != 0) {
                    System.out.println("\n\nPress Enter to continue...");
                    scanner.nextLine();
                }

            } catch (Exception e) {
                System.out.println("❌ Invalid input. Please enter a number (0-20).");
                scanner.nextLine(); // Clear buffer
            }
        }

        scanner.close();
    }
}
