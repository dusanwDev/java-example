package com.javalearning.jpa.demos;

import com.javalearning.jpa.config.HibernateUtil;
import com.javalearning.jpa.embeddable.Address;
import com.javalearning.jpa.relationships.*;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;

/**
 * RELATIONSHIPS DEMONSTRATION
 *
 * Topics Demonstrated:
 * - One-to-One relationship (Person ↔ Passport)
 * - One-to-Many relationship (Course → Students)
 * - Many-to-One relationship (Student → Course)
 * - Many-to-Many relationship (Student ↔ Courses)
 * - Embedded objects (Address)
 * - Cascade operations
 * - Bidirectional relationships
 *
 * REAL-WORLD USE CASE:
 * University Management System
 * - Students enroll in courses
 * - Courses have multiple students
 * - Students have passports (one-to-one)
 * - Students have addresses (embedded)
 *
 * TYPESCRIPT/ANGULAR COMPARISON:
 * This is like managing state in an Angular application with:
 * - Related entities (like normalized NgRx state)
 * - Nested objects (like component hierarchies)
 * - Foreign key relationships (like API responses)
 */
public class RelationshipsDemo {

    /**
     * ONE-TO-ONE RELATIONSHIP DEMO
     * Person ↔ Passport
     */
    public static void demonstrateOneToOne() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║      ONE-TO-ONE RELATIONSHIP DEMO                ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        EntityManager em = HibernateUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            // ============================================
            // CREATE PERSON AND PASSPORT
            // ============================================

            System.out.println("\n📝 Creating Person and Passport...");

            // Create Person
            Person person = new Person("John", "Doe", "john.doe@email.com");

            // Create Passport
            Passport passport = new Passport(
                "US123456789",
                LocalDate.of(2020, 1, 15),
                LocalDate.of(2030, 1, 15),
                "USA"
            );

            // Link them together (Unidirectional)
            person.setPassport(passport);

            // For Bidirectional, also set:
            // passport.setPerson(person);
            // Or use helper method:
            // person.setPassportBidirectional(passport);

            // Save person (CASCADE will save passport too!)
            em.persist(person);

            System.out.println("✓ Person saved: " + person.getFullName());
            System.out.println("✓ Passport saved: " + passport.getPassportNumber());
            System.out.println("ℹ️ CASCADE.PERSIST saved passport automatically!");

            em.getTransaction().commit();

            // ============================================
            // READ PERSON WITH PASSPORT
            // ============================================

            System.out.println("\n📖 Reading Person and Passport...");

            em.clear(); // Clear cache to force DB read

            Person foundPerson = em.find(Person.class, person.getId());
            System.out.println("Found: " + foundPerson);

            // Access passport (triggers LAZY loading if configured)
            if (foundPerson.getPassport() != null) {
                System.out.println("Passport: " + foundPerson.getPassport());
                System.out.println("Expires: " + foundPerson.getPassport().getExpiryDate());
            }

            // ============================================
            // UPDATE PASSPORT
            // ============================================

            System.out.println("\n✏️ Updating Passport...");

            em.getTransaction().begin();

            // Update passport expiry date
            foundPerson.getPassport().setExpiryDate(LocalDate.of(2035, 1, 15));

            em.getTransaction().commit();

            System.out.println("✓ Passport updated (CASCADE.MERGE)");

            // ============================================
            // DELETE PERSON (CASCADE DELETE PASSPORT)
            // ============================================

            System.out.println("\n🗑️ Deleting Person...");

            em.getTransaction().begin();

            em.remove(foundPerson);

            em.getTransaction().commit();

