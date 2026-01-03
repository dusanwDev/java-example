package com.javalearning.jpa.relationships;

import com.javalearning.jpa.embeddable.Address;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * STUDENT ENROLLMENT ENTITY - Demonstrating Multiple Relationship Types
 *
 * This entity demonstrates:
 * - Many-to-One relationship (Many students, One course)
 * - Many-to-Many relationship (Many students, Many courses)
 * - Embedded objects (@Embedded)
 * - Fetch strategies (EAGER vs LAZY)
 *
 * TYPESCRIPT/ANGULAR COMPARISON:
 * TypeScript (TypeORM):
 * @Entity()
 * class Student {
 *   @ManyToOne(() => Course)
 *   course: Course;
 *
 *   @ManyToMany(() => Course)
 *   @JoinTable()
 *   courses: Course[];
 * }
 */
@Entity
@Table(name = "student_enrollments")
public class StudentEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String studentName;

    @Column(unique = true, nullable = false)
    private String studentEmail;

    @Column
    private Integer age;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    // ============================================
    // EMBEDDED OBJECT
    // ============================================

    /**
     * @Embedded - Embeds Address object
     *
     * Address fields become columns in this table:
     * - street, city, state, zip_code, country
     *
     * No separate address table created!
     */
    @Embedded
    private Address address;

    // ============================================
    // MANY-TO-ONE RELATIONSHIP
    // ============================================

    /**
     * @ManyToOne - Many students belong to ONE course
     *
     * OWNER SIDE of the relationship
     * - This side has the foreign key
     * - Has @JoinColumn
     * - Controls the relationship
     *
     * @JoinColumn:
     * - name: Foreign key column name
     * - nullable: Can student exist without course?
     * - referencedColumnName: Column in Course table (usually id)
     *
     * FETCH STRATEGY:
     * - LAZY (default, recommended): Load course when accessed
     * - EAGER: Load course immediately with student
     *
     * TypeScript:
     * @ManyToOne(() => Course, course => course.students)
     * @JoinColumn()
     * course: Course;
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "course_id",
        nullable = true  // Student can exist without being enrolled
    )
    private Course course;

    // ============================================
    // MANY-TO-MANY RELATIONSHIP
    // ============================================

    /**
     * @ManyToMany - Student can attend MANY courses
     *               Course can have MANY students
     *
     * OWNER SIDE (has @JoinTable)
     *
     * @JoinTable - Defines the join table
     * Creates table: student_course (or custom name)
     *
     * Join table structure:
     * student_course
     * ┌────────────┬───────────┐
     * │ student_id │ course_id │
     * ├────────────┼───────────┤
     * │     1      │     1     │
     * │     1      │     2     │
     * │     2      │     1     │
     * └────────────┴───────────┘
     *
     * joinColumns:
     * - Columns that reference THIS entity (Student)
     *
     * inverseJoinColumns:
     * - Columns that reference OTHER entity (Course)
     *
     * TypeScript:
     * @ManyToMany(() => Course, course => course.students)
     * @JoinTable({
     *   name: 'student_course',
     *   joinColumn: { name: 'student_id' },
     *   inverseJoinColumn: { name: 'course_id' }
     * })
     * courses: Course[];
     */
    @ManyToMany(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        fetch = FetchType.LAZY
    )
    @JoinTable(
        name = "student_course",               // Join table name
        joinColumns = @JoinColumn(
            name = "student_id",               // Column for Student ID
            referencedColumnName = "id"
        ),
        inverseJoinColumns = @JoinColumn(
            name = "course_id",                // Column for Course ID
            referencedColumnName = "id"
        )
    )
    private Set<Course> courses = new HashSet<>();

    // ============================================
    // CONSTRUCTORS
    // ============================================

    public StudentEnrollment() {
        // Required by JPA
    }

    public StudentEnrollment(String studentName, String studentEmail) {
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.enrollmentDate = LocalDate.now();
    }

    public StudentEnrollment(String studentName, String studentEmail, Integer age) {
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.age = age;
        this.enrollmentDate = LocalDate.now();
    }

    // ============================================
    // CONVENIENCE METHODS
    // ============================================

    /**
     * Enroll in a course (Many-to-Many)
     *
     * Updates both sides of the relationship
     */
    public void enrollInCourse(Course course) {
        this.courses.add(course);
        course.getStudentsSet().add(this);
    }

    /**
     * Drop a course (Many-to-Many)
     *
     * Updates both sides
     */
    public void dropCourse(Course course) {
        this.courses.remove(course);
        course.getStudentsSet().remove(this);
    }

    /**
     * Check if enrolled in course
     */
    public boolean isEnrolledIn(Course course) {
        return courses.contains(course);
    }

    /**
     * Get total credits enrolled
     */
    public int getTotalCredits() {
        return courses.stream()
            .mapToInt(Course::getCredits)
            .sum();
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

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    // ============================================
    // EQUALS AND HASHCODE (Important for Set!)
    // ============================================

    /**
     * CRITICAL for Many-to-Many with Set!
     *
     * WHY?
     * - Set uses equals() to check duplicates
     * - Must be based on business key (email), NOT id
     * - id is null for new entities!
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentEnrollment)) return false;

        StudentEnrollment that = (StudentEnrollment) o;

        return studentEmail != null ? studentEmail.equals(that.studentEmail) : that.studentEmail == null;
    }

    @Override
    public int hashCode() {
        return studentEmail != null ? studentEmail.hashCode() : 0;
    }

    // ============================================
    // TOSTRING
    // ============================================

    @Override
    public String toString() {
        return String.format("Student[id=%d, name='%s', email='%s', courses=%d]",
            id, studentName, studentEmail, courses.size());
    }

    /*
     * ═══════════════════════════════════════════════════════════════
     * MANY-TO-MANY RELATIONSHIP EXPLAINED
     * ═══════════════════════════════════════════════════════════════
     *
     * DATABASE REPRESENTATION:
     *
     * Table: student_enrollments
     * ┌────┬──────────────┬─────────────────┬─────┐
     * │ id │ student_name │  student_email  │ ... │
     * ├────┼──────────────┼─────────────────┼─────┤
     * │  1 │ John Doe     │ john@email.com  │ ... │
     * │  2 │ Jane Smith   │ jane@email.com  │ ... │
     * └────┴──────────────┴─────────────────┴─────┘
     *
     * Table: courses
     * ┌────┬─────────────┬──────────────────┐
     * │ id │ course_code │   course_name    │
     * ├────┼─────────────┼──────────────────┤
     * │  1 │ CS101       │ Java Programming │
     * │  2 │ CS102       │ Data Structures  │
     * └────┴─────────────┴──────────────────┘
     *
     * JOIN TABLE: student_course
     * ┌────────────┬───────────┐
     * │ student_id │ course_id │ ← Composite Primary Key
     * ├────────────┼───────────┤
     * │     1      │     1     │ ← John enrolled in Java
     * │     1      │     2     │ ← John enrolled in Data Structures
     * │     2      │     1     │ ← Jane enrolled in Java
     * └────────────┴───────────┘
     *
     * Notice:
     * - Separate join table
     * - No other columns (pure relationship)
     * - Both columns are foreign keys
     * - Composite primary key (student_id, course_id)
     *
     * ═══════════════════════════════════════════════════════════════
     * WHEN TO USE MANY-TO-MANY
     * ═══════════════════════════════════════════════════════════════
     *
     * Use Many-to-Many when:
     * ✓ Both sides can have multiple
     * ✓ Pure relationship (no extra data needed)
     * ✓ Examples:
     *   - Students ↔ Courses
     *   - Users ↔ Roles
     *   - Products ↔ Categories
     *   - Authors ↔ Books
     *
     * DON'T use Many-to-Many when:
     * ✗ Need extra data in relationship
     * ✗ Example: Enrollment (student+course) needs grade, date
     *
     * Solution: Create intermediate entity!
     *
     * Instead of:
     * Student ↔ Course (can't store grade)
     *
     * Use:
     * Student → Enrollment ← Course
     * Enrollment has: student, course, grade, enrollmentDate
     *
     * ═══════════════════════════════════════════════════════════════
     * OWNER vs INVERSE SIDE
     * ═══════════════════════════════════════════════════════════════
     *
     * OWNER SIDE (has @JoinTable):
     * @Entity
     * class Student {
     *   @ManyToMany
     *   @JoinTable(...)
     *   private Set<Course> courses;
     * }
     *
     * INVERSE SIDE (has mappedBy):
     * @Entity
     * class Course {
     *   @ManyToMany(mappedBy = "courses")
     *   private Set<Student> students;
     * }
     *
     * Which should be owner?
     * - Choose based on business logic
     * - Usually the entity you'll modify more often
     * - Example: Student enrolls in courses (Student is owner)
     *
     * ═══════════════════════════════════════════════════════════════
     * IMPORTANT: equals() and hashCode() with Set
     * ═══════════════════════════════════════════════════════════════
     *
     * MUST override for entities in Set!
     *
     * ✗ WRONG (using id):
     * public boolean equals(Object o) {
     *   return id.equals(that.id); // id is null for new entities!
     * }
     *
     * ✓ CORRECT (using business key):
     * public boolean equals(Object o) {
     *   return email.equals(that.email); // email exists before save
     * }
     *
     * Why?
     * - Set uses equals() to check duplicates
     * - New entities don't have id yet
     * - Use natural identifier (email, username, etc.)
     *
     * ═══════════════════════════════════════════════════════════════
     * REAL-WORLD EXAMPLES
     * ═══════════════════════════════════════════════════════════════
     *
     * E-commerce:
     * - Products ↔ Categories
     * - Orders ↔ Products (with OrderItem intermediate)
     *
     * Social Media:
     * - Users ↔ Groups
     * - Posts ↔ Tags
     * - Users ↔ Followers (self-referencing)
     *
     * Education:
     * - Students ↔ Courses
     * - Teachers ↔ Classes
     *
     * Security:
     * - Users ↔ Roles
     * - Roles ↔ Permissions
     *
     * Frequency: Common in complex domains
     *
     * ═══════════════════════════════════════════════════════════════
     */
}
