package com.javalearning.taskmanagement;

/**
 * ENUMS (#68)
 *
 * Enums are type-safe constants - better than using strings or integers
 *
 * REAL-WORLD USE CASE:
 * - Status values (PENDING, APPROVED, REJECTED)
 * - Priority levels (LOW, MEDIUM, HIGH)
 * - User roles (USER, ADMIN, MODERATOR)
 * - Order states (CREATED, PROCESSING, SHIPPED, DELIVERED)
 * Frequency: Very common (daily in most applications)
 *
 * TYPESCRIPT COMPARISON:
 * TypeScript: enum Priority { LOW, MEDIUM, HIGH }
 * Java:       enum Priority { LOW, MEDIUM, HIGH }
 *
 * Very similar! But Java enums are more powerful (can have methods, constructors)
 */
public enum Priority {
    LOW("Low", 1),
    MEDIUM("Medium", 2),
    HIGH("High", 3),
    CRITICAL("Critical", 4);

    // Enums can have fields
    private final String displayName;
    private final int level;

    // Enums can have constructors (always private)
    Priority(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }

    // Enums can have methods
    public String getDisplayName() {
        return displayName;
    }

    public int getLevel() {
        return level;
    }

    /**
     * Check if this priority is higher than another
     */
    public boolean isHigherThan(Priority other) {
        return this.level > other.level;
    }

    /**
     * Get color code for UI display (like in Angular templates)
     */
    public String getColorCode() {
        return switch (this) {
            case LOW -> "green";
            case MEDIUM -> "yellow";
            case HIGH -> "orange";
            case CRITICAL -> "red";
        };
    }

    @Override
    public String toString() {
        return displayName;
    }

    /* TYPESCRIPT COMPARISON:
     * TypeScript enums are simpler:
     * enum Priority {
     *   LOW = 1,
     *   MEDIUM = 2,
     *   HIGH = 3
     * }
     *
     * Java enums can have:
     * - Custom fields
     * - Constructors
     * - Methods
     * - Can implement interfaces
     * Much more powerful!
     */

    /* REAL-WORLD USE IN ANGULAR:
     * export enum UserRole {
     *   USER = 'user',
     *   ADMIN = 'admin',
     *   MODERATOR = 'moderator'
     * }
     *
     * In Java, you'd use enum with more capabilities
     */
}
