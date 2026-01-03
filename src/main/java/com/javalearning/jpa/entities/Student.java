package com.javalearning.jpa.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * STUDENT ENTITY - Basic JPA/Hibernate Example
 *
 * Topics Demonstrated:
 * - @Entity annotation
 * - @Table annotation
 * - @Id and @GeneratedValue
 * - @Column with properties
 * - @Temporal for dates
 * - @Transient for non-persistent fields
 * - @Enumerated for enums
 *
 * WHAT IS AN ENTITY?
 * - A Java class mapped to a database table
 * - Each instance = one row in the table
 * - Each field = one column in the table
 * - Managed by JPA/Hibernate
 *
 * REAL-WORLD USE:
 * - User accounts, Products, Orders, etc.
 * - Any data that needs to be stored in database
 * - Frequency: EVERY database application uses entities (100%)
 *
 * TYPESCRIPT/ANGULAR COMPARISON:
 * TypeScript (TypeORM):
 * @Entity()
 * export class Student {
 *   @PrimaryGeneratedColumn()
 *   id: number;
 *
 *   @Column()
 *   name: string;
 * }
 *
 * Java (JPA/Hibernate):
 * @Entity
 * public class Student {
 *   @Id
 *   @GeneratedValue
 *   private Long id;
 *
 *   @Column
 *   private String name;
 * }
 *
 * Very similar! JPA influenced TypeORM design.
 */

// ============================================
// @ENTITY ANNOTATION
// ============================================

/**
 * @Entity - Marks this class as a JPA entity
 *
 * WHAT IT DOES:
 * - Tells Hibernate to manage this class
 * - Creates a table in the database
 * - Enables CRUD operations
 *
 * WITHOUT @Entity:
 * - Just a regular Java class
 * - No database mapping
 * - Can't use EntityManager with it
 */
@Entity

// ============================================
// @TABLE ANNOTATION (Optional)
// ============================================

/**
 * @Table - Customizes table mapping
 *
 * OPTIONAL: If not specified, table name = class name
 *
 * Properties:
 * - name: Custom table name
 * - schema: Database schema
 * - catalog: Database catalog
 * - uniqueConstraints: Unique constraints
 * - indexes: Table indexes
 *
 * Example without @Table:
 * Class Student -> table "Student"
 *
 * Example with @Table:
 * @Table(name="students") -> table "students"
 */
@Table(name = "students")  // Table will be named "students" not "Student"
public class Student {

    // ============================================
    // PRIMARY KEY (@Id)
    // ============================================

    /**
     * @Id - Marks the primary key field
     *
     * REQUIRED: Every entity MUST have a primary key!
     *
     * WHAT IS PRIMARY KEY?
     * - Unique identifier for each row
     * - Cannot be null
     * - Cannot be duplicated
     * - Used for relationships
     *
     * TypeScript comparison:
     * @PrimaryColumn() or @PrimaryGeneratedColumn()
     */
    @Id

    /**
     * @GeneratedValue - Auto-generate ID values
     *
     * Strategies:
     * - AUTO: Let Hibernate choose (default)
     * - IDENTITY: Use database auto-increment (MySQL, PostgreSQL)
     * - SEQUENCE: Use database sequence (Oracle, PostgreSQL)
     * - TABLE: Use separate table for IDs (portable but slower)
     *
     * Most common: GenerationType.IDENTITY
     *
     * TypeScript:
     * @PrimaryGeneratedColumn() // Similar to IDENTITY
     * @PrimaryGeneratedColumn('uuid') // For UUIDs
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    /**
     * @Column - Customizes column mapping
     *
     * Properties demonstrated below in other fields
     * For ID, usually no customization needed
     */
    private Long id;

    // ============================================
    // REGULAR COLUMNS
    // ============================================

    /**
     * @Column with customizations
     *
     * Properties:
     * - name: Column name in database
     * - nullable: Can be NULL? (default: true)
     * - unique: Must be unique? (default: false)
     * - length: For String/varchar (default: 255)
     * - precision: For decimal numbers
     * - scale: For decimal numbers
     * - insertable: Include in INSERT? (default: true)
     * - updatable: Include in UPDATE? (default: true)
     *
     * TypeScript:
     * @Column({ length: 100, nullable: false })
     * name: string;
     */
    @Column(
        name = "student_name",  // Column name in DB
        nullable = false,       // NOT NULL constraint
        length = 100            // VARCHAR(100)
    )
    private String name;

    /**
     * Email with unique constraint
     *
     * unique = true creates UNIQUE constraint in database
     * No two students can have same email
     */
    @Column(
        nullable = false,
        unique = true,
        length = 150
    )
    private String email;

    /**
     * Age - simple numeric column
     *
     * No @Column needed if you're happy with defaults
     * Column name will be "age" (same as field name)
     */
    private Integer age;

    /**
     * Date of birth
     *
     * For dates, Hibernate automatically handles conversion
     * Java LocalDate <-> SQL DATE
     *
     * Other date types:
     * - LocalDateTime -> TIMESTAMP
     * - LocalTime -> TIME
     * - java.util.Date -> TIMESTAMP (old way)
     */
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    // ============================================
    // ENUM COLUMN
    // ============================================

