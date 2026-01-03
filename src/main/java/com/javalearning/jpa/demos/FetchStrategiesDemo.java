package com.javalearning.jpa.demos;

import com.javalearning.jpa.config.HibernateUtil;
import com.javalearning.jpa.relationships.Course;
import com.javalearning.jpa.relationships.StudentEnrollment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * FETCH STRATEGIES DEMONSTRATION
 *
 * Topics Demonstrated:
 * - EAGER vs LAZY loading
 * - N+1 SELECT problem
 * - JOIN FETCH optimization
 * - @BatchSize optimization
 * - Performance comparison
 * - Best practices
 *
 * WHAT IS FETCHING?
 * Fetching determines WHEN related entities are loaded from the database.
 *
 * TWO STRATEGIES:
 * 1. EAGER (FetchType.EAGER) - Load immediately with parent
 * 2. LAZY (FetchType.LAZY) - Load only when accessed
 *
 * TYPESCRIPT/ANGULAR COMPARISON:
 * Similar to:
 * - Eager loading: Like including 'relations' in TypeORM find()
 * - Lazy loading: Like separate API calls when data is needed
 * - N+1: Like fetching related data in a loop (anti-pattern)
 *
 * REAL-WORLD USE CASE:
 * E-commerce product catalog:
 * - EAGER: Load product with category (always needed)
 * - LAZY: Load reviews only when viewing product details
 */
public class FetchStrategiesDemo {

    /**
     * DEMONSTRATION 1: LAZY LOADING (Default for collections)
     *
     * Collection relationships (@OneToMany, @ManyToMany) are LAZY by default
     */
    public static void demonstrateLazyLoading() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║           LAZY LOADING DEMONSTRATION            ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        EntityManager em = HibernateUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            // ============================================
            // CREATE TEST DATA
            // ============================================

            System.out.println("\n📝 Creating test data...");

            Course course = new Course("CS101", "Java Programming", 3);

            StudentEnrollment student1 = new StudentEnrollment("Alice", "alice@test.com", 20);
            StudentEnrollment student2 = new StudentEnrollment("Bob", "bob@test.com", 21);
            StudentEnrollment student3 = new StudentEnrollment("Charlie", "charlie@test.com", 22);

            student1.setCourse(course);
            student2.setCourse(course);
            student3.setCourse(course);

            em.persist(course);
            em.persist(student1);
            em.persist(student2);
            em.persist(student3);

            em.getTransaction().commit();

            System.out.println("✓ Created course with 3 students");

            // ============================================
            // LAZY LOADING IN ACTION
            // ============================================

            System.out.println("\n📖 Loading course (LAZY loading)...");

            em.clear(); // Clear cache to force DB read

            // Load course
            System.out.println("\nExecuting: em.find(Course.class, courseId)");
            Course foundCourse = em.find(Course.class, course.getId());

            System.out.println("\n✓ Course loaded: " + foundCourse.getCourseName());
            System.out.println("⚠️  Students NOT loaded yet (LAZY)");
            System.out.println("ℹ️  Only ONE SQL query executed so far");

            // Access lazy collection
            System.out.println("\n📖 Now accessing students collection...");
            System.out.println("Executing: course.getEnrollments()");

            List<StudentEnrollment> students = foundCourse.getEnrollments();

            System.out.println("\n✓ Students loaded: " + students.size());
            System.out.println("ℹ️  SECOND SQL query executed now (lazy initialization)");

            System.out.println("\nStudent list:");
            for (StudentEnrollment s : students) {
                System.out.println("  - " + s.getStudentName());
            }

