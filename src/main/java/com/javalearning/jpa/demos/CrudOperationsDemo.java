package com.javalearning.jpa.demos;

import com.javalearning.jpa.config.HibernateUtil;
import com.javalearning.jpa.embeddable.Address;
import com.javalearning.jpa.entities.Student;
import com.javalearning.jpa.relationships.Course;
import com.javalearning.jpa.relationships.StudentEnrollment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;

/**
 * CRUD OPERATIONS DEMONSTRATION
 *
 * Topics Demonstrated:
 * - CREATE (persist)
 * - READ (find, getReference, queries)
 * - UPDATE (merge, direct modification)
 * - DELETE (remove, bulk delete)
 * - Transactions
 * - Entity states
 *
 * WHAT IS CRUD?
 * Create, Read, Update, Delete - fundamental database operations
 *
 * JPA/HIBERNATE CRUD METHODS:
 * - persist()    → INSERT
 * - find()       → SELECT by primary key
 * - merge()      → UPDATE
 * - remove()     → DELETE
 *
 * TYPESCRIPT/ANGULAR COMPARISON:
 * Similar to:
 * - TypeORM: save(), find(), update(), delete()
 * - HTTP: POST, GET, PUT/PATCH, DELETE
 * - NgRx: CREATE, SELECT, UPDATE, DELETE actions
 *
 * REAL-WORLD USE CASE:
 * Every application needs CRUD operations:
 * - User management
 * - Product catalog
 * - Blog posts
 * - Customer records
 */
public class CrudOperationsDemo {

    /**
     * CREATE OPERATIONS - persist()
     *
     * Saves new entity to database (INSERT)
     */
    public static void demonstrateCreate() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║          CREATE OPERATIONS (INSERT)              ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        EntityManager em = HibernateUtil.getEntityManager();

