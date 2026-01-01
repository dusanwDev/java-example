package com.javalearning.taskmanagement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TASK CLASS - Demonstrates OOP Concepts
 *
 * Topics covered:
 * - Object-Oriented Programming (#39)
 * - Constructors (#40)
 * - Overloaded constructors (#41)
 * - toString method (#47)
 * - Getters and setters (#52)
 * - Wrapper classes (#55)
 * - Dates & times (#62)
 *
 * REAL-WORLD USE CASE:
 * This is like creating a TypeScript interface + class in Angular
 * Similar to a model in an Angular service
 *
 * TYPESCRIPT COMPARISON:
 * TypeScript:
 * interface Task {
 *   id: number;
 *   title: string;
 *   description?: string;
 * }
 *
 * Java:
 * public class Task {
 *   private int id;
 *   private String title;
 *   private String description;
 *   // + getters/setters
 * }
 */
public class Task {

    // ============================================
    // FIELDS (Private - encapsulation)
    // ============================================

    private int id;
    private String title;
    private String description;
    private Priority priority;
    private TaskStatus status;
    private LocalDateTime createdAt;      // #62 Dates & times
    private LocalDateTime dueDate;
    private String assignedTo;

    // Static counter for auto-incrementing IDs (like database auto-increment)
    private static int nextId = 1;

    // ============================================
    // CONSTRUCTORS (#40, #41)
    // ============================================

    /**
     * CONSTRUCTOR (#40)
     * Special method to initialize objects (like TypeScript constructors)
     *
     * TypeScript:
     * constructor(private title: string, private description: string) {}
     *
     * Java requires explicit getter/setter creation
     */
    public Task(String title, String description, Priority priority) {
        this.id = nextId++;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = TaskStatus.TODO;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * OVERLOADED CONSTRUCTOR (#41)
     * Different ways to create a Task object
     *
     * This is common in Java - providing multiple constructors for flexibility
     * In TypeScript, you'd use optional parameters instead
     */
    public Task(String title, String description) {
        this(title, description, Priority.MEDIUM); // Call other constructor
    }

    /**
     * Constructor with all fields
     */
    public Task(String title, String description, Priority priority, String assignedTo, LocalDateTime dueDate) {
        this(title, description, priority);
        this.assignedTo = assignedTo;
        this.dueDate = dueDate;
    }

    /* REAL-WORLD USE:
     * Different constructors for different creation scenarios:
     * - Quick task creation: new Task("Fix bug", "Description")
     * - Full task creation: new Task("Feature", "Desc", Priority.HIGH, "John", dueDate)
     *
     * Similar to having factory methods in Angular services
     */

    // ============================================
    // GETTERS AND SETTERS (#52)
    // ============================================

    /**
     * GETTERS AND SETTERS (#52)
     *
     * Java uses explicit getter/setter methods for encapsulation
     * TypeScript can use get/set keywords or public properties
     *
     * TypeScript (public properties):
     * class Task {
     *   public title: string;
     * }
     * task.title = "New title";
     *
     * TypeScript (getters/setters):
     * class Task {
     *   private _title: string;
     *   get title() { return this._title; }
     *   set title(value: string) { this._title = value; }
     * }
     *
     * Java (explicit methods):
     * public String getTitle() { return title; }
     * public void setTitle(String title) { this.title = title; }
     */

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        // Can add validation in setters
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    // ============================================
    // BUSINESS METHODS
    // ============================================

    /**
     * Mark task as completed
     */
    public void complete() {
        this.status = TaskStatus.COMPLETED;
    }

    /**
     * Start working on task
     */
    public void start() {
        this.status = TaskStatus.IN_PROGRESS;
    }

    /**
     * Cancel task
     */
    public void cancel() {
        this.status = TaskStatus.CANCELLED;
    }

    /**
     * Check if task is overdue
     */
    public boolean isOverdue() {
        if (dueDate == null || status == TaskStatus.COMPLETED) {
            return false;
        }
        return LocalDateTime.now().isAfter(dueDate);
    }

    /**
     * Get formatted due date
     * Demonstrates date formatting (#62)
     */
    public String getFormattedDueDate() {
        if (dueDate == null) {
            return "No due date";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        return dueDate.format(formatter);
    }

    /**
     * Get task for file storage (CSV format)
     */
    public String toCSV() {
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s",
            id,
            escapeCSV(title),
            escapeCSV(description),
            priority,
            status,
            createdAt,
            dueDate != null ? dueDate : "",
            assignedTo != null ? assignedTo : ""
        );
    }

    private String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    // ============================================
    // TOSTRING METHOD (#47)
    // ============================================

    /**
     * TOSTRING METHOD (#47)
     *
     * Provides string representation of the object
     * Like TypeScript's toString() or JSON.stringify() for debugging
     *
     * Called automatically when:
     * - System.out.println(task); // Calls toString() automatically
     * - "Task: " + task; // Calls toString() automatically
     *
     * TypeScript equivalent:
     * toString(): string {
     *   return `Task: ${this.title}`;
     * }
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Task #").append(id).append(": ").append(title);
        sb.append(" [").append(status).append("]");
        sb.append(" (").append(priority).append(")");

        if (assignedTo != null) {
            sb.append(" - Assigned to: ").append(assignedTo);
        }

        if (dueDate != null) {
            sb.append(" - Due: ").append(getFormattedDueDate());
            if (isOverdue()) {
                sb.append(" ⚠️ OVERDUE");
            }
        }

        return sb.toString();
    }

    /* WRAPPER CLASSES (#55)
     *
     * Notice we use:
     * - Integer, not int (can be null)
     * - String (already an object)
     * - LocalDateTime (object)
     *
     * Wrapper classes:
     * int -> Integer
     * double -> Double
     * boolean -> Boolean
     * char -> Character
     *
     * Why use wrappers?
     * - Can be null (primitives can't)
     * - Can be used in collections (ArrayList<Integer>, not ArrayList<int>)
     * - Have useful methods (Integer.parseInt(), Double.parseDouble())
     *
     * REAL-WORLD USE:
     * - Optional values (like TypeScript's number | null)
     * - Collections (ArrayList, HashMap)
     * - Database operations (nullable columns)
     * Frequency: Very common (daily)
     */
}