            /*
             * SQL QUERIES EXECUTED:
             *
             * Query 1 (Loading course):
             * SELECT * FROM courses WHERE id = ?
             *
             * Query 2 (Lazy loading students - when accessed):
             * SELECT * FROM student_enrollments WHERE course_id = ?
             *
             * Total: 2 queries
             *
             * KEY POINTS:
             * - Students NOT in first query
             * - Second query only when students.size() called
             * - Saves memory if students never accessed
             * - Must be in active session to work
             */

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }

        /*
         * BENEFITS OF LAZY LOADING:
         * ✅ Saves memory (don't load what you don't need)
         * ✅ Faster initial query
         * ✅ Good for large collections
         *
         * DRAWBACKS:
         * ❌ LazyInitializationException if session closed
         * ❌ Can cause N+1 problem
         * ❌ Requires active session
         */
    }

    /**
     * DEMONSTRATION 2: EAGER LOADING
     *
     * @ManyToOne and @OneToOne are EAGER by default
     */
    public static void demonstrateEagerLoading() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║          EAGER LOADING DEMONSTRATION             ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        EntityManager em = HibernateUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            // ============================================
            // CREATE TEST DATA
            // ============================================

            System.out.println("\n📝 Creating test data...");

            Course course = new Course("CS201", "Data Structures", 4);
            em.persist(course);

            StudentEnrollment student = new StudentEnrollment("David", "david@test.com", 23);
            student.setCourse(course);  // Many-to-One relationship
            em.persist(student);

            em.getTransaction().commit();

            System.out.println("✓ Created student enrolled in course");

            // ============================================
            // EAGER LOADING IN ACTION
            // ============================================

            System.out.println("\n📖 Loading student (EAGER loading)...");

            em.clear();

            // Load student
            System.out.println("\nExecuting: em.find(StudentEnrollment.class, studentId)");
            StudentEnrollment foundStudent = em.find(StudentEnrollment.class, student.getId());

            System.out.println("\n✓ Student loaded: " + foundStudent.getStudentName());
            System.out.println("✓ Course ALSO loaded automatically (EAGER)");
            System.out.println("ℹ️  Single query with JOIN or 2 queries");

            // Access course (already loaded!)
            System.out.println("\n📖 Accessing course...");
            Course foundCourse = foundStudent.getCourse();

            System.out.println("\n✓ Course: " + foundCourse.getCourseName());
            System.out.println("ℹ️  No additional query (already loaded)");

            /*
             * SQL QUERIES EXECUTED:
             *
             * With EAGER (default for @ManyToOne):
             *
             * Option 1 (with JOIN):
             * SELECT s.*, c.*
             * FROM student_enrollments s
             * LEFT JOIN courses c ON s.course_id = c.id
             * WHERE s.id = ?
             *
             * Option 2 (separate queries):
             * SELECT * FROM student_enrollments WHERE id = ?
             * SELECT * FROM courses WHERE id = ?
             *
             * Total: 1-2 queries (course loaded immediately)
             *
             * KEY POINTS:
             * - Course loaded with student
             * - No lazy initialization
             * - Works even after session closed
             */

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }

        /*
         * BENEFITS OF EAGER LOADING:
         * ✅ No LazyInitializationException
         * ✅ Data immediately available
         * ✅ Good for small, always-needed data
         *
         * DRAWBACKS:
         * ❌ Loads unnecessary data
         * ❌ Can cause performance issues
         * ❌ May load too much data
         */
    }

    /**
     * DEMONSTRATION 3: THE N+1 SELECT PROBLEM
     *
     * One of the most common performance problems!
     *
     * PROBLEM:
     * - Load N parent entities (1 query)
     * - Each parent loads its children (N queries)
     * - Total: 1 + N queries = N+1 queries!
     */
    public static void demonstrateNPlusOneProblem() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║          N+1 SELECT PROBLEM DEMO                 ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        EntityManager em = HibernateUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            // ============================================
            // CREATE TEST DATA
            // ============================================

            System.out.println("\n📝 Creating 5 courses with students...");

            for (int i = 1; i <= 5; i++) {
                Course course = new Course("CS" + (100 + i), "Course " + i, 3);
                em.persist(course);

                // Add 3 students per course
                for (int j = 1; j <= 3; j++) {
                    StudentEnrollment student = new StudentEnrollment(
                        "Student " + i + "-" + j,
                        "student" + i + j + "@test.com",
                        20 + j
                    );
                    student.setCourse(course);
                    em.persist(student);
                }
            }

            em.getTransaction().commit();

            System.out.println("✓ Created 5 courses with 15 students total");

            // ============================================
            // DEMONSTRATE N+1 PROBLEM
            // ============================================

            System.out.println("\n⚠️  DEMONSTRATING N+1 PROBLEM:");

            em.clear();

            System.out.println("\n1️⃣  Load all courses:");
            List<Course> courses = em.createQuery(
                "FROM Course", Course.class
            ).getResultList();

            System.out.println("✓ Loaded " + courses.size() + " courses");
            System.out.println("ℹ️  SQL Query 1: SELECT * FROM courses");

            System.out.println("\n2️⃣  Access students for each course:");
            int totalStudents = 0;
            for (Course course : courses) {
                int count = course.getEnrollments().size();
                totalStudents += count;
                System.out.println("  - " + course.getCourseCode() + ": " + count + " students");
                System.out.println("    ℹ️  SQL Query " + (courses.indexOf(course) + 2) +
                                 ": SELECT * FROM students WHERE course_id = " + course.getId());
            }

            System.out.println("\n📊 TOTAL QUERIES:");
            System.out.println("  - 1 query to load courses");
            System.out.println("  - " + courses.size() + " queries to load students (one per course)");
            System.out.println("  - Total: " + (1 + courses.size()) + " queries (N+1 problem!)");
            System.out.println("\n⚠️  THIS IS BAD! Performance degrades with data size.");

            /*
             * THE N+1 PROBLEM EXPLAINED:
             *
             * Query 1: Load courses
             * SELECT * FROM courses;
             * -- Returns 5 courses
             *
             * Query 2: Load students for course 1
             * SELECT * FROM student_enrollments WHERE course_id = 1;
             *
             * Query 3: Load students for course 2
             * SELECT * FROM student_enrollments WHERE course_id = 2;
             *
             * Query 4: Load students for course 3
             * SELECT * FROM student_enrollments WHERE course_id = 3;
             *
             * Query 5: Load students for course 4
             * SELECT * FROM student_enrollments WHERE course_id = 4;
             *
             * Query 6: Load students for course 5
             * SELECT * FROM student_enrollments WHERE course_id = 5;
             *
             * TOTAL: 6 queries (1 + 5)
             *
             * If 100 courses → 101 queries!
             * If 1000 courses → 1001 queries!
             *
             * CATASTROPHIC for performance!
             */

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * DEMONSTRATION 4: SOLUTION 1 - JOIN FETCH
     *
     * Load parent and children in ONE query using JOIN
     */
    public static void demonstrateJoinFetchSolution() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║      SOLUTION 1: JOIN FETCH                      ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        EntityManager em = HibernateUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            // ============================================
            // CREATE TEST DATA (same as N+1 demo)
            // ============================================

            System.out.println("\n📝 Creating 5 courses with students...");

            for (int i = 1; i <= 5; i++) {
                Course course = new Course("CS" + (200 + i), "Advanced Course " + i, 3);
                em.persist(course);

                for (int j = 1; j <= 3; j++) {
                    StudentEnrollment student = new StudentEnrollment(
                        "Student " + i + "-" + j,
                        "advstudent" + i + j + "@test.com",
                        20 + j
                    );
                    student.setCourse(course);
                    em.persist(student);
                }
            }

            em.getTransaction().commit();

            System.out.println("✓ Created 5 courses with 15 students");

            // ============================================
            // SOLUTION: JOIN FETCH
            // ============================================

            System.out.println("\n✅ USING JOIN FETCH:");

            em.clear();

            System.out.println("\n1️⃣  Load courses WITH students (JOIN FETCH):");

            TypedQuery<Course> query = em.createQuery(
                "SELECT DISTINCT c FROM Course c " +
                "LEFT JOIN FETCH c.enrollments " +
                "WHERE c.courseCode LIKE 'CS2%'",
                Course.class
            );

            List<Course> courses = query.getResultList();

            System.out.println("✓ Loaded " + courses.size() + " courses with students");
            System.out.println("ℹ️  SQL: Single query with LEFT JOIN");

            System.out.println("\n2️⃣  Access students (already loaded!):");
            int totalStudents = 0;
            for (Course course : courses) {
                int count = course.getEnrollments().size();
                totalStudents += count;
                System.out.println("  - " + course.getCourseCode() + ": " + count + " students");
                System.out.println("    ℹ️  No additional query (already loaded)");
            }

            System.out.println("\n📊 TOTAL QUERIES:");
            System.out.println("  - 1 query with JOIN to load everything");
            System.out.println("  - Total: 1 query (solved N+1!)");
            System.out.println("\n✅ EXCELLENT! Constant performance regardless of data size.");

            /*
             * SQL QUERY EXECUTED:
             *
             * SELECT DISTINCT c.*, s.*
             * FROM courses c
             * LEFT JOIN student_enrollments s ON c.id = s.course_id
             * WHERE c.course_code LIKE 'CS2%';
             *
             * Returns:
             * - All courses AND their students
             * - Single query
             * - Cartesian product (course repeated for each student)
             *
             * Example result:
             * ┌──────────────┬─────────────┬──────────────┐
             * │ Course Code  │ Course ID   │  Student     │
             * ├──────────────┼─────────────┼──────────────┤
             * │ CS201        │      1      │ Student 1-1  │
             * │ CS201        │      1      │ Student 1-2  │
             * │ CS201        │      1      │ Student 1-3  │
             * │ CS202        │      2      │ Student 2-1  │
             * │ CS202        │      2      │ Student 2-2  │
             * └──────────────┴─────────────┴──────────────┘
             *
             * DISTINCT needed to avoid duplicate courses!
             */

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }

        /*
         * JOIN FETCH BENEFITS:
         * ✅ Single query
         * ✅ Best performance
         * ✅ No N+1 problem
         *
         * JOIN FETCH DRAWBACKS:
         * ❌ Cartesian product (duplicate parent rows)
         * ❌ May transfer more data
         * ❌ Can't paginate easily
         * ❌ Multiple collections cause multiplication
         */
    }

    /**
     * DEMONSTRATION 5: SOLUTION 2 - @BatchSize
     *
     * Load children in batches instead of one-by-one
     * Reduces N+1 to N/BatchSize + 1
     */
    public static void demonstrateBatchSizeSolution() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║      SOLUTION 2: @BatchSize                      ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        System.out.println("\n@BatchSize is configured on the entity:");
        System.out.println("@OneToMany");
        System.out.println("@BatchSize(size = 10)  // Hibernate annotation");
        System.out.println("private List<Student> students;");

        System.out.println("\nHow it works:");
        System.out.println("- Load 50 courses: 1 query");
        System.out.println("- Load students in batches of 10:");
        System.out.println("  SELECT * FROM students WHERE course_id IN (1,2,3,4,5,6,7,8,9,10)");
        System.out.println("  SELECT * FROM students WHERE course_id IN (11,12,13,14,15,16,17,18,19,20)");
        System.out.println("  ... 5 queries total");
        System.out.println("- Total: 1 + 5 = 6 queries");

        System.out.println("\nComparison:");
        System.out.println("┌─────────────┬──────────────┬─────────────┐");
        System.out.println("│  Courses    │  No Batch    │  Batch=10   │");
        System.out.println("├─────────────┼──────────────┼─────────────┤");
        System.out.println("│     10      │   11 queries │  2 queries  │");
        System.out.println("│     50      │   51 queries │  6 queries  │");
        System.out.println("│    100      │  101 queries │ 11 queries  │");
        System.out.println("│   1000      │ 1001 queries │101 queries  │");
        System.out.println("└─────────────┴──────────────┴─────────────┘");

        System.out.println("\n✅ Much better than N+1!");
        System.out.println("⚠️  Not as good as JOIN FETCH, but:");
        System.out.println("   - Works with pagination");
        System.out.println("   - No cartesian product");
        System.out.println("   - Easy to implement (just annotation)");
    }

    /**
     * DEMONSTRATION 6: COMPARISON SUMMARY
     */
    public static void demonstrateComparisonSummary() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         FETCH STRATEGY COMPARISON                ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        System.out.println("\n┌──────────────────────────────────────────────────────────────┐");
        System.out.println("│                  LAZY vs EAGER                               │");
        System.out.println("├─────────────┬────────────────────┬───────────────────────────┤");
        System.out.println("│  Aspect     │       LAZY         │        EAGER              │");
        System.out.println("├─────────────┼────────────────────┼───────────────────────────┤");
        System.out.println("│ When loaded │ On access          │ With parent               │");
        System.out.println("│ Query count │ Separate query     │ Same query or +1          │");
        System.out.println("│ Performance │ Good if not used   │ Good if always needed     │");
        System.out.println("│ Memory      │ Saves memory       │ Uses more memory          │");
        System.out.println("│ Exception   │ LazyInitException  │ None                      │");
        System.out.println("│ Default for │ @OneToMany         │ @ManyToOne, @OneToOne     │");
        System.out.println("│             │ @ManyToMany        │                           │");
        System.out.println("└─────────────┴────────────────────┴───────────────────────────┘");

        System.out.println("\n┌──────────────────────────────────────────────────────────────┐");
        System.out.println("│              N+1 PROBLEM SOLUTIONS                           │");
        System.out.println("├──────────────┬───────────┬─────────────┬────────────────────┤");
        System.out.println("│  Solution    │  Queries  │  Best For   │      Trade-off     │");
        System.out.println("├──────────────┼───────────┼─────────────┼────────────────────┤");
        System.out.println("│ JOIN FETCH   │     1     │ Small data  │ Cartesian product  │");
        System.out.println("│              │           │ Always need │ No pagination      │");
        System.out.println("├──────────────┼───────────┼─────────────┼────────────────────┤");
        System.out.println("│ @BatchSize   │  N/Batch  │ Large data  │ Multiple queries   │");
        System.out.println("│              │    + 1    │ Pagination  │ Still some queries │");
        System.out.println("├──────────────┼───────────┼─────────────┼────────────────────┤");
        System.out.println("│ SUBSELECT    │     2     │ Medium data │ Subquery overhead  │");
        System.out.println("│ (@Fetch)     │           │ All at once │ Database-dependent │");
        System.out.println("├──────────────┼───────────┼─────────────┼────────────────────┤");
        System.out.println("│ EAGER        │    1-2    │ Small,      │ Loads unnecessary  │");
        System.out.println("│              │           │ always need │ Can cascade        │");
        System.out.println("└──────────────┴───────────┴─────────────┴────────────────────┘");

        System.out.println("\n📚 BEST PRACTICES:");
        System.out.println("1. ✅ Default to LAZY for collections");
        System.out.println("2. ✅ Use JOIN FETCH for queries that always need children");
        System.out.println("3. ✅ Use @BatchSize for pagination with related data");
        System.out.println("4. ✅ Monitor SQL queries (hibernate.show_sql=true)");
        System.out.println("5. ✅ Test with realistic data volumes");
        System.out.println("6. ❌ Avoid EAGER for large collections");
        System.out.println("7. ❌ Never use EAGER on multiple collections");
        System.out.println("8. ❌ Don't ignore the N+1 problem");

        System.out.println("\n🎯 DECISION TREE:");
        System.out.println("Do you always need the related data?");
        System.out.println("  ├─ YES, small data → Use EAGER or JOIN FETCH");
        System.out.println("  └─ NO or large data → Use LAZY");
        System.out.println("     └─ Getting N+1 problem?");
        System.out.println("        ├─ Need pagination → Use @BatchSize");
        System.out.println("        ├─ Small dataset → Use JOIN FETCH");
        System.out.println("        └─ Medium dataset → Use @BatchSize or SUBSELECT");
    }

    /**
     * DEMONSTRATION 7: REAL-WORLD EXAMPLES
     */
    public static void demonstrateRealWorldExamples() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         REAL-WORLD USE CASES                     ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        System.out.println("\n📦 E-COMMERCE EXAMPLE:");
        System.out.println("```java");
        System.out.println("@Entity");
        System.out.println("public class Product {");
        System.out.println("    @ManyToOne(fetch = LAZY)    // ✅ Lazy: category loaded on demand");
        System.out.println("    private Category category;");
        System.out.println("");
        System.out.println("    @OneToMany(fetch = LAZY)    // ✅ Lazy: reviews only on detail page");
        System.out.println("    @BatchSize(size = 20)       // ✅ Batch if loading multiple products");
        System.out.println("    private List<Review> reviews;");
        System.out.println("");
        System.out.println("    @OneToMany(fetch = EAGER)   // ❌ Bad: loads all images always");
        System.out.println("    private List<ProductImage> images;");
        System.out.println("}");
        System.out.println("```");

        System.out.println("\n📝 BLOG EXAMPLE:");
        System.out.println("```java");
        System.out.println("@Entity");
        System.out.println("public class BlogPost {");
        System.out.println("    @ManyToOne(fetch = EAGER)   // ✅ Eager: always show author");
        System.out.println("    private Author author;");
        System.out.println("");
        System.out.println("    @OneToMany(fetch = LAZY)    // ✅ Lazy: comments only when viewing");
        System.out.println("    private List<Comment> comments;");
        System.out.println("");
        System.out.println("    @ManyToMany(fetch = LAZY)   // ✅ Lazy: tags loaded on demand");
        System.out.println("    @BatchSize(size = 50)");
        System.out.println("    private Set<Tag> tags;");
        System.out.println("}");
        System.out.println("");
        System.out.println("// List posts with authors:");
        System.out.println("SELECT p FROM BlogPost p");
        System.out.println("JOIN FETCH p.author           // ✅ Eager fetch author");
        System.out.println("WHERE p.published = true");
        System.out.println("```");

        System.out.println("\n👥 SOCIAL NETWORK EXAMPLE:");
        System.out.println("```java");
        System.out.println("@Entity");
        System.out.println("public class User {");
        System.out.println("    @OneToOne(fetch = EAGER)    // ✅ Eager: profile always shown");
        System.out.println("    private UserProfile profile;");
        System.out.println("");
        System.out.println("    @OneToMany(fetch = LAZY)    // ✅ Lazy: posts only on profile page");
        System.out.println("    @BatchSize(size = 10)");
        System.out.println("    private List<Post> posts;");
        System.out.println("");
        System.out.println("    @ManyToMany(fetch = LAZY)   // ✅ Lazy: don't load all friends");
        System.out.println("    private Set<User> friends;   // Could be thousands!");
        System.out.println("}");
        System.out.println("```");

        System.out.println("\n🏢 ENTERPRISE EXAMPLE:");
        System.out.println("```java");
        System.out.println("@Entity");
        System.out.println("public class Invoice {");
        System.out.println("    @ManyToOne(fetch = LAZY)    // ✅ Lazy: customer loaded if needed");
        System.out.println("    private Customer customer;");
        System.out.println("");
        System.out.println("    @OneToMany(fetch = LAZY)    // ✅ Lazy: line items on detail view");
        System.out.println("    @BatchSize(size = 25)");
        System.out.println("    private List<InvoiceLineItem> items;");
        System.out.println("");
        System.out.println("    @OneToOne(fetch = LAZY)     // ✅ Lazy: payment only if exists");
        System.out.println("    private Payment payment;");
        System.out.println("}");
        System.out.println("");
        System.out.println("// Dashboard query - show invoices with totals:");
        System.out.println("SELECT i FROM Invoice i");
        System.out.println("WHERE i.date >= :startDate");
        System.out.println("// Items loaded lazily only when needed");
        System.out.println("");
        System.out.println("// Invoice detail - need everything:");
        System.out.println("SELECT i FROM Invoice i");
        System.out.println("LEFT JOIN FETCH i.items       // ✅ Fetch items");
        System.out.println("LEFT JOIN FETCH i.customer    // ✅ Fetch customer");
        System.out.println("LEFT JOIN FETCH i.payment     // ✅ Fetch payment");
        System.out.println("WHERE i.id = :id");
        System.out.println("```");
    }

    /**
     * Run all fetch strategy demonstrations
     */
    public static void runAllDemos() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║      FETCH STRATEGIES DEMONSTRATIONS             ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        demonstrateLazyLoading();
        demonstrateEagerLoading();
        demonstrateNPlusOneProblem();
        demonstrateJoinFetchSolution();
        demonstrateBatchSizeSolution();
        demonstrateComparisonSummary();
        demonstrateRealWorldExamples();

        System.out.println("\n\n✅ All fetch strategy demonstrations completed!");
        System.out.println("\n📚 Summary:");
        System.out.println("  ✓ Lazy Loading (FetchType.LAZY)");
        System.out.println("  ✓ Eager Loading (FetchType.EAGER)");
        System.out.println("  ✓ N+1 SELECT Problem");
        System.out.println("  ✓ JOIN FETCH Solution");
        System.out.println("  ✓ @BatchSize Solution");
        System.out.println("  ✓ Performance Comparison");
        System.out.println("  ✓ Real-World Examples");
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