        try {
            // ============================================
            // SIMPLE CREATE
            // ============================================

            System.out.println("\n1️⃣  SIMPLE CREATE:");

            em.getTransaction().begin();

            // Create new entity
            Student student = new Student();
            student.setName("John Doe");
            student.setEmail("john.doe@university.edu");
            student.setAge(20);

            System.out.println("Before persist:");
            System.out.println("  ID: " + student.getId());  // null
            System.out.println("  State: Transient (not tracked by EntityManager)");

            // Save to database
            em.persist(student);

            System.out.println("\nAfter persist:");
            System.out.println("  ID: " + student.getId());  // assigned by DB
            System.out.println("  State: Managed (tracked by EntityManager)");
            System.out.println("  SQL: INSERT INTO students (...) VALUES (...)");

            em.getTransaction().commit();

            System.out.println("\nAfter commit:");
            System.out.println("  ✓ Entity saved to database");
            System.out.println("  ID: " + student.getId());

            // ============================================
            // CREATE WITH EMBEDDED OBJECT
            // ============================================

            System.out.println("\n\n2️⃣  CREATE WITH EMBEDDED OBJECT:");

            em.getTransaction().begin();

            Student studentWithAddress = new Student();
            studentWithAddress.setName("Jane Smith");
            studentWithAddress.setEmail("jane.smith@university.edu");
            studentWithAddress.setAge(21);

            // Set embedded address
            Address address = new Address();
            address.setStreet("123 Main St");
            address.setCity("Boston");
            address.setState("MA");
            address.setZipCode("02101");
            address.setCountry("USA");

            studentWithAddress.setAddress(address);

            em.persist(studentWithAddress);

            System.out.println("✓ Student with address saved");
            System.out.println("  Address fields saved in same table");
            System.out.println("  No separate address table");

            em.getTransaction().commit();

            // ============================================
            // CREATE WITH RELATIONSHIPS (CASCADE)
            // ============================================

            System.out.println("\n\n3️⃣  CREATE WITH RELATIONSHIPS:");

            em.getTransaction().begin();

            // Create course
            Course course = new Course("CS101", "Introduction to Java", 3);

            // Create student enrolled in course
            StudentEnrollment enrollment = new StudentEnrollment(
                "Bob Johnson",
                "bob@university.edu",
                22
            );
            enrollment.setCourse(course);

            // Save both (course persisted due to cascade)
            em.persist(course);      // Save course first
            em.persist(enrollment);  // Save student with reference

            System.out.println("✓ Course saved: " + course.getCourseCode());
            System.out.println("✓ Student enrolled: " + enrollment.getStudentName());
            System.out.println("  Foreign key set automatically");

            em.getTransaction().commit();

            // ============================================
            // BATCH CREATE
            // ============================================

            System.out.println("\n\n4️⃣  BATCH CREATE (Multiple entities):");

            em.getTransaction().begin();

            System.out.println("Creating 10 students...");

            for (int i = 1; i <= 10; i++) {
                Student s = new Student();
                s.setName("Student " + i);
                s.setEmail("student" + i + "@university.edu");
                s.setAge(18 + i);
                em.persist(s);

                // Flush every 5 entities
                if (i % 5 == 0) {
                    em.flush();     // Execute SQL
                    em.clear();     // Clear cache
                    System.out.println("  Flushed batch of 5 students");
                }
            }

            em.getTransaction().commit();

            System.out.println("✓ All 10 students created");
            System.out.println("  Used batching for better performance");

            /*
             * SQL GENERATED:
             *
             * Simple Create:
             * INSERT INTO students (name, email, age)
             * VALUES ('John Doe', 'john.doe@...', 20);
             *
             * With Embedded:
             * INSERT INTO students (name, email, age, street, city, state, zip_code, country)
             * VALUES ('Jane Smith', 'jane@...', 21, '123 Main St', 'Boston', ...);
             *
             * With Relationships:
             * INSERT INTO courses (course_code, course_name, credits)
             * VALUES ('CS101', 'Introduction to Java', 3);
             *
             * INSERT INTO student_enrollments (student_name, email, age, course_id)
             * VALUES ('Bob Johnson', 'bob@...', 22, 1);
             */

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                System.out.println("❌ Transaction rolled back");
            }
            e.printStackTrace();
        } finally {
            em.close();
        }

        /*
         * CREATE BEST PRACTICES:
         * ✅ Always use transactions
         * ✅ Validate data before persist
         * ✅ Use batch processing for multiple entities
         * ✅ flush() and clear() for large batches
         * ❌ Don't persist in loops without batching
         * ❌ Don't forget to commit
         */
    }

    /**
     * READ OPERATIONS - find(), queries
     *
     * Retrieves data from database (SELECT)
     */
    public static void demonstrateRead() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║          READ OPERATIONS (SELECT)                ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        EntityManager em = HibernateUtil.getEntityManager();

        try {
            // ============================================
            // SETUP: Create test data
            // ============================================

            em.getTransaction().begin();

            for (int i = 1; i <= 5; i++) {
                Student s = new Student();
                s.setName("Reader Student " + i);
                s.setEmail("reader" + i + "@test.com");
                s.setAge(20 + i);
                em.persist(s);
            }

            em.getTransaction().commit();
            em.clear();

            // ============================================
            // 1. FIND BY PRIMARY KEY
            // ============================================

            System.out.println("\n1️⃣  FIND BY PRIMARY KEY:");

            Student student = em.find(Student.class, 1L);

            if (student != null) {
                System.out.println("✓ Found: " + student.getName());
                System.out.println("  SQL: SELECT * FROM students WHERE id = 1");
            } else {
                System.out.println("❌ Student not found");
            }

            // Try finding non-existent
            Student notFound = em.find(Student.class, 9999L);
            System.out.println("\nFind non-existent ID:");
            System.out.println("  Result: " + (notFound == null ? "null" : "found"));

            // ============================================
            // 2. GET ALL (JPQL)
            // ============================================

            System.out.println("\n\n2️⃣  GET ALL ENTITIES:");

            TypedQuery<Student> query = em.createQuery(
                "SELECT s FROM Student s WHERE s.email LIKE :pattern",
                Student.class
            );
            query.setParameter("pattern", "reader%");

            List<Student> students = query.getResultList();

            System.out.println("✓ Found " + students.size() + " students");
            System.out.println("  SQL: SELECT * FROM students WHERE email LIKE 'reader%'");

            for (Student s : students) {
                System.out.println("  - " + s.getName() + " (age: " + s.getAge() + ")");
            }

            // ============================================
            // 3. SINGLE RESULT
            // ============================================

            System.out.println("\n\n3️⃣  GET SINGLE RESULT:");

            TypedQuery<Student> singleQuery = em.createQuery(
                "SELECT s FROM Student s WHERE s.email = :email",
                Student.class
            );
            singleQuery.setParameter("email", "reader1@test.com");

            try {
                Student single = singleQuery.getSingleResult();
                System.out.println("✓ Found: " + single.getName());
            } catch (Exception e) {
                System.out.println("❌ No result or multiple results");
            }

            // ============================================
            // 4. PAGINATION
            // ============================================

            System.out.println("\n\n4️⃣  PAGINATION:");

            TypedQuery<Student> pageQuery = em.createQuery(
                "SELECT s FROM Student s WHERE s.email LIKE :pattern ORDER BY s.name",
                Student.class
            );
            pageQuery.setParameter("pattern", "reader%");
            pageQuery.setFirstResult(0);   // Offset (start at 0)
            pageQuery.setMaxResults(3);     // Limit (3 per page)

            List<Student> page1 = pageQuery.getResultList();

            System.out.println("Page 1 (3 items):");
            for (Student s : page1) {
                System.out.println("  - " + s.getName());
            }
            System.out.println("  SQL: SELECT * FROM students ... LIMIT 3 OFFSET 0");

            // Page 2
            pageQuery.setFirstResult(3);
            List<Student> page2 = pageQuery.getResultList();

            System.out.println("\nPage 2 (remaining items):");
            for (Student s : page2) {
                System.out.println("  - " + s.getName());
            }
            System.out.println("  SQL: SELECT * FROM students ... LIMIT 3 OFFSET 3");

            // ============================================
            // 5. COUNT
            // ============================================

            System.out.println("\n\n5️⃣  COUNT:");

            TypedQuery<Long> countQuery = em.createQuery(
                "SELECT COUNT(s) FROM Student s WHERE s.email LIKE :pattern",
                Long.class
            );
            countQuery.setParameter("pattern", "reader%");

            Long count = countQuery.getSingleResult();

            System.out.println("✓ Total count: " + count);
            System.out.println("  SQL: SELECT COUNT(*) FROM students WHERE email LIKE 'reader%'");

            // ============================================
            // 6. PROJECTION (Select specific fields)
            // ============================================

            System.out.println("\n\n6️⃣  PROJECTION (Select specific fields):");

            TypedQuery<String> nameQuery = em.createQuery(
                "SELECT s.name FROM Student s WHERE s.email LIKE :pattern",
                String.class
            );
            nameQuery.setParameter("pattern", "reader%");

            List<String> names = nameQuery.getResultList();

            System.out.println("✓ Student names:");
            for (String name : names) {
                System.out.println("  - " + name);
            }
            System.out.println("  SQL: SELECT name FROM students WHERE email LIKE 'reader%'");

            // ============================================
            // 7. NATIVE SQL QUERY
            // ============================================

            System.out.println("\n\n7️⃣  NATIVE SQL QUERY:");

            Query nativeQuery = em.createNativeQuery(
                "SELECT * FROM students WHERE age > ?",
                Student.class
            );
            nativeQuery.setParameter(1, 22);

            @SuppressWarnings("unchecked")
            List<Student> nativeResults = nativeQuery.getResultList();

            System.out.println("✓ Found " + nativeResults.size() + " students (age > 22)");
            System.out.println("  Raw SQL executed directly");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        /*
         * READ METHODS COMPARISON:
         *
         * find(Class, id):
         * - Returns entity or null
         * - Single database hit
         * - Cached (second call is free)
         *
         * getReference(Class, id):
         * - Returns proxy (lazy)
         * - No immediate database hit
         * - Throws exception if not found
         *
         * createQuery():
         * - JPQL (object-oriented)
         * - Portable across databases
         * - Type-safe with TypedQuery
         *
         * createNativeQuery():
         * - Raw SQL
         * - Database-specific
         * - For complex queries
         */
    }

    /**
     * UPDATE OPERATIONS - merge(), direct modification
     *
     * Modifies existing data (UPDATE)
     */
    public static void demonstrateUpdate() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║          UPDATE OPERATIONS (UPDATE)              ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        EntityManager em = HibernateUtil.getEntityManager();

        try {
            // ============================================
            // SETUP: Create test entity
            // ============================================

            em.getTransaction().begin();

            Student student = new Student();
            student.setName("Update Test");
            student.setEmail("update@test.com");
            student.setAge(20);
            em.persist(student);

            em.getTransaction().commit();
            Long studentId = student.getId();

            // ============================================
            // 1. MANAGED ENTITY UPDATE (Auto-sync)
            // ============================================

            System.out.println("\n1️⃣  MANAGED ENTITY UPDATE (Automatic):");

            em.getTransaction().begin();

            // Load entity (becomes managed)
            Student managedStudent = em.find(Student.class, studentId);

            System.out.println("Before: " + managedStudent.getName() + ", age " + managedStudent.getAge());

            // Just modify it - no need for update() call!
            managedStudent.setName("Updated Name");
            managedStudent.setAge(21);

            System.out.println("After:  " + managedStudent.getName() + ", age " + managedStudent.getAge());
            System.out.println("  No explicit update() call needed!");

            em.getTransaction().commit();  // UPDATE executed here

            System.out.println("\n✓ Entity updated automatically");
            System.out.println("  SQL: UPDATE students SET name=?, age=? WHERE id=?");
            System.out.println("  Hibernate detects changes (dirty checking)");

            // ============================================
            // 2. DETACHED ENTITY UPDATE (merge)
            // ============================================

            System.out.println("\n\n2️⃣  DETACHED ENTITY UPDATE (merge):");

            em.clear();  // Detach all entities

            // This student is now detached (not managed)
            Student detachedStudent = new Student();
            detachedStudent.setId(studentId);
            detachedStudent.setName("Merged Name");
            detachedStudent.setEmail("merged@test.com");
            detachedStudent.setAge(22);

            System.out.println("State: Detached (not tracked)");

            em.getTransaction().begin();

            // Merge brings it back to managed state
            Student mergedStudent = em.merge(detachedStudent);

            System.out.println("State: Managed (tracked again)");
            System.out.println("✓ Changes will be saved on commit");

            em.getTransaction().commit();

            System.out.println("\n✓ Entity merged and updated");
            System.out.println("  SQL: UPDATE students SET name=?, email=?, age=? WHERE id=?");

            // ============================================
            // 3. PARTIAL UPDATE
            // ============================================

            System.out.println("\n\n3️⃣  PARTIAL UPDATE (Only changed fields):");

            em.getTransaction().begin();

            Student partialStudent = em.find(Student.class, studentId);

            System.out.println("Before: " + partialStudent.getEmail());

            // Update only one field
            partialStudent.setEmail("partial@test.com");
            // Other fields unchanged

            em.getTransaction().commit();

            System.out.println("After:  " + partialStudent.getEmail());
            System.out.println("\n✓ Only modified field updated");
            System.out.println("  SQL: UPDATE students SET email=? WHERE id=?");
            System.out.println("  (Hibernate tracks which fields changed)");

            // ============================================
            // 4. BULK UPDATE (JPQL)
            // ============================================

            System.out.println("\n\n4️⃣  BULK UPDATE (Multiple entities):");

            // Create more test data
            em.getTransaction().begin();

            for (int i = 1; i <= 5; i++) {
                Student s = new Student();
                s.setName("Bulk Student " + i);
                s.setEmail("bulk" + i + "@test.com");
                s.setAge(20);
                em.persist(s);
            }

            em.getTransaction().commit();

            // Bulk update
            em.getTransaction().begin();

            Query updateQuery = em.createQuery(
                "UPDATE Student s SET s.age = s.age + 1 " +
                "WHERE s.email LIKE :pattern"
            );
            updateQuery.setParameter("pattern", "bulk%");

            int updatedCount = updateQuery.executeUpdate();

            em.getTransaction().commit();

            System.out.println("✓ Updated " + updatedCount + " students");
            System.out.println("  SQL: UPDATE students SET age = age + 1 WHERE email LIKE 'bulk%'");
            System.out.println("  Single query for multiple entities");

            // ============================================
            // 5. UPDATE WITH OPTIMISTIC LOCKING
            // ============================================

            System.out.println("\n\n5️⃣  UPDATE WITH VERSION CHECK:");

            System.out.println("When using @Version:");
            System.out.println("  @Version");
            System.out.println("  private Long version;");
            System.out.println("");
            System.out.println("Update SQL becomes:");
            System.out.println("  UPDATE students SET name=?, version=version+1");
            System.out.println("  WHERE id=? AND version=?");
            System.out.println("");
            System.out.println("If version doesn't match:");
            System.out.println("  → OptimisticLockException thrown");
            System.out.println("  → Prevents lost updates");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }

        /*
         * UPDATE PATTERNS:
         *
         * 1. MANAGED ENTITY (Best for single entities):
         *    Student s = em.find(Student.class, id);
         *    s.setName("New Name");
         *    // Auto-updated on commit
         *
         * 2. MERGE (For detached entities):
         *    Student s = getFromCache();  // Detached
         *    em.merge(s);  // Merge into managed state
         *
         * 3. BULK UPDATE (Best for many entities):
         *    em.createQuery("UPDATE Student s SET s.age = ...").executeUpdate();
         *
         * DIRTY CHECKING:
         * - Hibernate tracks changes to managed entities
         * - Compares current state with loaded state
         * - Only updates changed fields (with @DynamicUpdate)
         */
    }

    /**
     * DELETE OPERATIONS - remove(), bulk delete
     *
     * Removes data from database (DELETE)
     */
    public static void demonstrateDelete() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║          DELETE OPERATIONS (DELETE)              ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        EntityManager em = HibernateUtil.getEntityManager();

        try {
            // ============================================
            // SETUP: Create test data
            // ============================================

            em.getTransaction().begin();

            for (int i = 1; i <= 10; i++) {
                Student s = new Student();
                s.setName("Delete Student " + i);
                s.setEmail("delete" + i + "@test.com");
                s.setAge(20 + i);
                em.persist(s);
            }

            em.getTransaction().commit();
            em.clear();

            // ============================================
            // 1. SIMPLE DELETE
            // ============================================

            System.out.println("\n1️⃣  SIMPLE DELETE:");

            em.getTransaction().begin();

            // Load entity
            Student student = em.find(Student.class, 1L);

            if (student != null) {
                System.out.println("Found: " + student.getName());

                // Remove entity
                em.remove(student);

                System.out.println("✓ Entity marked for deletion");
            }

            em.getTransaction().commit();  // DELETE executed here

            System.out.println("\n✓ Entity deleted");
            System.out.println("  SQL: DELETE FROM students WHERE id = 1");

            // Verify deletion
            Student deleted = em.find(Student.class, 1L);
            System.out.println("  Verification: " + (deleted == null ? "Not found (deleted)" : "Still exists"));

            // ============================================
            // 2. DELETE DETACHED ENTITY
            // ============================================

            System.out.println("\n\n2️⃣  DELETE DETACHED ENTITY:");

            // Get entity and detach
            Student toDelete = em.find(Student.class, 2L);
            em.clear();  // Now detached

            System.out.println("State: Detached");

            em.getTransaction().begin();

            // Can't remove detached entity directly!
            // Must merge first or find again

            // Option 1: Merge then remove
            Student managed = em.merge(toDelete);
            em.remove(managed);

            // Option 2: Find then remove
            // Student managed = em.find(Student.class, 2L);
            // em.remove(managed);

            em.getTransaction().commit();

            System.out.println("✓ Detached entity deleted");
            System.out.println("  Must merge or find before removing");

            // ============================================
            // 3. DELETE BY ID (Without loading)
            // ============================================

            System.out.println("\n\n3️⃣  DELETE BY ID (Without loading entity):");

            em.getTransaction().begin();

            // Get reference (proxy) without loading
            Student reference = em.getReference(Student.class, 3L);

            // Remove reference (no SELECT needed)
            em.remove(reference);

            em.getTransaction().commit();

            System.out.println("✓ Deleted without loading entity");
            System.out.println("  No SELECT query");
            System.out.println("  Only DELETE query executed");
            System.out.println("  More efficient for delete-by-ID");

            // ============================================
            // 4. BULK DELETE (JPQL)
            // ============================================

            System.out.println("\n\n4️⃣  BULK DELETE:");

            em.getTransaction().begin();

            Query deleteQuery = em.createQuery(
                "DELETE FROM Student s WHERE s.age > :age"
            );
            deleteQuery.setParameter("age", 25);

            int deletedCount = deleteQuery.executeUpdate();

            em.getTransaction().commit();

            System.out.println("✓ Deleted " + deletedCount + " students");
            System.out.println("  SQL: DELETE FROM students WHERE age > 25");
            System.out.println("  Single query for multiple entities");

            // ============================================
            // 5. SOFT DELETE (Logical delete)
            // ============================================

            System.out.println("\n\n5️⃣  SOFT DELETE (Logical delete):");

            System.out.println("Using @Where annotation:");
            System.out.println("  @Entity");
            System.out.println("  @Where(clause = \"deleted = false\")");
            System.out.println("  public class Student {");
            System.out.println("      private Boolean deleted = false;");
            System.out.println("  }");
            System.out.println("");
            System.out.println("Soft delete:");
            System.out.println("  student.setDeleted(true);");
            System.out.println("  em.merge(student);");
            System.out.println("");
            System.out.println("SQL:");
            System.out.println("  UPDATE students SET deleted = true WHERE id = ?");
            System.out.println("");
            System.out.println("Benefits:");
            System.out.println("  ✅ Data not actually deleted");
            System.out.println("  ✅ Can restore later");
            System.out.println("  ✅ Audit trail preserved");
            System.out.println("  ✅ @Where filters out deleted records automatically");

            // ============================================
            // 6. CASCADE DELETE
            // ============================================

            System.out.println("\n\n6️⃣  CASCADE DELETE:");

            System.out.println("With cascade:");
            System.out.println("  @OneToMany(cascade = CascadeType.REMOVE)");
            System.out.println("  private List<Enrollment> enrollments;");
            System.out.println("");
            System.out.println("When deleting student:");
            System.out.println("  em.remove(student);");
            System.out.println("");
            System.out.println("SQL:");
            System.out.println("  DELETE FROM enrollments WHERE student_id = ?");
            System.out.println("  DELETE FROM students WHERE id = ?");
            System.out.println("");
            System.out.println("⚠️  WARNING:");
            System.out.println("  Be careful with cascade delete!");
            System.out.println("  Can accidentally delete related data");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }

        /*
         * DELETE PATTERNS:
         *
         * 1. MANAGED ENTITY:
         *    Student s = em.find(Student.class, id);
         *    em.remove(s);
         *    // One SELECT, one DELETE
         *
         * 2. BY REFERENCE (No loading):
         *    Student s = em.getReference(Student.class, id);
         *    em.remove(s);
         *    // Only one DELETE (faster)
         *
         * 3. BULK DELETE:
         *    em.createQuery("DELETE FROM Student s WHERE ...").executeUpdate();
         *    // Single DELETE for many entities
         *
         * 4. SOFT DELETE:
         *    student.setDeleted(true);
         *    // UPDATE instead of DELETE
         *
         * IMPORTANT:
         * - remove() only works on managed entities
         * - Bulk delete bypasses cascade rules
         * - Consider soft delete for important data
         */
    }

    /**
     * TRANSACTION MANAGEMENT
     *
     * All CRUD operations should be in transactions
     */
    public static void demonstrateTransactions() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║          TRANSACTION MANAGEMENT                  ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        EntityManager em = HibernateUtil.getEntityManager();

        try {
            System.out.println("\n✅ PROPER TRANSACTION PATTERN:");
            System.out.println("```java");
            System.out.println("EntityManager em = emf.createEntityManager();");
            System.out.println("try {");
            System.out.println("    em.getTransaction().begin();");
            System.out.println("    ");
            System.out.println("    // Your CRUD operations here");
            System.out.println("    Student s = new Student();");
            System.out.println("    em.persist(s);");
            System.out.println("    ");
            System.out.println("    em.getTransaction().commit();  // Success");
            System.out.println("} catch (Exception e) {");
            System.out.println("    if (em.getTransaction().isActive()) {");
            System.out.println("        em.getTransaction().rollback();  // Failure");
            System.out.println("    }");
            System.out.println("    e.printStackTrace();");
            System.out.println("} finally {");
            System.out.println("    em.close();");
            System.out.println("}");
            System.out.println("```");

            System.out.println("\n📋 TRANSACTION STATES:");
            System.out.println("1. begin()     → Transaction started");
            System.out.println("2. Operations  → Changes tracked");
            System.out.println("3. commit()    → Changes persisted to DB");
            System.out.println("4. rollback()  → Changes discarded");

            System.out.println("\n⚠️  COMMON MISTAKES:");
            System.out.println("❌ Forgetting to begin transaction");
            System.out.println("❌ Forgetting to commit");
            System.out.println("❌ Not rolling back on error");
            System.out.println("❌ Not closing EntityManager");
            System.out.println("❌ Committing too frequently (performance)");
            System.out.println("❌ Transactions too large (locks)");

            System.out.println("\n✅ BEST PRACTICES:");
            System.out.println("1. Keep transactions short");
            System.out.println("2. Always rollback on error");
            System.out.println("3. Use try-finally to close EntityManager");
            System.out.println("4. Batch operations when possible");
            System.out.println("5. Don't hold transactions during user input");

        } finally {
            em.close();
        }
    }

    /**
     * SUMMARY AND COMPARISON
     */
    public static void demonstrateSummary() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║          CRUD OPERATIONS SUMMARY                 ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        System.out.println("\n┌────────────────────────────────────────────────────────────────┐");
        System.out.println("│                    JPA CRUD METHODS                            │");
        System.out.println("├──────────┬─────────────┬──────────────┬────────────────────────┤");
        System.out.println("│ Operation│  JPA Method │     SQL      │      When to Use       │");
        System.out.println("├──────────┼─────────────┼──────────────┼────────────────────────┤");
        System.out.println("│ CREATE   │ persist()   │   INSERT     │ New entities           │");
        System.out.println("│          │ merge()     │ INSERT/UPDATE│ Detached entities      │");
        System.out.println("├──────────┼─────────────┼──────────────┼────────────────────────┤");
        System.out.println("│ READ     │ find()      │   SELECT     │ By primary key         │");
        System.out.println("│          │ createQuery │   SELECT     │ Custom criteria        │");
        System.out.println("│          │ getReference│   (lazy)     │ Delete without loading │");
        System.out.println("├──────────┼─────────────┼──────────────┼────────────────────────┤");
        System.out.println("│ UPDATE   │ (auto)      │   UPDATE     │ Managed entities       │");
        System.out.println("│          │ merge()     │   UPDATE     │ Detached entities      │");
        System.out.println("│          │ bulk update │   UPDATE     │ Many entities          │");
        System.out.println("├──────────┼─────────────┼──────────────┼────────────────────────┤");
        System.out.println("│ DELETE   │ remove()    │   DELETE     │ Single entity          │");
        System.out.println("│          │ bulk delete │   DELETE     │ Many entities          │");
        System.out.println("│          │ (soft)      │   UPDATE     │ Logical delete         │");
        System.out.println("└──────────┴─────────────┴──────────────┴────────────────────────┘");

        System.out.println("\n┌────────────────────────────────────────────────────────────────┐");
        System.out.println("│              TYPESCRIPT/TYPEORM COMPARISON                     │");
        System.out.println("├──────────────────────┬─────────────────────────────────────────┤");
        System.out.println("│     JPA/Hibernate    │           TypeORM                       │");
        System.out.println("├──────────────────────┼─────────────────────────────────────────┤");
        System.out.println("│ em.persist(entity)   │ repository.save(entity)                 │");
        System.out.println("│ em.find(Class, id)   │ repository.findOne({ where: { id } })   │");
        System.out.println("│ em.merge(entity)     │ repository.save(entity)                 │");
        System.out.println("│ em.remove(entity)    │ repository.remove(entity)               │");
        System.out.println("│ em.createQuery()     │ repository.find({ where: ... })         │");
        System.out.println("│ em.flush()           │ await queryRunner.commitTransaction()   │");
        System.out.println("└──────────────────────┴─────────────────────────────────────────┘");

        System.out.println("\n🎯 DECISION TREE:");
        System.out.println("");
        System.out.println("Need to save?");
        System.out.println("  ├─ New entity → persist()");
        System.out.println("  └─ Existing/detached → merge()");
        System.out.println("");
        System.out.println("Need to load?");
        System.out.println("  ├─ By ID → find()");
        System.out.println("  ├─ By criteria → createQuery()");
        System.out.println("  └─ For deletion → getReference()");
        System.out.println("");
        System.out.println("Need to update?");
        System.out.println("  ├─ Single entity → find() + modify + commit");
        System.out.println("  ├─ Detached → merge()");
        System.out.println("  └─ Many entities → bulk update");
        System.out.println("");
        System.out.println("Need to delete?");
        System.out.println("  ├─ Single → find() + remove()");
        System.out.println("  ├─ By ID → getReference() + remove()");
        System.out.println("  ├─ Many → bulk delete");
        System.out.println("  └─ Keep data → soft delete");
    }

    /**
     * Run all CRUD demonstrations
     */
    public static void runAllDemos() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║         CRUD OPERATIONS DEMONSTRATIONS           ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        demonstrateCreate();
        demonstrateRead();
        demonstrateUpdate();
        demonstrateDelete();
        demonstrateTransactions();
        demonstrateSummary();

        System.out.println("\n\n✅ All CRUD demonstrations completed!");
        System.out.println("\n📚 Summary:");
        System.out.println("  ✓ CREATE - persist() and merge()");
        System.out.println("  ✓ READ - find(), queries, pagination");
        System.out.println("  ✓ UPDATE - managed entities, merge(), bulk updates");
        System.out.println("  ✓ DELETE - remove(), bulk delete, soft delete");
        System.out.println("  ✓ TRANSACTIONS - begin, commit, rollback");
        System.out.println("  ✓ BEST PRACTICES - patterns and comparisons");
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