            System.out.println("✓ Person deleted");
            System.out.println("✓ Passport also deleted (CASCADE.REMOVE)");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }

        /*
         * WHAT HAPPENED IN DATABASE:
         *
         * 1. INSERT INTO persons (...) VALUES (...);
         * 2. INSERT INTO passports (...) VALUES (...);
         * 3. UPDATE persons SET passport_id = ... WHERE id = ...;
         *
         * Or with proper cascade:
         * 1. INSERT INTO passports (...) VALUES (...);
         * 2. INSERT INTO persons (... passport_id) VALUES (... passport.id);
         *
         * 4. SELECT * FROM persons WHERE id = ...;
         * 5. SELECT * FROM passports WHERE id = ...; (if accessed)
         *
         * 6. UPDATE passports SET expiry_date = ... WHERE id = ...;
         *
         * 7. DELETE FROM persons WHERE id = ...;
         * 8. DELETE FROM passports WHERE id = ...; (cascade)
         *
         * REAL-WORLD USE CASE:
         * - User ↔ Profile
         * - Employee ↔ Parking Spot
         * - Product ↔ Product Details
         * - Order ↔ Invoice
         */
    }

    /**
     * ONE-TO-MANY / MANY-TO-ONE RELATIONSHIP DEMO
     * Course → Students (One-to-Many)
     * Student → Course (Many-to-One)
     */
    public static void demonstrateOneToManyManyToOne() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║    ONE-TO-MANY / MANY-TO-ONE DEMO                ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        EntityManager em = HibernateUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            // ============================================
            // CREATE COURSE
            // ============================================

            System.out.println("\n📚 Creating Course...");

            Course course = new Course(
                "CS101",
                "Introduction to Java Programming",
                "Learn Java from scratch",
                3,
                "Prof. Smith"
            );

            em.persist(course);

            System.out.println("✓ Course created: " + course.getCourseCode());

            // ============================================
            // CREATE STUDENTS AND ENROLL
            // ============================================

            System.out.println("\n👨‍🎓 Creating Students...");

            StudentEnrollment student1 = new StudentEnrollment(
                "Alice Johnson",
                "alice@email.com",
                20
            );

            StudentEnrollment student2 = new StudentEnrollment(
                "Bob Smith",
                "bob@email.com",
                22
            );

            StudentEnrollment student3 = new StudentEnrollment(
                "Charlie Brown",
                "charlie@email.com",
                21
            );

            // Add addresses (Embedded objects)
            student1.setAddress(new Address(
                "123 Main St", "Boston", "MA", "02101", "USA"
            ));

            student2.setAddress(new Address(
                "456 Oak Ave", "Cambridge", "MA", "02139", "USA"
            ));

            // ============================================
            // ESTABLISH RELATIONSHIP
            // ============================================

            System.out.println("\n🔗 Enrolling students in course...");

            // Method 1: Set course on student (OWNER side)
            student1.setCourse(course);
            student2.setCourse(course);
            student3.setCourse(course);

            // Method 2: Use helper method (updates both sides)
            // course.addEnrollment(student1);

            // Save students
            em.persist(student1);
            em.persist(student2);
            em.persist(student3);

            System.out.println("✓ Students enrolled:");
            System.out.println("  - " + student1.getStudentName());
            System.out.println("  - " + student2.getStudentName());
            System.out.println("  - " + student3.getStudentName());

            em.getTransaction().commit();

            // ============================================
            // READ COURSE WITH STUDENTS
            // ============================================

            System.out.println("\n📖 Reading Course with Students...");

            em.clear(); // Clear cache

            Course foundCourse = em.find(Course.class, course.getId());
            System.out.println("\nCourse: " + foundCourse.getCourseName());
            System.out.println("Instructor: " + foundCourse.getInstructor());
            System.out.println("Enrolled students: " + foundCourse.getEnrollmentCount());

            // Access students (triggers LAZY loading)
            System.out.println("\nStudent List:");
            for (StudentEnrollment s : foundCourse.getEnrollments()) {
                System.out.println("  - " + s.getStudentName() +
                                 " (" + s.getStudentEmail() + ")");
                if (s.getAddress() != null) {
                    System.out.println("    Address: " + s.getAddress());
                }
            }

            // ============================================
            // READ STUDENT WITH COURSE
            // ============================================

            System.out.println("\n📖 Reading Student with Course...");

            StudentEnrollment foundStudent = em.find(StudentEnrollment.class, student1.getId());
            System.out.println("\nStudent: " + foundStudent.getStudentName());

            if (foundStudent.getCourse() != null) {
                System.out.println("Enrolled in: " +
                    foundStudent.getCourse().getCourseCode() + " - " +
                    foundStudent.getCourse().getCourseName());
            }

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }

        /*
         * WHAT HAPPENED IN DATABASE:
         *
         * 1. INSERT INTO courses (...) VALUES (...);
         *
         * 2. INSERT INTO student_enrollments (... course_id) VALUES (... course.id);
         * 3. INSERT INTO student_enrollments (... course_id) VALUES (... course.id);
         * 4. INSERT INTO student_enrollments (... course_id) VALUES (... course.id);
         *
         * Notice:
         * - Foreign key (course_id) is in STUDENT table
         * - NOT in COURSE table
         * - This is standard for one-to-many
         *
         * 5. SELECT * FROM courses WHERE id = ...;
         * 6. SELECT * FROM student_enrollments WHERE course_id = ...; (LAZY)
         *
         * REAL-WORLD USE CASE:
         * - Department → Employees
         * - Category → Products
         * - Blog → Posts
         * - Customer → Orders
         * - Parent → Children
         */
    }

    /**
     * MANY-TO-MANY RELATIONSHIP DEMO
     * Student ↔ Courses (Many-to-Many)
     */
    public static void demonstrateManyToMany() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║        MANY-TO-MANY RELATIONSHIP DEMO            ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        EntityManager em = HibernateUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            // ============================================
            // CREATE COURSES
            // ============================================

            System.out.println("\n📚 Creating multiple courses...");

            Course java = new Course("CS101", "Java Programming", 3);
            Course dataStructures = new Course("CS102", "Data Structures", 4);
            Course algorithms = new Course("CS201", "Algorithms", 4);

            em.persist(java);
            em.persist(dataStructures);
            em.persist(algorithms);

            System.out.println("✓ Created 3 courses");

            // ============================================
            // CREATE STUDENTS
            // ============================================

            System.out.println("\n👨‍🎓 Creating students...");

            StudentEnrollment alice = new StudentEnrollment(
                "Alice Johnson",
                "alice2@email.com",
                20
            );

            StudentEnrollment bob = new StudentEnrollment(
                "Bob Smith",
                "bob2@email.com",
                22
            );

            // ============================================
            // MANY-TO-MANY ENROLLMENT
            // ============================================

            System.out.println("\n🔗 Enrolling students in multiple courses...");

            // Alice enrolls in Java and Data Structures
            alice.enrollInCourse(java);
            alice.enrollInCourse(dataStructures);

            // Bob enrolls in all three courses
            bob.enrollInCourse(java);
            bob.enrollInCourse(dataStructures);
            bob.enrollInCourse(algorithms);

            em.persist(alice);
            em.persist(bob);

            System.out.println("✓ Alice enrolled in " + alice.getCourses().size() + " courses");
            System.out.println("✓ Bob enrolled in " + bob.getCourses().size() + " courses");

            em.getTransaction().commit();

            // ============================================
            // READ STUDENT WITH COURSES
            // ============================================

            System.out.println("\n📖 Reading Student enrollments...");

            em.clear();

            StudentEnrollment foundAlice = em.find(StudentEnrollment.class, alice.getId());

            System.out.println("\nStudent: " + foundAlice.getStudentName());
            System.out.println("Total Credits: " + foundAlice.getTotalCredits());
            System.out.println("\nEnrolled Courses:");

            for (Course c : foundAlice.getCourses()) {
                System.out.println("  - " + c.getCourseCode() + ": " +
                                 c.getCourseName() + " (" + c.getCredits() + " credits)");
            }

            // ============================================
            // DROP A COURSE
            // ============================================

            System.out.println("\n📤 Dropping a course...");

            em.getTransaction().begin();

            foundAlice.dropCourse(dataStructures);

            em.getTransaction().commit();

            System.out.println("✓ Alice dropped " + dataStructures.getCourseCode());
            System.out.println("  Remaining courses: " + foundAlice.getCourses().size());

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }

        /*
         * WHAT HAPPENED IN DATABASE:
         *
         * 1. INSERT INTO courses (...) VALUES (...); -- Java
         * 2. INSERT INTO courses (...) VALUES (...); -- Data Structures
         * 3. INSERT INTO courses (...) VALUES (...); -- Algorithms
         *
         * 4. INSERT INTO student_enrollments (...) VALUES (...); -- Alice
         * 5. INSERT INTO student_enrollments (...) VALUES (...); -- Bob
         *
         * 6. INSERT INTO student_course (student_id, course_id) VALUES (alice.id, java.id);
         * 7. INSERT INTO student_course (student_id, course_id) VALUES (alice.id, ds.id);
         * 8. INSERT INTO student_course (student_id, course_id) VALUES (bob.id, java.id);
         * 9. INSERT INTO student_course (student_id, course_id) VALUES (bob.id, ds.id);
         * 10. INSERT INTO student_course (student_id, course_id) VALUES (bob.id, algo.id);
         *
         * JOIN TABLE: student_course
         * ┌────────────┬───────────┐
         * │ student_id │ course_id │
         * ├────────────┼───────────┤
         * │  alice.id  │  java.id  │
         * │  alice.id  │    ds.id  │
         * │   bob.id   │  java.id  │
         * │   bob.id   │    ds.id  │
         * │   bob.id   │  algo.id  │
         * └────────────┴───────────┘
         *
         * 11. DELETE FROM student_course
         *     WHERE student_id = alice.id AND course_id = ds.id;
         *
         * REAL-WORLD USE CASE:
         * - Students ↔ Courses
         * - Users ↔ Roles
         * - Products ↔ Categories
         * - Authors ↔ Books
         * - Tags ↔ Posts
         */
    }

    /**
     * Run all relationship demonstrations
     */
    public static void runAllDemos() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║         JPA RELATIONSHIPS DEMONSTRATIONS         ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        demonstrateOneToOne();
        demonstrateOneToManyManyToOne();
        demonstrateManyToMany();

        System.out.println("\n\n✅ All relationship demonstrations completed!");
        System.out.println("\nSummary:");
        System.out.println("  ✓ One-to-One: Person ↔ Passport");
        System.out.println("  ✓ One-to-Many: Course → Students");
        System.out.println("  ✓ Many-to-One: Student → Course");
        System.out.println("  ✓ Many-to-Many: Student ↔ Courses");
        System.out.println("  ✓ Embedded Objects: Address");
        System.out.println("  ✓ Cascade Operations: PERSIST, MERGE, REMOVE");
        System.out.println("  ✓ Bidirectional Relationships");
    }

    /**
     * Main method
     */
    public static void main(String[] args) {
        try {
            runAllDemos();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
