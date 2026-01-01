package com.javalearning.taskmanagement;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TASK MANAGER - Demonstrates Collections and File I/O
 *
 * Topics covered:
 * - ArrayLists (#56)
 * - Exception handling (#57)
 * - Write files (#58)
 * - Read files (#59)
 *
 * REAL-WORLD USE CASE:
 * This is like an Angular service that manages state
 * Similar to a CRUD service with localStorage or HTTP calls
 *
 * TYPESCRIPT COMPARISON:
 * TypeScript service:
 * @Injectable()
 * class TaskService {
 *   private tasks: Task[] = [];
 *   getTasks(): Task[] { return this.tasks; }
 *   addTask(task: Task) { this.tasks.push(task); }
 * }
 */
public class TaskManager {

    // ============================================
    // ARRAYLISTS (#56)
    // ============================================

    /**
     * ARRAYLIST (#56)
     *
     * Dynamic array (unlike fixed-size arrays)
     * This is what you're used to from TypeScript!
     *
     * TypeScript: private tasks: Task[] = [];
     * Java:       private ArrayList<Task> tasks = new ArrayList<>();
     *
     * ArrayList is part of Java Collections Framework
     * - Dynamic size (grows/shrinks automatically)
     * - Type-safe with generics (<Task>)
     * - Many useful methods (add, remove, contains, etc.)
     *
     * REAL-WORLD USE:
     * Used EVERYWHERE in Java development
     * Frequency: 100% - You'll use ArrayLists daily
     */
    private ArrayList<Task> tasks;
    private String filename;

    // ============================================
    // CONSTRUCTOR
    // ============================================

    public TaskManager() {
        this.tasks = new ArrayList<>();
        this.filename = "tasks.csv";
    }

    public TaskManager(String filename) {
        this.tasks = new ArrayList<>();
        this.filename = filename;
    }

    // ============================================
    // ARRAYLIST OPERATIONS
    // ============================================

    /**
     * Add a task (like Array.push() in TypeScript)
     */
    public void addTask(Task task) {
        tasks.add(task);
        System.out.println("✓ Task added: " + task.getTitle());
    }

    /**
     * Remove a task by ID (like Array.splice() in TypeScript)
     */
    public boolean removeTask(int id) {
        // Using removeIf (Java 8+) - similar to Array.filter() in TypeScript
        boolean removed = tasks.removeIf(task -> task.getId() == id);

        if (removed) {
            System.out.println("✓ Task removed");
        } else {
            System.out.println("✗ Task not found");
        }

        return removed;

        /* TypeScript equivalent:
         * this.tasks = this.tasks.filter(task => task.id !== id);
         */
    }

    /**
     * Get task by ID (like Array.find() in TypeScript)
     */
    public Task getTaskById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;

        /* TypeScript equivalent:
         * return this.tasks.find(task => task.id === id);
         *
         * Or with Java Streams (more advanced):
         * return tasks.stream()
         *     .filter(task -> task.getId() == id)
         *     .findFirst()
         *     .orElse(null);
         */
    }

    /**
     * Get all tasks
     */
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks); // Return copy for encapsulation
    }

    /**
     * Get tasks by status (like Array.filter() in TypeScript)
     */
    public ArrayList<Task> getTasksByStatus(TaskStatus status) {
        ArrayList<Task> filtered = new ArrayList<>();

        for (Task task : tasks) {
            if (task.getStatus() == status) {
                filtered.add(task);
            }
        }

        return filtered;

        /* TypeScript equivalent:
         * return this.tasks.filter(task => task.status === status);
         */
    }

    /**
     * Get tasks by priority
     */
    public ArrayList<Task> getTasksByPriority(Priority priority) {
        ArrayList<Task> filtered = new ArrayList<>();

        for (Task task : tasks) {
            if (task.getPriority() == priority) {
                filtered.add(task);
            }
        }

        return filtered;
    }

    /**
     * Get overdue tasks
     */
    public ArrayList<Task> getOverdueTasks() {
        ArrayList<Task> overdue = new ArrayList<>();

        for (Task task : tasks) {
            if (task.isOverdue()) {
                overdue.add(task);
            }
        }

        return overdue;
    }

    /**
     * Get task count (like Array.length in TypeScript)
     */
    public int getTaskCount() {
        return tasks.size(); // Note: size() not length in Java
    }

    // ============================================
    // EXCEPTION HANDLING (#57)
    // ============================================

    /**
     * EXCEPTION HANDLING (#57)
     *
     * Try-catch blocks handle runtime errors
     * Similar to TypeScript try-catch, but Java is stricter
     *
     * TypeScript:
     * try {
     *   // risky code
     * } catch (error) {
     *   console.error(error);
     * }
     *
     * Java:
     * try {
     *   // risky code
     * } catch (IOException e) {
     *   e.printStackTrace();
     * }
     *
     * Key differences:
     * - Java has checked exceptions (must handle or declare)
     * - Can catch specific exception types
     * - Finally block always executes (cleanup)
     */

    // ============================================
    // WRITE FILES (#58)
    // ============================================

    /**
     * WRITE FILES (#58)
     *
     * Save tasks to file (like localStorage.setItem() in Angular)
     *
     * REAL-WORLD USE:
     * - Saving configuration
     * - Exporting data
     * - Logging
     * - Data persistence
     * Frequency: Common (several times per week)
     */
    public void saveToFile() {
        // Try-with-resources (automatic resource cleanup - Java 7+)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {

            // Write header
            writer.write("ID,Title,Description,Priority,Status,CreatedAt,DueDate,AssignedTo");
            writer.newLine();

            // Write each task
            for (Task task : tasks) {
                writer.write(task.toCSV());
                writer.newLine();
            }

            System.out.println("✓ Tasks saved to " + filename);

        } catch (IOException e) {
            // Exception handling
            System.err.println("❌ Error saving tasks: " + e.getMessage());
            e.printStackTrace();
        }

        /* TypeScript/Angular equivalent:
         * localStorage.setItem('tasks', JSON.stringify(this.tasks));
         *
         * Or with file system in Node.js:
         * fs.writeFileSync('tasks.json', JSON.stringify(this.tasks));
         */

        /* EXCEPTION HANDLING NOTES:
         * - IOException is a checked exception (must handle)
         * - Try-with-resources automatically closes the writer
         * - catch block handles any errors during file writing
         *
         * Without try-catch, code won't compile!
         */
    }

    // ============================================
    // READ FILES (#59)
    // ============================================

    /**
     * READ FILES (#59)
     *
     * Load tasks from file (like localStorage.getItem() in Angular)
     *
     * REAL-WORLD USE:
     * - Loading configuration
     * - Importing data
     * - Reading logs
     * - Data persistence
     * Frequency: Common (several times per week)
     */
    public void loadFromFile() {
        File file = new File(filename);

        // Check if file exists
        if (!file.exists()) {
            System.out.println("ℹ️ No saved tasks found");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

            String line;
            int lineNumber = 0;

            // Read file line by line
            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Skip header
                if (lineNumber == 1) {
                    continue;
                }

                try {
                    // Parse CSV line (simplified - real-world would use CSV library)
                    String[] parts = line.split(",");

                    if (parts.length >= 5) {
                        String title = parts[1].replace("\"", "");
                        String description = parts[2].replace("\"", "");
                        Priority priority = Priority.valueOf(parts[3]);

                        Task task = new Task(title, description, priority);
                        tasks.add(task);
                    }

                } catch (Exception e) {
                    System.err.println("⚠️ Error parsing line " + lineNumber + ": " + e.getMessage());
                }
            }

            System.out.println("✓ Loaded " + tasks.size() + " tasks from " + filename);

        } catch (IOException e) {
            System.err.println("❌ Error loading tasks: " + e.getMessage());
            e.printStackTrace();
        }

        /* TypeScript/Angular equivalent:
         * const data = localStorage.getItem('tasks');
         * this.tasks = data ? JSON.parse(data) : [];
         *
         * Or with file system in Node.js:
         * const data = fs.readFileSync('tasks.json', 'utf-8');
         * this.tasks = JSON.parse(data);
         */
    }

    // ============================================
    // DISPLAY METHODS
    // ============================================

    /**
     * Display all tasks
     */
    public void displayAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found");
            return;
        }

        System.out.println("\n=== ALL TASKS ===");
        for (Task task : tasks) {
            System.out.println(task); // Calls toString() automatically
        }
    }

    /**
     * Display tasks by status
     */
    public void displayTasksByStatus(TaskStatus status) {
        ArrayList<Task> filtered = getTasksByStatus(status);

        System.out.println("\n=== " + status.getDisplayName().toUpperCase() + " TASKS ===");

        if (filtered.isEmpty()) {
            System.out.println("No tasks with status: " + status);
            return;
        }

        for (Task task : filtered) {
            System.out.println(task);
        }
    }

    /**
     * Display statistics
     */
    public void displayStatistics() {
        System.out.println("\n=== TASK STATISTICS ===");
        System.out.println("Total tasks: " + tasks.size());
        System.out.println("To Do: " + getTasksByStatus(TaskStatus.TODO).size());
        System.out.println("In Progress: " + getTasksByStatus(TaskStatus.IN_PROGRESS).size());
        System.out.println("Completed: " + getTasksByStatus(TaskStatus.COMPLETED).size());
        System.out.println("Cancelled: " + getTasksByStatus(TaskStatus.CANCELLED).size());
        System.out.println("Overdue: " + getOverdueTasks().size());
    }

    /* ARRAYLIST METHODS SUMMARY (similar to TypeScript Array methods):
     *
     * Java ArrayList              | TypeScript Array
     * ----------------------------|---------------------------
     * list.add(item)              | array.push(item)
     * list.remove(index)          | array.splice(index, 1)
     * list.get(index)             | array[index]
     * list.set(index, item)       | array[index] = item
     * list.size()                 | array.length
     * list.isEmpty()              | array.length === 0
     * list.contains(item)         | array.includes(item)
     * list.clear()                | array = []
     * list.indexOf(item)          | array.indexOf(item)
     *
     * For functional operations (map, filter, reduce), Java uses Streams API
     */
}
