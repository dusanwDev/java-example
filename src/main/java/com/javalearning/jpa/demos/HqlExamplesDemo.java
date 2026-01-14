package com.javalearning.jpa.demos;

import com.javalearning.jpa.config.HibernateUtil;
import com.javalearning.jpa.embeddable.Address;
import com.javalearning.jpa.relationships.Course;
import com.javalearning.jpa.relationships.StudentEnrollment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;

/**
 * HQL (HIBERNATE QUERY LANGUAGE) COMPLETE EXAMPLES
 *
 * Topics Demonstrated:
 * - Basic HQL syntax
 * - SELECT queries
 * - WHERE clauses
 * - Joins (INNER, LEFT, RIGHT, CROSS)
 * - Aggregation functions
 * - Grouping and HAVING
 * - Subqueries
 * - Named queries
 * - Parameters (positional and named)
 * - Projections and DTOs
 * - Update and Delete queries
 *
 * WHAT IS HQL?
 * HQL (Hibernate Query Language) is an object-oriented query language.
 * Similar to SQL but operates on entity objects instead of tables.
 *
 * HQL vs SQL:
 * - HQL: SELECT s FROM Student s WHERE s.age > 20
 * - SQL: SELECT * FROM students WHERE age > 20
 *
 * HQL vs JPQL:
 * - JPQL: JPA standard (portable)
 * - HQL: Hibernate-specific (more features)
 * - Most JPQL queries work in HQL
 *
 * TYPESCRIPT/ANGULAR COMPARISON:
 * Similar to:
 * - TypeORM QueryBuilder
 * - SQL-like syntax but for objects
 * - LINQ in C#/.NET
 * - Mongoose queries for MongoDB
 *
 * REAL-WORLD USE CASE:
 * Complex queries for:
 * - Reporting and analytics
 * - Search functionality
 * - Data filtering
 * - Business logic queries
 */
public class HqlExamplesDemo {

    /**
     * PART 1: BASIC HQL QUERIES
     */
    public static void demonstrateBasicQueries() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║          BASIC HQL QUERIES                       ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        EntityManager em = HibernateUtil.getEntityManager();