    /**
     * @Enumerated - Map Java enum to database
     *
     * Two strategies:
     *
     * 1. EnumType.ORDINAL (default):
     *    - Stores enum position (0, 1, 2, ...)
     *    - DANGEROUS: Reordering enum breaks data!
     *    - Smaller storage
     *    - Example: FRESHMAN=0, SOPHOMORE=1
     *
     * 2. EnumType.STRING (recommended):
     *    - Stores enum name as string
     *    - Safe: Reordering doesn't matter
     *    - More storage but safer
     *    - Example: "FRESHMAN", "SOPHOMORE"
     *
     * ALWAYS USE STRING in production!
     *
     * TypeScript:
     * @Column({ type: 'enum', enum: GradeLevel })
     * gradeLevel: GradeLevel;
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private GradeLevel gradeLevel;

    // ============================================
    // TRANSIENT FIELD
    // ============================================

    /**
     * @Transient - NOT stored in database
     *
     * Use for:
     * - Calculated fields
     * - Temporary values
     * - Helper fields
     * - Cache variables
     *
     * This field exists in Java object but NOT in database table
     *
     * Example: Full name calculated from first + last
     *          Age calculated from date of birth
     *
     * TypeScript:
     * No annotation needed, just don't use @Column()
     */
    @Transient
    private String fullName;  // Not stored in DB

    // ============================================
    // LARGE TEXT COLUMN
    // ============================================

    /**
     * @Lob - Large Object
     *
     * For large text or binary data
     * Maps to:
     * - TEXT, CLOB for strings
     * - BLOB for byte[]
     *
     * Use for:
     * - Long descriptions
     * - Article content
     * - JSON data
     * - File content
     *
     * TypeScript:
     * @Column({ type: 'text' })
     */
    @Lob
    @Column(name = "notes")
    private String notes;  // Can store large amounts of text

    // ============================================
    // CONSTRUCTORS
    // ============================================

    /**
     * Default constructor (REQUIRED by JPA)
     *
     * IMPORTANT:
     * - JPA requires no-arg constructor
     * - Can be protected/private
     * - Hibernate uses reflection to create instances
     *
     * WHY REQUIRED?
     * When Hibernate loads data from DB:
     * 1. Creates object using no-arg constructor
     * 2. Sets fields using reflection
     * 3. Returns populated object
     */
    public Student() {
        // Required by JPA
    }

    /**
     * Parameterized constructor for convenience
     */
    public Student(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    /**
     * Full constructor
     */
    public Student(String name, String email, Integer age,
                  LocalDate dateOfBirth, GradeLevel gradeLevel) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.gradeLevel = gradeLevel;
    }

    // ============================================
    // GETTERS AND SETTERS
    // ============================================

    /**
     * Getters and setters
     *
     * REQUIRED for JPA properties
     * Hibernate uses these to access fields
     *
     * Can use:
     * - Manual getters/setters (shown here)
     * - Lombok @Data annotation
     * - IDE generation
     * - Java Records (with limitations)
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public GradeLevel getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(GradeLevel gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // ============================================
    // TOSTRING (Helpful for debugging)
    // ============================================

    @Override
    public String toString() {
        return String.format("Student[id=%d, name='%s', email='%s', age=%d, grade=%s]",
            id, name, email, age, gradeLevel);
    }

    /*
     * ═══════════════════════════════════════════════════════════════
     * ANNOTATION SUMMARY
     * ═══════════════════════════════════════════════════════════════
     *
     * @Entity          - Mark class as entity (REQUIRED)
     * @Table           - Customize table name (optional)
     * @Id              - Mark primary key (REQUIRED)
     * @GeneratedValue  - Auto-generate ID
     * @Column          - Customize column mapping
     * @Enumerated      - Map enum to database
     * @Transient       - Exclude from database
     * @Lob             - Large text/binary data
     *
     * ═══════════════════════════════════════════════════════════════
     * FIELD ACCESS vs PROPERTY ACCESS
     * ═══════════════════════════════════════════════════════════════
     *
     * Field Access (shown here):
     * - Annotations on fields
     * - Hibernate reads fields directly
     * - @Id on field
     *
     * Property Access:
     * - Annotations on getters
     * - Hibernate uses getters/setters
     * - @Id on getId() method
     *
     * Recommendation: Use Field Access (simpler)
     *
     * ═══════════════════════════════════════════════════════════════
     * REAL-WORLD USE CASES
     * ═══════════════════════════════════════════════════════════════
     *
     * Student Entity (Learning Management System):
     * - Store student profiles
     * - Track enrollment
     * - Manage grades
     * - Handle authentication
     *
     * Similar Entities in Real Projects:
     * - User (authentication systems)
     * - Product (e-commerce)
     * - Order (shopping carts)
     * - Post (blogs, social media)
     * - Employee (HR systems)
     *
     * Frequency: Every application with database uses entities!
     *
     * ═══════════════════════════════════════════════════════════════
     */
}

/**
 * Grade Level Enum
 *
 * Demonstrates enum usage with JPA
 */
enum GradeLevel {
    FRESHMAN,
    SOPHOMORE,
    JUNIOR,
    SENIOR,
    GRADUATE
}
