package com.javalearning.jpa.relationships;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * COURSE ENTITY - One-to-Many Relationship Example
 *
 * Topics Demonstrated:
 * - @OneToMany mapping
 * - Bidirectional relationship with Student
 * - mappedBy attribute
 * - Cascade operations
 * - Collection types (List vs Set)
 *
 * WHAT IS ONE-TO-MANY?
 * - One Course has MANY Students
 * - Inverse: Many Students belong to ONE Course (Many-to-One)
 * - Most common relationship type
 *
 * REAL-WORLD EXAMPLES:
 * - Course → Students
 * - Department → Employees
 * - Category → Products
 * - Blog → Posts
 * - Customer → Orders
 * - Parent → Children
 * Frequency: EXTREMELY common (daily in every application)
 *
 * TYPESCRIPT/ANGULAR COMPARISON:
 * TypeScript (TypeORM):
 * @Entity()
 * class Course {
 *   @OneToMany(() => Student, student => student.course)
 *   students: Student[];
 * }
 *
 * Java (JPA/Hibernate):
 * @Entity
 * class Course {
 *   @OneToMany(mappedBy = "course")
 *   private List<Student> students;
 * }
 */
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String courseCode;  // e.g., "CS101"

    @Column(nullable = false, length = 200)
    private String courseName;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Integer credits;

    @Column(length = 100)
    private String instructor;

    // ============================================
    // ONE-TO-MANY RELATIONSHIP
    // ============================================

    /**
     * @OneToMany - Defines one-to-many relationship
     *
     * COLLECTION SIDE (Course has many Students)
     *
     * KEY CONCEPTS:
     *
     * mappedBy = "course":
     * - Indicates this is INVERSE (non-owning) side
     * - "course" refers to field name in Student class
     * - Student side has the foreign key (course_id)
     * - This side does NOT control the relationship
     *
     * IMPORTANT: Changes to this collection affect the database!
     * - Add student to list → updates student's foreign key
     * - Remove student from list → sets student's foreign key to null
     *
     * CASCADE OPTIONS:
     * - CascadeType.ALL: All operations cascade
     * - CascadeType.PERSIST: Save students when saving course
     * - CascadeType.MERGE: Update students when updating course
     * - CascadeType.REMOVE: Delete students when deleting course (DANGEROUS!)
     *
     * ORPHAN REMOVAL:
     * - orphanRemoval = true: Delete student if removed from course
     * - DANGEROUS for one-to-many (student might belong to other entities)
     * - Use carefully!
     *
     * FETCH STRATEGY:
     * - FetchType.LAZY (default, recommended): Load students when accessed
     * - FetchType.EAGER: Load students immediately (can cause performance issues)
     *
     * COLLECTION TYPE:
     * - List<Student>: Ordered, allows duplicates
     * - Set<Student>: Unordered, no duplicates (requires equals/hashCode)
     * - Collection<Student>: Generic collection
     *
     * TypeScript comparison:
     * @OneToMany(() => Student, student => student.course, {
     *   cascade: true,
     *   eager: false
     * })
     * students: Student[];
     */
    @OneToMany(
        mappedBy = "course",         // Field name in Student class
        cascade = {                   // Cascade only these operations
            CascadeType.PERSIST,      // Save new students with course
            CascadeType.MERGE         // Update students with course
            // NOT REMOVE - don't delete students when deleting course
        },
        orphanRemoval = false,        // Don't delete students removed from list
        fetch = FetchType.LAZY        // Load students only when needed
    )
    private List<StudentEnrollment> enrollments = new ArrayList<>();

    // ============================================
    // MANY-TO-MANY RELATIONSHIP
    // ============================================

    /**
     * Many-to-Many with Student (alternative relationship)
     *
     * One Course can have MANY Students
     * One Student can attend MANY Courses
     *
     * This creates a join table: student_course
     *
     * We'll use this later for Many-to-Many demo
     */
    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private Set<StudentEnrollment> studentsSet = new HashSet<>();

    // ============================================
    // CONSTRUCTORS
    // ============================================

    public Course() {
        // Required by JPA
    }

    public Course(String courseCode, String courseName, Integer credits) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
    }

    public Course(String courseCode, String courseName, String description,
                 Integer credits, String instructor) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.description = description;
        this.credits = credits;
        this.instructor = instructor;
    }

    // ============================================
    // CONVENIENCE METHODS (Helper Methods)
    // ============================================

    /**
     * Add student to course
     *
     * IMPORTANT: Helper method for bidirectional relationship
     * Updates BOTH sides of the relationship
     *
     * This is CRITICAL for bidirectional relationships!
     */
    public void addEnrollment(StudentEnrollment enrollment) {
        enrollments.add(enrollment);
        enrollment.setCourse(this);
    }

    /**
     * Remove student from course
     *
     * Updates both sides of relationship
     */
    public void removeEnrollment(StudentEnrollment enrollment) {
        enrollments.remove(enrollment);
        enrollment.setCourse(null);
    }

    /**
     * Get number of enrolled students
     */
    public int getEnrollmentCount() {
        return enrollments.size();
    }

    /**
     * Check if course is full
     */
    public boolean isFull(int maxCapacity) {
        return enrollments.size() >= maxCapacity;
    }

    // ============================================
    // GETTERS AND SETTERS
    // ============================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public List<StudentEnrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<StudentEnrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public Set<StudentEnrollment> getStudentsSet() {
        return studentsSet;
    }

    public void setStudentsSet(Set<StudentEnrollment> studentsSet) {
        this.studentsSet = studentsSet;
    }

    // ============================================
    // TOSTRING (Avoid infinite loop!)
    // ============================================

    /**
     * IMPORTANT: Don't include students in toString()
     *
     * WHY?
     * - Course.toString() calls Student.toString()
     * - Student.toString() calls Course.toString()
     * - Infinite loop! StackOverflowError!
     *
     * SOLUTION:
     * - Only show count, not full list
     * - Or use enrollment.size()
     */
    @Override
    public String toString() {
        return String.format("Course[id=%d, code='%s', name='%s', credits=%d, enrollments=%d]",
            id, courseCode, courseName, credits, enrollments.size());
    }

    /*
     * ═══════════════════════════════════════════════════════════════
     * ONE-TO-MANY RELATIONSHIP EXPLAINED
     * ═══════════════════════════════════════════════════════════════
     *
     * DATABASE REPRESENTATION:
     *
     * Table: courses
     * ┌────┬─────────────┬──────────────────┬─────────┬─────────────┐
     * │ id │ course_code │   course_name    │ credits │ instructor  │
     * ├────┼─────────────┼──────────────────┼─────────┼─────────────┤
     * │  1 │ CS101       │ Java Programming │    3    │ Prof. Smith │
     * │  2 │ CS102       │ Data Structures  │    4    │ Prof. Jones │
     * └────┴─────────────┴──────────────────┴─────────┴─────────────┘
     *
     * Table: students
     * ┌────┬───────┬──────────────┬────────────┐
     * │ id │ name  │    email     │ course_id  │ ← Foreign Key
     * ├────┼───────┼──────────────┼────────────┤
     * │  1 │ John  │ john@...     │     1      │ ← References course id=1
     * │  2 │ Jane  │ jane@...     │     1      │ ← References course id=1
     * │  3 │ Bob   │ bob@...      │     2      │ ← References course id=2
     * └────┴───────┴──────────────┴────────────┘
     *
     * Notice:
     * - Foreign key is in STUDENT table (many side)
     * - NOT in COURSE table (one side)
     * - This is standard for one-to-many
     *
     * ═══════════════════════════════════════════════════════════════
     * OWNER vs INVERSE SIDE
     * ═══════════════════════════════════════════════════════════════
     *
     * OWNER SIDE (Student - Many side):
     * - Has @ManyToOne
     * - Has @JoinColumn
     * - Has foreign key
     * - Controls relationship
     *
     * @Entity
     * class Student {
     *   @ManyToOne
     *   @JoinColumn(name = "course_id")
     *   private Course course;
     * }
     *
     * INVERSE SIDE (Course - One side):
     * - Has @OneToMany
     * - Has mappedBy
     * - No foreign key
     * - Relationship is managed by owner
     *
     * @Entity
     * class Course {
     *   @OneToMany(mappedBy = "course")
     *   private List<Student> students;
     * }
     *
     * ═══════════════════════════════════════════════════════════════
     * UNIDIRECTIONAL vs BIDIRECTIONAL
     * ═══════════════════════════════════════════════════════════════
     *
     * UNIDIRECTIONAL (One direction only):
     *
     * Option 1: Student → Course (Many-to-One only)
     * - Student knows its course
     * - Course doesn't know its students
     * - Simpler, less memory
     *
     * Option 2: Course → Students (One-to-Many only)
     * - Course knows its students
     * - Students don't know their course
     * - Less common
     *
     * BIDIRECTIONAL (Both directions):
     * - Course knows its students
     * - Students know their course
     * - Can navigate both ways
     * - More flexible but more complex
     * - MUST keep both sides in sync!
     *
     * ═══════════════════════════════════════════════════════════════
     * LIST vs SET - Which to use?
     * ═══════════════════════════════════════════════════════════════
     *
     * LIST<Student>:
     * ✓ Maintains insertion order
     * ✓ Allows duplicates (usually not wanted)
     * ✓ Indexed access (get(0))
     * ✓ Simple to use
     * ✗ Slower contains() check
     * ✗ Doesn't enforce uniqueness
     *
     * SET<Student>:
     * ✓ No duplicates (enforces uniqueness)
     * ✓ Fast contains() check
     * ✓ Better for large collections
     * ✗ No order guarantee
     * ✗ Requires equals() and hashCode()
     * ✗ More complex to implement correctly
     *
     * RECOMMENDATION:
     * - Use Set if you need uniqueness
     * - Use List if order matters
     * - Most common: List for simplicity
     *
     * ═══════════════════════════════════════════════════════════════
     * CASCADE OPERATIONS - Be Careful!
     * ═══════════════════════════════════════════════════════════════
     *
     * CascadeType.PERSIST:
     * ✓ SAFE: Save new students when saving course
     * Example: course.getStudents().add(newStudent);
     *          em.persist(course); // Also saves newStudent
     *
     * CascadeType.MERGE:
     * ✓ SAFE: Update students when updating course
     *
     * CascadeType.REMOVE:
     * ✗ DANGEROUS: Delete all students when deleting course!
     * Example: em.remove(course); // Deletes all enrolled students!
     * Usually NOT what you want!
     *
     * CascadeType.ALL:
     * ✗ VERY DANGEROUS: Includes REMOVE!
     * Only use if child cannot exist without parent
     *
     * RECOMMENDATION:
     * - Use {CascadeType.PERSIST, CascadeType.MERGE}
     * - Avoid CascadeType.REMOVE for one-to-many
     * - Only use CascadeType.ALL for composition relationships
     *
     * ═══════════════════════════════════════════════════════════════
     * COMMON MISTAKES
     * ═══════════════════════════════════════════════════════════════
     *
     * ✗ Forgetting to update both sides:
     * student.setCourse(course);
     * // course.getStudents().add(student); // FORGOT THIS!
     * // Result: Inconsistent state in memory
     *
     * ✓ Use helper methods:
     * course.addStudent(student); // Updates both sides
     *
     * ✗ Using toString() that includes collection:
     * public String toString() {
     *   return "Course: " + students; // Infinite loop!
     * }
     *
     * ✓ Only show count:
     * public String toString() {
     *   return "Course: " + students.size() + " students";
     * }
     *
     * ✗ Using CascadeType.REMOVE incorrectly:
     * @OneToMany(cascade = CascadeType.ALL) // DANGEROUS!
     *
     * ✓ Be selective with cascade:
     * @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
     *
     * ═══════════════════════════════════════════════════════════════
     * REAL-WORLD EXAMPLES
     * ═══════════════════════════════════════════════════════════════
     *
     * E-commerce:
     * - Customer → Orders
     * - Category → Products
     * - ShoppingCart → CartItems
     *
     * Social Media:
     * - User → Posts
     * - Post → Comments
     * - Album → Photos
     *
     * Blog:
     * - Author → Articles
     * - Article → Comments
     * - Category → Posts
     *
     * HR System:
     * - Department → Employees
     * - Manager → Employees
     * - Project → Tasks
     *
     * Frequency: Most common relationship type!
     * Used in almost every entity model.
     *
     * ═══════════════════════════════════════════════════════════════
     */
}