        try {
            // ============================================
            // SETUP: Create test data
            // ============================================

            em.getTransaction().begin();

            for (int i = 1; i <= 10; i++) {
                StudentEnrollment student = new StudentEnrollment(
                    "Student " + i,
                    "student" + i + "@university.edu",
                    18 + i
                );

                if (i <= 5) {
                    Address addr = new Address();
                    addr.setCity("Boston");
                    addr.setState("MA");
                    addr.setCountry("USA");
                    student.setAddress(addr);
                }

                student.setEnrollmentDate(LocalDate.now().minusDays(i * 10));
                em.persist(student);
            }

            em.getTransaction().commit();

            // ============================================
            // 1. SELECT ALL
            // ============================================

            System.out.println("\n1️⃣  SELECT ALL:");

            String hql1 = "FROM StudentEnrollment";
            // Or: "SELECT s FROM StudentEnrollment s"

            TypedQuery<StudentEnrollment> query1 = em.createQuery(hql1, StudentEnrollment.class);
            List<StudentEnrollment> allStudents = query1.getResultList();

            System.out.println("HQL: " + hql1);
            System.out.println("✓ Found " + allStudents.size() + " students");
            System.out.println("SQL: SELECT * FROM student_enrollments");

            // ============================================
            // 2. SELECT WITH ALIAS
            // ============================================

            System.out.println("\n\n2️⃣  SELECT WITH ALIAS:");

            String hql2 = "SELECT s FROM StudentEnrollment s";

            TypedQuery<StudentEnrollment> query2 = em.createQuery(hql2, StudentEnrollment.class);
            List<StudentEnrollment> students = query2.getResultList();

            System.out.println("HQL: " + hql2);
            System.out.println("✓ Found " + students.size() + " students");
            System.out.println("Alias 's' represents StudentEnrollment entity");

            // ============================================
            // 3. SELECT SPECIFIC FIELDS (Projection)
            // ============================================

            System.out.println("\n\n3️⃣  SELECT SPECIFIC FIELDS:");

            String hql3 = "SELECT s.studentName FROM StudentEnrollment s";

            TypedQuery<String> query3 = em.createQuery(hql3, String.class);
            List<String> names = query3.getResultList();

            System.out.println("HQL: " + hql3);
            System.out.println("✓ Found " + names.size() + " names:");
            names.stream().limit(3).forEach(name ->
                System.out.println("  - " + name)
            );
            System.out.println("SQL: SELECT student_name FROM student_enrollments");

            // ============================================
            // 4. SELECT MULTIPLE FIELDS
            // ============================================

            System.out.println("\n\n4️⃣  SELECT MULTIPLE FIELDS:");

            String hql4 = "SELECT s.studentName, s.age FROM StudentEnrollment s";

            Query query4 = em.createQuery(hql4);
            @SuppressWarnings("unchecked")
            List<Object[]> results = query4.getResultList();

            System.out.println("HQL: " + hql4);
            System.out.println("✓ Results (name, age):");
            results.stream().limit(3).forEach(row ->
                System.out.println("  - " + row[0] + ", age " + row[1])
            );
            System.out.println("Returns Object[] for each row");

            // ============================================
            // 5. SELECT DISTINCT
            // ============================================

            System.out.println("\n\n5️⃣  SELECT DISTINCT:");

            String hql5 = "SELECT DISTINCT s.age FROM StudentEnrollment s";

            TypedQuery<Integer> query5 = em.createQuery(hql5, Integer.class);
            List<Integer> distinctAges = query5.getResultList();

            System.out.println("HQL: " + hql5);
            System.out.println("✓ Distinct ages: " + distinctAges);
            System.out.println("SQL: SELECT DISTINCT age FROM student_enrollments");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }

