package com.javalearning.jpa.demos;

import com.javalearning.jpa.config.HibernateUtil;
import com.javalearning.jpa.entities.Student;
import jakarta.persistence.EntityManager;

/**
 * ENTITY LIFECYCLE STATES DEMONSTRATION
 *
 * Topics Demonstrated:
 * - Transient state
 * - Persistent (Managed) state
 * - Detached state
 * - Removed state
 * - State transitions
 * - get() vs load()
 *
 * ENTITY STATES:
 * 1. TRANSIENT - New object, not in database, not tracked
 * 2. PERSISTENT - In database, tracked by EntityManager
 * 3. DETACHED - Was persistent, no longer tracked
 * 4. REMOVED - Marked for deletion
 *
 * TYPESCRIPT/ANGULAR COMPARISON:
 * Similar to:
 * - NgRx entity states (loading, loaded, deleted)
 * - Form states (pristine, dirty, touched)
 * - Object lifecycle in memory management
 *
 * REAL-WORLD USE CASE:
 * Understanding states helps with:
 * - Debugging persistence issues
 * - Optimizing performance
 * - Managing detached entities
 * - Handling LazyInitializationException
 */
public class EntityLifecycleDemo {

    public static void demonstrateAllStates() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         ENTITY LIFECYCLE STATES                  ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        EntityManager em = HibernateUtil.getEntityManager();

