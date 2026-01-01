package com.javalearning.taskmanagement;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * TASK MANAGEMENT APPLICATION
 *
 * This application demonstrates:
 * - Object-Oriented Programming (#39)
 * - Constructors (regular & overloaded) (#40, #41)
 * - toString method (#47)
 * - Getters and setters (#52)
 * - Wrapper classes (#55)
 * - ArrayLists (#56)
 * - Exception handling (#57)
 * - Write files (#58)
 * - Read files (#59)
 * - Dates & times (#62)
 * - Enums (#68)
 *
 * REAL-WORLD COMPARISON:
 * This is similar to building an Angular application with:
 * - Models (Task class)
 * - Enums (Priority, TaskStatus)
 * - Services (TaskManager)
 * - Components (TaskManagementApp - main controller)
 *
 * TYPESCRIPT TO JAVA COMPARISON:
 * ┌────────────────────────┬─────────────────────────┐
 * │ TypeScript/Angular     │ Java                    │
 * ├────────────────────────┼─────────────────────────┤
 * │ interface Task {}      │ class Task {}           │
 * │ enum Priority {}       │ enum Priority {}        │
 * │ @Injectable() service  │ class TaskManager {}    │
 * │ Component + template   │ Main class with UI      │
 * │ Array<Task>            │ ArrayList<Task>         │
 * │ localStorage           │ File I/O                │
 * │ try-catch              │ try-catch (stricter)    │
 * └────────────────────────┴─────────────────────────┘
 */
public class TaskManagementApp {

    /**
     * Run demonstration of all features
     */
    public static void runDemo() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║    TASK MANAGEMENT SYSTEM - DEMONSTRATION        ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        // Create task manager (like injecting a service in Angular)
        TaskManager manager = new TaskManager("demo_tasks.csv");

        // ============================================
        // DEMONSTRATE CONSTRUCTORS & OOP
        // ============================================

        System.out.println("\n📝 Creating tasks (demonstrating constructors)...");

        // Using basic constructor
        Task task1 = new Task(
            "Implement user authentication",
            "Add JWT-based authentication to the API"
        );

        // Using constructor with priority
        Task task2 = new Task(
            "Fix login bug",
            "Users can't login with special characters in password",
            Priority.HIGH
        );

        // Using full constructor with due date
        Task task3 = new Task(
            "Write API documentation",
            "Document all REST endpoints using Swagger",
            Priority.MEDIUM,
            "John Doe",
            LocalDateTime.now().plusDays(7) // Due in 7 days
        );

        Task task4 = new Task(
            "Database migration",
            "Migrate from MySQL to PostgreSQL",
            Priority.CRITICAL,
            "Jane Smith",
            LocalDateTime.now().plusDays(3)
        );

        Task task5 = new Task(
            "Update dependencies",
            "Update all npm packages to latest versions",
            Priority.LOW
        );

        // ============================================
        // DEMONSTRATE ARRAYLISTS
        // ============================================

        System.out.println("\n📋 Adding tasks to ArrayList...");

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.addTask(task4);
        manager.addTask(task5);

        System.out.println("✓ Total tasks: " + manager.getTaskCount());

        // ============================================
        // DEMONSTRATE GETTERS AND SETTERS
        // ============================================

        System.out.println("\n🔧 Using getters and setters...");

        // Start working on task
        task2.start();
        System.out.println("Started task: " + task2.getTitle());
        System.out.println("Status: " + task2.getStatus());

        // Complete task
        task1.complete();
        System.out.println("Completed task: " + task1.getTitle());

        // Update task properties
        task5.setAssignedTo("Bob Johnson");
        task5.setDueDate(LocalDateTime.now().plusDays(14));
        System.out.println("Updated task: " + task5);

        // ============================================
        // DEMONSTRATE TOSTRING METHOD
        // ============================================

        System.out.println("\n📄 Displaying tasks (toString method)...");
        manager.displayAllTasks();

        // ============================================
        // DEMONSTRATE ENUMS
        // ============================================

        System.out.println("\n🎯 Filtering by Priority (enum)...");

        ArrayList<Task> highPriorityTasks = manager.getTasksByPriority(Priority.HIGH);
        System.out.println("High priority tasks: " + highPriorityTasks.size());
        for (Task task : highPriorityTasks) {
            System.out.println("  - " + task);
        }

        ArrayList<Task> criticalTasks = manager.getTasksByPriority(Priority.CRITICAL);
        System.out.println("\nCritical tasks: " + criticalTasks.size());
        for (Task task : criticalTasks) {
            System.out.println("  - " + task);
            System.out.println("    Color code: " + task.getPriority().getColorCode());
        }

        // ============================================
        // DEMONSTRATE ARRAYLIST FILTERING
        // ============================================

        System.out.println("\n🔍 Filtering tasks by status...");

        manager.displayTasksByStatus(TaskStatus.TODO);
        manager.displayTasksByStatus(TaskStatus.IN_PROGRESS);
        manager.displayTasksByStatus(TaskStatus.COMPLETED);

        // ============================================
        // DEMONSTRATE DATES & TIMES
        // ============================================

        System.out.println("\n📅 Date and time operations...");

        for (Task task : manager.getAllTasks()) {
            if (task.getDueDate() != null) {
                System.out.println(task.getTitle());
                System.out.println("  Due: " + task.getFormattedDueDate());
                System.out.println("  Overdue: " + (task.isOverdue() ? "Yes ⚠️" : "No"));
            }
        }

        // ============================================
        // DEMONSTRATE STATISTICS
        // ============================================

        manager.displayStatistics();

        // ============================================
        // DEMONSTRATE FILE WRITING (#58)
        // ============================================

        System.out.println("\n💾 Saving tasks to file...");
        manager.saveToFile();

        // ============================================
        // DEMONSTRATE FILE READING (#59)
        // ============================================

        System.out.println("\n📂 Loading tasks from file...");

        TaskManager newManager = new TaskManager("demo_tasks.csv");
        newManager.loadFromFile();
        newManager.displayAllTasks();

        // ============================================
        // DEMONSTRATE EXCEPTION HANDLING (#57)
        // ============================================

        System.out.println("\n⚠️ Exception handling demonstration...");

        try {
            // Try to set invalid title (will throw exception)
            Task invalidTask = new Task("Valid title", "Description");
            invalidTask.setTitle(""); // This will throw IllegalArgumentException

        } catch (IllegalArgumentException e) {
            System.out.println("✓ Caught exception: " + e.getMessage());
        }

        try {
            // Try to read non-existent file
            TaskManager errorManager = new TaskManager("nonexistent_file.csv");
            errorManager.loadFromFile();
            System.out.println("✓ Gracefully handled missing file");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // ============================================
        // DEMONSTRATE WRAPPER CLASSES (#55)
        // ============================================

        System.out.println("\n🎁 Wrapper classes demonstration...");

        // Wrapper classes allow null values
        Integer taskId = task1.getId();
        System.out.println("Task ID (Integer wrapper): " + taskId);

        // Can call methods on wrapper classes
        String idString = taskId.toString();
        System.out.println("ID as string: " + idString);

        // Parsing strings to numbers
        String numberStr = "42";
        Integer parsed = Integer.parseInt(numberStr);
        System.out.println("Parsed integer: " + parsed);

        Double price = Double.valueOf("99.99");
        System.out.println("Parsed double: " + price);

        // Wrapper classes can be null (primitives can't)
        Integer nullableValue = null;
        System.out.println("Nullable Integer: " + nullableValue);
        // int primitiveValue = null; // ERROR: Can't assign null to primitive

        // ============================================
        // REAL-WORLD SCENARIO
        // ============================================

        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         REAL-WORLD SCENARIO: SPRINT PLANNING     ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        TaskManager sprintManager = new TaskManager("sprint_tasks.csv");

        // Create sprint tasks
        Task[] sprintTasks = {
            new Task(
                "Setup CI/CD pipeline",
                "Configure GitHub Actions for automated testing and deployment",
                Priority.HIGH,
                "DevOps Team",
                LocalDateTime.now().plusDays(2)
            ),
            new Task(
                "Design user dashboard",
                "Create wireframes and mockups for the user dashboard",
                Priority.MEDIUM,
                "UI/UX Team",
                LocalDateTime.now().plusDays(5)
            ),
            new Task(
                "Implement REST API",
                "Build RESTful API endpoints for user management",
                Priority.HIGH,
                "Backend Team",
                LocalDateTime.now().plusDays(7)
            ),
            new Task(
                "Write unit tests",
                "Achieve 80% code coverage with unit tests",
                Priority.MEDIUM,
                "QA Team",
                LocalDateTime.now().plusDays(10)
            )
        };

        for (Task task : sprintTasks) {
            sprintManager.addTask(task);
        }

        // Simulate sprint progress
        sprintTasks[0].start();
        sprintTasks[1].complete();

        System.out.println("\n📊 Sprint Status:");
        sprintManager.displayStatistics();

        System.out.println("\n🔥 High Priority Items:");
        for (Task task : sprintManager.getTasksByPriority(Priority.HIGH)) {
            System.out.println("  " + task);
        }

        // Save sprint data
        sprintManager.saveToFile();

        System.out.println("\n\n✅ Task Management System demonstration completed!");
        System.out.println("✅ All OOP concepts demonstrated successfully!");
    }

    /**
     * Main method
     */
    public static void main(String[] args) {
        runDemo();
    }
}