        /*
         * BASIC HQL SYNTAX:
         *
         * FROM Entity              → Select all
         * FROM Entity e            → With alias
         * SELECT e FROM Entity e   → Explicit select
         * SELECT e.field           → Single field
         * SELECT e.f1, e.f2        → Multiple fields
         * SELECT DISTINCT e.field  → Unique values
         *
         * KEY POINTS:
         * - Use entity name (StudentEnrollment), not table name
         * - Use field names (studentName), not column names
         * - Case-sensitive for entity and field names
         * - FROM clause can omit SELECT
         */
    }

    /**
     * PART 2: WHERE CLAUSES AND CONDITIONS
     */
    public static void demonstrateWhereQueries() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║          WHERE CLAUSES AND CONDITIONS            ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        EntityManager em = HibernateUtil.getEntityManager();

        try {
            // ============================================
            // 1. SIMPLE WHERE
            // ============================================

            System.out.println("\n1️⃣  SIMPLE WHERE:");

            String hql1 = "FROM StudentEnrollment s WHERE s.age > 20";

            TypedQuery<StudentEnrollment> query1 = em.createQuery(hql1, StudentEnrollment.class);
            List<StudentEnrollment> students1 = query1.getResultList();

            System.out.println("HQL: " + hql1);
            System.out.println("✓ Found " + students1.size() + " students (age > 20)");

            // ============================================
            // 2. MULTIPLE CONDITIONS (AND, OR)
            // ============================================

            System.out.println("\n\n2️⃣  MULTIPLE CONDITIONS:");

            String hql2 = "FROM StudentEnrollment s WHERE s.age >= 20 AND s.age <= 25";

            TypedQuery<StudentEnrollment> query2 = em.createQuery(hql2, StudentEnrollment.class);
            List<StudentEnrollment> students2 = query2.getResultList();

            System.out.println("HQL: " + hql2);
            System.out.println("✓ Found " + students2.size() + " students (age 20-25)");

            // With OR
            String hql2b = "FROM StudentEnrollment s WHERE s.age < 20 OR s.age > 25";
            TypedQuery<StudentEnrollment> query2b = em.createQuery(hql2b, StudentEnrollment.class);
            List<StudentEnrollment> students2b = query2b.getResultList();

            System.out.println("\nHQL: " + hql2b);
            System.out.println("✓ Found " + students2b.size() + " students (age < 20 OR > 25)");

            // ============================================
            // 3. LIKE OPERATOR
            // ============================================

            System.out.println("\n\n3️⃣  LIKE OPERATOR:");

            String hql3 = "FROM StudentEnrollment s WHERE s.studentEmail LIKE :pattern";

            TypedQuery<StudentEnrollment> query3 = em.createQuery(hql3, StudentEnrollment.class);
            query3.setParameter("pattern", "%university%");
            List<StudentEnrollment> students3 = query3.getResultList();

            System.out.println("HQL: " + hql3);
            System.out.println("Pattern: %university%");
            System.out.println("✓ Found " + students3.size() + " students");
            System.out.println("% = wildcard (0 or more characters)");
            System.out.println("_ = single character wildcard");

            // ============================================
            // 4. BETWEEN OPERATOR
            // ============================================

            System.out.println("\n\n4️⃣  BETWEEN OPERATOR:");

            String hql4 = "FROM StudentEnrollment s WHERE s.age BETWEEN :min AND :max";

            TypedQuery<StudentEnrollment> query4 = em.createQuery(hql4, StudentEnrollment.class);
            query4.setParameter("min", 20);
            query4.setParameter("max", 25);
            List<StudentEnrollment> students4 = query4.getResultList();

            System.out.println("HQL: " + hql4);
            System.out.println("✓ Found " + students4.size() + " students (age BETWEEN 20 AND 25)");

            // ============================================
            // 5. IN OPERATOR
            // ============================================

            System.out.println("\n\n5️⃣  IN OPERATOR:");

            String hql5 = "FROM StudentEnrollment s WHERE s.age IN (20, 22, 24, 26)";

            TypedQuery<StudentEnrollment> query5 = em.createQuery(hql5, StudentEnrollment.class);
            List<StudentEnrollment> students5 = query5.getResultList();

            System.out.println("HQL: " + hql5);
            System.out.println("✓ Found " + students5.size() + " students with specific ages");

            // With parameter
            List<Integer> ages = List.of(19, 21, 23);
            String hql5b = "FROM StudentEnrollment s WHERE s.age IN :ages";
            TypedQuery<StudentEnrollment> query5b = em.createQuery(hql5b, StudentEnrollment.class);
            query5b.setParameter("ages", ages);
            List<StudentEnrollment> students5b = query5b.getResultList();

            System.out.println("\nHQL with parameter: " + hql5b);
            System.out.println("Ages: " + ages);
            System.out.println("✓ Found " + students5b.size() + " students");

            // ============================================
            // 6. IS NULL / IS NOT NULL
            // ============================================

            System.out.println("\n\n6️⃣  NULL CHECKS:");

            String hql6 = "FROM StudentEnrollment s WHERE s.address IS NOT NULL";

            TypedQuery<StudentEnrollment> query6 = em.createQuery(hql6, StudentEnrollment.class);
            List<StudentEnrollment> students6 = query6.getResultList();

            System.out.println("HQL: " + hql6);
            System.out.println("✓ Found " + students6.size() + " students with address");

            // IS NULL
            String hql6b = "FROM StudentEnrollment s WHERE s.address IS NULL";
            TypedQuery<StudentEnrollment> query6b = em.createQuery(hql6b, StudentEnrollment.class);
            List<StudentEnrollment> students6b = query6b.getResultList();

            System.out.println("\nHQL: " + hql6b);
            System.out.println("✓ Found " + students6b.size() + " students without address");

            // ============================================
            // 7. EMBEDDED OBJECT PROPERTIES
            // ============================================

            System.out.println("\n\n7️⃣  QUERY EMBEDDED OBJECT:");

            String hql7 = "FROM StudentEnrollment s WHERE s.address.city = :city";

            TypedQuery<StudentEnrollment> query7 = em.createQuery(hql7, StudentEnrollment.class);
            query7.setParameter("city", "Boston");
            List<StudentEnrollment> students7 = query7.getResultList();

            System.out.println("HQL: " + hql7);
            System.out.println("✓ Found " + students7.size() + " students in Boston");
            System.out.println("Use dot notation for embedded object fields");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        /*
         * WHERE OPERATORS:
         *
         * Comparison:
         * =, !=, <, <=, >, >=
         *
         * Logical:
         * AND, OR, NOT
         *
         * Pattern Matching:
         * LIKE, NOT LIKE
         *
         * Range:
         * BETWEEN ... AND ...
         * IN (...)
         *
         * Null:
         * IS NULL, IS NOT NULL
         *
         * KEY POINTS:
         * - Use field names, not column names
         * - Embedded objects: use dot notation
         * - Parameters: named (:param) or positional (?1)
         */
    }

    /**
     * PART 3: JOINS
     */
    public static void demonstrateJoins() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║          JOIN QUERIES                            ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        EntityManager em = HibernateUtil.getEntityManager();

        try {
            // ============================================
            // SETUP: Create test data with relationships
            // ============================================

            em.getTransaction().begin();

            Course course1 = new Course("CS101", "Java Programming", 3);
            Course course2 = new Course("CS102", "Data Structures", 4);
            Course course3 = new Course("CS201", "Algorithms", 4);

            em.persist(course1);
            em.persist(course2);
            em.persist(course3);

            for (int i = 1; i <= 6; i++) {
                StudentEnrollment student = new StudentEnrollment(
                    "Join Student " + i,
                    "join" + i + "@test.com",
                    20 + i
                );

                // Assign courses
                if (i <= 2) {
                    student.setCourse(course1);
                } else if (i <= 4) {
                    student.setCourse(course2);
                }
                // Students 5-6 have no course

                em.persist(student);
            }

            em.getTransaction().commit();

            // ============================================
            // 1. INNER JOIN
            // ============================================

            System.out.println("\n1️⃣  INNER JOIN:");

            String hql1 = "SELECT s FROM StudentEnrollment s " +
                         "INNER JOIN s.course c " +
                         "WHERE c.credits > 3";

            TypedQuery<StudentEnrollment> query1 = em.createQuery(hql1, StudentEnrollment.class);
            List<StudentEnrollment> students1 = query1.getResultList();

            System.out.println("HQL: " + hql1);
            System.out.println("✓ Found " + students1.size() + " students in courses with > 3 credits");
            System.out.println("INNER JOIN: Only students WITH courses");

            // ============================================
            // 2. LEFT JOIN (LEFT OUTER JOIN)
            // ============================================

            System.out.println("\n\n2️⃣  LEFT JOIN:");

            String hql2 = "SELECT s FROM StudentEnrollment s " +
                         "LEFT JOIN s.course c";

            TypedQuery<StudentEnrollment> query2 = em.createQuery(hql2, StudentEnrollment.class);
            List<StudentEnrollment> students2 = query2.getResultList();

            System.out.println("HQL: " + hql2);
            System.out.println("✓ Found " + students2.size() + " students");
            System.out.println("LEFT JOIN: Students WITH and WITHOUT courses");

            // ============================================
            // 3. JOIN WITH FETCH (Eager loading)
            // ============================================

            System.out.println("\n\n3️⃣  JOIN FETCH:");

            String hql3 = "SELECT DISTINCT s FROM StudentEnrollment s " +
                         "LEFT JOIN FETCH s.course";

            TypedQuery<StudentEnrollment> query3 = em.createQuery(hql3, StudentEnrollment.class);
            List<StudentEnrollment> students3 = query3.getResultList();

            System.out.println("HQL: " + hql3);
            System.out.println("✓ Found " + students3.size() + " students");
            System.out.println("JOIN FETCH: Loads students AND courses in one query");
            System.out.println("Solves N+1 problem!");

            // Access course without additional query
            for (StudentEnrollment s : students3) {
                if (s.getCourse() != null) {
                    System.out.println("  - " + s.getStudentName() +
                                     " → " + s.getCourse().getCourseCode());
                } else {
                    System.out.println("  - " + s.getStudentName() + " → No course");
                }
            }

            // ============================================
            // 4. MULTIPLE JOINS
            // ============================================

            System.out.println("\n\n4️⃣  MULTIPLE JOINS:");

            String hql4 = "SELECT s.studentName, c.courseName " +
                         "FROM StudentEnrollment s " +
                         "INNER JOIN s.course c";

            Query query4 = em.createQuery(hql4);
            @SuppressWarnings("unchecked")
            List<Object[]> results4 = query4.getResultList();

            System.out.println("HQL: " + hql4);
            System.out.println("✓ Results:");
            for (Object[] row : results4) {
                System.out.println("  - " + row[0] + " enrolled in " + row[1]);
            }

            // ============================================
            // 5. JOIN WITH WHERE
            // ============================================

            System.out.println("\n\n5️⃣  JOIN WITH WHERE:");

            String hql5 = "SELECT s FROM StudentEnrollment s " +
                         "JOIN s.course c " +
                         "WHERE c.courseCode = :code";

            TypedQuery<StudentEnrollment> query5 = em.createQuery(hql5, StudentEnrollment.class);
            query5.setParameter("code", "CS101");
            List<StudentEnrollment> students5 = query5.getResultList();

            System.out.println("HQL: " + hql5);
            System.out.println("✓ Found " + students5.size() + " students in CS101");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }

        /*
         * JOIN TYPES:
         *
         * INNER JOIN (JOIN):
         * - Only matching records
         * - Students WITH courses
         *
         * LEFT JOIN (LEFT OUTER JOIN):
         * - All from left side
         * - Students WITH and WITHOUT courses
         *
         * RIGHT JOIN (RIGHT OUTER JOIN):
         * - All from right side
         * - Rarely used in HQL
         *
         * JOIN FETCH:
         * - Eager loading
         * - Loads related entities
         * - Prevents N+1 problem
         * - Use DISTINCT to avoid duplicates
         *
         * KEY POINTS:
         * - Use entity relationships (s.course)
         * - Not table foreign keys
         * - JOIN FETCH for performance
         * - DISTINCT with fetch joins
         */
    }

    /**
     * PART 4: AGGREGATION AND GROUPING
     */
    public static void demonstrateAggregation() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║          AGGREGATION AND GROUPING                ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        EntityManager em = HibernateUtil.getEntityManager();

        try {
            // ============================================
            // 1. COUNT
            // ============================================

            System.out.println("\n1️⃣  COUNT:");

            String hql1 = "SELECT COUNT(s) FROM StudentEnrollment s";

            TypedQuery<Long> query1 = em.createQuery(hql1, Long.class);
            Long count = query1.getSingleResult();

            System.out.println("HQL: " + hql1);
            System.out.println("✓ Total students: " + count);

            // COUNT with WHERE
            String hql1b = "SELECT COUNT(s) FROM StudentEnrollment s WHERE s.age > 22";
            TypedQuery<Long> query1b = em.createQuery(hql1b, Long.class);
            Long count1b = query1b.getSingleResult();

            System.out.println("\nHQL: " + hql1b);
            System.out.println("✓ Students with age > 22: " + count1b);

            // ============================================
            // 2. SUM, AVG, MIN, MAX
            // ============================================

            System.out.println("\n\n2️⃣  AGGREGATE FUNCTIONS:");

            String hql2 = "SELECT " +
                         "SUM(s.age), " +
                         "AVG(s.age), " +
                         "MIN(s.age), " +
                         "MAX(s.age) " +
                         "FROM StudentEnrollment s";

            Query query2 = em.createQuery(hql2);
            Object[] result2 = (Object[]) query2.getSingleResult();

            System.out.println("HQL: " + hql2);
            System.out.println("✓ Sum of ages: " + result2[0]);
            System.out.println("✓ Average age: " + String.format("%.2f", result2[1]));
            System.out.println("✓ Min age: " + result2[2]);
            System.out.println("✓ Max age: " + result2[3]);

            // ============================================
            // 3. GROUP BY
            // ============================================

            System.out.println("\n\n3️⃣  GROUP BY:");

            String hql3 = "SELECT c.courseCode, COUNT(s) " +
                         "FROM StudentEnrollment s " +
                         "JOIN s.course c " +
                         "GROUP BY c.courseCode";

            Query query3 = em.createQuery(hql3);
            @SuppressWarnings("unchecked")
            List<Object[]> results3 = query3.getResultList();

            System.out.println("HQL: " + hql3);
            System.out.println("✓ Students per course:");
            for (Object[] row : results3) {
                System.out.println("  - " + row[0] + ": " + row[1] + " students");
            }

            // ============================================
            // 4. HAVING CLAUSE
            // ============================================

            System.out.println("\n\n4️⃣  HAVING CLAUSE:");

            String hql4 = "SELECT c.courseCode, COUNT(s) " +
                         "FROM StudentEnrollment s " +
                         "JOIN s.course c " +
                         "GROUP BY c.courseCode " +
                         "HAVING COUNT(s) >= 2";

            Query query4 = em.createQuery(hql4);
            @SuppressWarnings("unchecked")
            List<Object[]> results4 = query4.getResultList();

            System.out.println("HQL: " + hql4);
            System.out.println("✓ Courses with >= 2 students:");
            for (Object[] row : results4) {
                System.out.println("  - " + row[0] + ": " + row[1] + " students");
            }
            System.out.println("HAVING filters grouped results");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        /*
         * AGGREGATE FUNCTIONS:
         *
         * COUNT(e)     → Number of entities
         * SUM(e.field) → Sum of values
         * AVG(e.field) → Average of values
         * MIN(e.field) → Minimum value
         * MAX(e.field) → Maximum value
         *
         * GROUP BY:
         * - Groups results by field(s)
         * - Used with aggregate functions
         *
         * HAVING:
         * - Filters grouped results
         * - WHERE filters before grouping
         * - HAVING filters after grouping
         *
         * EXAMPLE:
         * SELECT department, AVG(salary)
         * FROM Employee
         * WHERE active = true        ← Filter before grouping
         * GROUP BY department
         * HAVING AVG(salary) > 50000 ← Filter after grouping
         */
    }

    /**
     * Run all HQL demonstrations
     */
    public static void runAllDemos() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║         HQL (HIBERNATE QUERY LANGUAGE)           ║");
        System.out.println("║              DEMONSTRATIONS                      ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        demonstrateBasicQueries();
        demonstrateWhereQueries();
        demonstrateJoins();
        demonstrateAggregation();

        System.out.println("\n\n✅ All HQL demonstrations completed!");
        System.out.println("\n📚 Summary:");
        System.out.println("  ✓ Basic SELECT queries");
        System.out.println("  ✓ WHERE clauses and conditions");
        System.out.println("  ✓ JOIN operations (INNER, LEFT, FETCH)");
        System.out.println("  ✓ Aggregation (COUNT, SUM, AVG, MIN, MAX)");
        System.out.println("  ✓ GROUP BY and HAVING");

        System.out.println("\n🎯 HQL Best Practices:");
        System.out.println("  1. Use named parameters for security (:param)");
        System.out.println("  2. Use JOIN FETCH to prevent N+1 problem");
        System.out.println("  3. Use projections for better performance");
        System.out.println("  4. Cache frequent queries with @NamedQuery");
        System.out.println("  5. Use TypedQuery for type safety");
        System.out.println("  6. Test queries with explain plan");
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