        try {
            // ============================================
            // STATE 1: TRANSIENT
            // ============================================

            System.out.println("\n1️⃣  TRANSIENT STATE:");
            System.out.println("Creating new entity...");

            Student student = new Student();
            student.setName("Lifecycle Demo");
            student.setEmail("lifecycle@test.com");
            student.setAge(20);

            System.out.println("✓ Entity created");
            System.out.println("  ID: " + student.getId());  // null
            System.out.println("  State: TRANSIENT");
            System.out.println("  - Not in database");
            System.out.println("  - Not tracked by EntityManager");
            System.out.println("  - No persistence context");
            System.out.println("  Managed? " + em.contains(student));  // false

            // ============================================
            // STATE 2: PERSISTENT (MANAGED)
            // ============================================

            System.out.println("\n\n2️⃣  PERSISTENT STATE:");
            System.out.println("Calling persist()...");

            em.getTransaction().begin();
            em.persist(student);

            System.out.println("✓ Entity persisted");
            System.out.println("  ID: " + student.getId());  // assigned
            System.out.println("  State: PERSISTENT (Managed)");
            System.out.println("  - Will be saved to database on commit");
            System.out.println("  - Tracked by EntityManager");
            System.out.println("  - In persistence context");
            System.out.println("  - Changes auto-detected");
            System.out.println("  Managed? " + em.contains(student));  // true

            // Modify entity (auto-update on commit)
            student.setAge(21);
            System.out.println("\n📝 Modified age to 21");
            System.out.println("  No explicit update() call needed");
            System.out.println("  Hibernate tracks changes automatically");

            em.getTransaction().commit();
            System.out.println("\n✓ Transaction committed");
            System.out.println("  INSERT executed");
            System.out.println("  UPDATE executed (for age change)");

            Long studentId = student.getId();

            // ============================================
            // STATE 3: DETACHED
            // ============================================

            System.out.println("\n\n3️⃣  DETACHED STATE:");
            System.out.println("Closing EntityManager...");

            em.close();
            System.out.println("✓ EntityManager closed");
            System.out.println("  State: DETACHED");
            System.out.println("  - Still in database");
            System.out.println("  - No longer tracked");
            System.out.println("  - Changes not auto-saved");
            System.out.println("  - Can cause LazyInitializationException");

            // Modify detached entity
            student.setAge(22);
            System.out.println("\n📝 Modified age to 22 (detached)");
            System.out.println("  Change NOT saved to database");
            System.out.println("  Need merge() to save");

            // Re-attach with new EntityManager
            em = HibernateUtil.getEntityManager();
            em.getTransaction().begin();

            System.out.println("\n🔄 Merging detached entity...");
            Student merged = em.merge(student);

            System.out.println("✓ Entity merged");
            System.out.println("  State: PERSISTENT again");
            System.out.println("  Changes will be saved on commit");
            System.out.println("  Managed? " + em.contains(merged));  // true

            em.getTransaction().commit();
            System.out.println("\n✓ UPDATE executed (age = 22)");

            // ============================================
            // STATE 4: REMOVED
            // ============================================

            System.out.println("\n\n4️⃣  REMOVED STATE:");

            em.getTransaction().begin();

            Student toRemove = em.find(Student.class, studentId);
            System.out.println("Found student: " + toRemove.getName());

            em.remove(toRemove);

            System.out.println("✓ Entity removed");
            System.out.println("  State: REMOVED");
            System.out.println("  - Marked for deletion");
            System.out.println("  - Still in EntityManager");
            System.out.println("  - DELETE on commit");
            System.out.println("  Managed? " + em.contains(toRemove));  // true (still managed!)

            em.getTransaction().commit();
            System.out.println("\n✓ Transaction committed");
            System.out.println("  DELETE executed");
            System.out.println("  Entity removed from database");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    public static void demonstrateGetVsLoad() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║          get() vs load() vs find()               ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        EntityManager em = HibernateUtil.getEntityManager();

        try {
            // Create test entity
            em.getTransaction().begin();
            Student student = new Student();
            student.setName("Get vs Load Demo");
            student.setEmail("demo@test.com");
            student.setAge(20);
            em.persist(student);
            em.getTransaction().commit();
            Long id = student.getId();

            em.clear();  // Clear cache

            // ============================================
            // find() - JPA Standard
            // ============================================

            System.out.println("\n1️⃣  find() - JPA Standard:");

            Student found = em.find(Student.class, id);

            System.out.println("✓ Entity loaded");
            System.out.println("  Returns: Actual entity or null");
            System.out.println("  Query: Immediate SELECT");
            System.out.println("  If not found: null");
            System.out.println("  Use when: Need to access data immediately");

            // Try non-existent
            Student notFound = em.find(Student.class, 9999L);
            System.out.println("\n  Non-existent ID:");
            System.out.println("  Result: " + (notFound == null ? "null" : "found"));

            em.clear();

            // ============================================
            // getReference() - JPA (like Hibernate's load())
            // ============================================

            System.out.println("\n\n2️⃣  getReference() - JPA Lazy:");

            Student reference = em.getReference(Student.class, id);

            System.out.println("✓ Reference obtained");
            System.out.println("  Returns: Proxy object");
            System.out.println("  Query: Deferred (lazy)");
            System.out.println("  If not found: EntityNotFoundException (when accessed)");
            System.out.println("  Use when: Only need ID (e.g., for delete)");

            System.out.println("\n  Accessing name (triggers load):");
            String name = reference.getName();
            System.out.println("  Name: " + name);
            System.out.println("  SELECT executed now");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static void demonstrateSummary() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║              LIFECYCLE SUMMARY                   ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        System.out.println("\n┌──────────────────────────────────────────────────────────────┐");
        System.out.println("│                  STATE TRANSITIONS                           │");
        System.out.println("└──────────────────────────────────────────────────────────────┘");

        System.out.println("\n  TRANSIENT");
        System.out.println("      │");
        System.out.println("      │ persist()");
        System.out.println("      ↓");
        System.out.println("  PERSISTENT ←──────┐");
        System.out.println("      │             │");
        System.out.println("      │ em.close()  │ merge()");
        System.out.println("      │             │");
        System.out.println("      ↓             │");
        System.out.println("  DETACHED ─────────┘");
        System.out.println("      ");
        System.out.println("  PERSISTENT");
        System.out.println("      │");
        System.out.println("      │ remove()");
        System.out.println("      ↓");
        System.out.println("  REMOVED");

        System.out.println("\n┌──────────────────────────────────────────────────────────────┐");
        System.out.println("│              find() vs getReference()                        │");
        System.out.println("├─────────────────┬──────────────────┬───────────────────────────┤");
        System.out.println("│    Aspect       │     find()       │    getReference()         │");
        System.out.println("├─────────────────┼──────────────────┼───────────────────────────┤");
        System.out.println("│ Loading         │ Eager            │ Lazy (proxy)              │");
        System.out.println("│ SQL Query       │ Immediate        │ Deferred                  │");
        System.out.println("│ Returns         │ Entity or null   │ Proxy                     │");
        System.out.println("│ Not found       │ Returns null     │ Exception when accessed   │");
        System.out.println("│ Use when        │ Need data now    │ Only need ID (delete)     │");
        System.out.println("└─────────────────┴──────────────────┴───────────────────────────┘");

        System.out.println("\n📋 BEST PRACTICES:");
        System.out.println("1. ✅ Use find() for most cases");
        System.out.println("2. ✅ Use getReference() for delete-by-ID");
        System.out.println("3. ✅ Close EntityManager to free resources");
        System.out.println("4. ✅ Use merge() for detached entities");
        System.out.println("5. ❌ Don't access lazy fields after session close");
        System.out.println("6. ❌ Don't rely on getReference() for null checks");
    }

    public static void runAllDemos() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║         ENTITY LIFECYCLE DEMONSTRATIONS          ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        demonstrateAllStates();
        demonstrateGetVsLoad();
        demonstrateSummary();

        System.out.println("\n\n✅ All lifecycle demonstrations completed!");
        System.out.println("\n📚 Summary:");
        System.out.println("  ✓ Transient → Persistent → Detached → Removed");
        System.out.println("  ✓ find() vs getReference()");
        System.out.println("  ✓ State transitions and best practices");
    }

    public static void main(String[] args) {
        try {
            runAllDemos();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
