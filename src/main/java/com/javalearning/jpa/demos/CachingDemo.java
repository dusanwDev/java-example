package com.javalearning.jpa.demos;

import com.javalearning.jpa.config.HibernateUtil;
import com.javalearning.jpa.entities.Student;
import jakarta.persistence.Cache;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * CACHING DEMONSTRATION
 *
 * Topics Demonstrated:
 * - First-level cache (Session cache)
 * - Second-level cache (SessionFactory cache)
 * - Query cache
 * - Cache operations (evict, clear, contains)
 *
 * WHAT IS CACHING?
 * Storing frequently accessed data in memory for faster retrieval
 *
 * HIBERNATE CACHE LEVELS:
 * 1. First-Level Cache (L1) - Session-scoped, always enabled
 * 2. Second-Level Cache (L2) - SessionFactory-scoped, optional
 * 3. Query Cache - Caches query results, optional
 *
 * TYPESCRIPT/ANGULAR COMPARISON:
 * Similar to:
 * - NgRx Store (application-level cache)
 * - HTTP Cache (browser cache)
 * - Redis/Memcached (distributed cache)
 * - Service worker cache
 *
 * REAL-WORLD USE CASE:
 * - Reduce database hits
 * - Improve application performance
 * - Cache reference data (countries, currencies)
 * - Session-specific data
 */
public class CachingDemo {

    /**
     * DEMONSTRATION 1: FIRST-LEVEL CACHE (L1)
     *
     * - Session-scoped
     * - Always enabled
     * - Automatic
     */
    public static void demonstrateFirstLevelCache() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║      FIRST-LEVEL CACHE (SESSION CACHE)           ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        EntityManager em = HibernateUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            // Create test student
            Student student = new Student();
            student.setName("Cache Test Student");
            student.setEmail("cache@test.com");
            student.setAge(20);
            em.persist(student);

            em.getTransaction().commit();
            Long studentId = student.getId();

            // ============================================
            // FIRST-LEVEL CACHE IN ACTION
            // ============================================

            System.out.println("\n📖 First find (hits database):");
            Student s1 = em.find(Student.class, studentId);
            System.out.println("✓ Found: " + s1.getName());
            System.out.println("SQL: SELECT * FROM students WHERE id = " + studentId);

            System.out.println("\n📖 Second find (from cache):");
            Student s2 = em.find(Student.class, studentId);
            System.out.println("✓ Found: " + s2.getName());
            System.out.println("✓ NO SQL QUERY - Retrieved from L1 cache");

            System.out.println("\n🔍 Verification:");
            System.out.println("  s1 == s2: " + (s1 == s2));  // true - same object!
            System.out.println("  Same object reference from cache");

            // ============================================
            // CLEARING THE CACHE
            // ============================================

            System.out.println("\n\n🗑️  Clearing session cache:");
            em.clear();
            System.out.println("✓ Cache cleared");

            System.out.println("\n📖 Third find (hits database again):");
            Student s3 = em.find(Student.class, studentId);
            System.out.println("✓ Found: " + s3.getName());
            System.out.println("SQL: SELECT * FROM students WHERE id = " + studentId);
            System.out.println("  s3 == s1: " + (s3 == s1));  // false - different object

            // ============================================
            // DETACHING FROM CACHE
            // ============================================

            System.out.println("\n\n🔌 Detaching entity:");
            Student s4 = em.find(Student.class, studentId);
            System.out.println("Before detach - Managed: " + em.contains(s4));

            em.detach(s4);
            System.out.println("After detach - Managed: " + em.contains(s4));
            System.out.println("✓ Entity removed from cache");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }

        /*
         * FIRST-LEVEL CACHE CHARACTERISTICS:
         *
         * ✅ Always enabled (can't disable)
         * ✅ Scoped to EntityManager/Session
         * ✅ Automatic dirty checking
         * ✅ Identity guarantee (same object)
         * ✅ Write-behind optimization
         *
         * OPERATIONS:
         * - find(id) → Check cache first
         * - clear() → Clear entire cache
         * - detach(entity) → Remove from cache
         * - contains(entity) → Check if cached
         *
         * LIFESPAN:
         * - Created with EntityManager
         * - Destroyed when EntityManager closed
         * - Not shared between sessions
         */
    }

    /**
     * DEMONSTRATION 2: SECOND-LEVEL CACHE (L2)
     *
     * - SessionFactory-scoped
     * - Optional (must configure)
     * - Shared across sessions
     */
    public static void demonstrateSecondLevelCache() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║     SECOND-LEVEL CACHE (SHARED CACHE)            ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        System.out.println("\n⚠️  Note: Second-level cache requires configuration");
        System.out.println("This demo shows concepts - actual implementation needs:");

        System.out.println("\n1️⃣  Add cache provider dependency:");
        System.out.println("```xml");
        System.out.println("<dependency>");
        System.out.println("    <groupId>org.hibernate.orm</groupId>");
        System.out.println("    <artifactId>hibernate-jcache</artifactId>");
        System.out.println("</dependency>");
        System.out.println("<dependency>");
        System.out.println("    <groupId>org.ehcache</groupId>");
        System.out.println("    <artifactId>ehcache</artifactId>");
        System.out.println("</dependency>");
        System.out.println("```");

        System.out.println("\n2️⃣  Configure in persistence.xml:");
        System.out.println("```xml");
        System.out.println("<property name=\"hibernate.cache.use_second_level_cache\" value=\"true\"/>");
        System.out.println("<property name=\"hibernate.cache.region.factory_class\"");
        System.out.println("          value=\"org.hibernate.cache.jcache.JCacheRegionFactory\"/>");
        System.out.println("```");

        System.out.println("\n3️⃣  Enable on entities:");
        System.out.println("```java");
        System.out.println("@Entity");
        System.out.println("@Cacheable");
        System.out.println("@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)");
        System.out.println("public class Student { }");
        System.out.println("```");

        System.out.println("\n📊 CACHE STRATEGIES:");
        System.out.println("┌─────────────────────┬──────────────┬─────────────┐");
        System.out.println("│     Strategy        │ Performance  │ Consistency │");
        System.out.println("├─────────────────────┼──────────────┼─────────────┤");
        System.out.println("│ READ_ONLY           │    ⭐⭐⭐⭐⭐    │   ⭐⭐⭐⭐⭐   │");
        System.out.println("│ NONSTRICT_READ_WRITE│    ⭐⭐⭐⭐     │   ⭐⭐      │");
        System.out.println("│ READ_WRITE          │    ⭐⭐⭐      │   ⭐⭐⭐⭐    │");
        System.out.println("│ TRANSACTIONAL       │    ⭐⭐       │   ⭐⭐⭐⭐⭐   │");
        System.out.println("└─────────────────────┴──────────────┴─────────────┘");

        System.out.println("\n🎯 WHEN TO USE EACH:");
        System.out.println("READ_ONLY:");
        System.out.println("  ✓ Reference data (countries, currencies)");
        System.out.println("  ✓ Never changes");
        System.out.println("  ✓ Best performance");

        System.out.println("\nREAD_WRITE:");
        System.out.println("  ✓ Most entities");
        System.out.println("  ✓ Frequent reads, occasional writes");
        System.out.println("  ✓ Good balance");

        System.out.println("\nTRANSACTIONAL:");
        System.out.println("  ✓ Critical data (financial)");
        System.out.println("  ✓ Strict consistency needed");
        System.out.println("  ✓ Requires JTA");

        // Demonstrate L2 cache API (even without actual cache configured)
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            Cache cache = em.getEntityManagerFactory().getCache();

            System.out.println("\n\n🔧 CACHE API OPERATIONS:");
            System.out.println("```java");
            System.out.println("Cache cache = emf.getCache();");
            System.out.println("");
            System.out.println("// Check if entity is cached");
            System.out.println("boolean cached = cache.contains(Student.class, id);");
            System.out.println("");
            System.out.println("// Evict single entity");
            System.out.println("cache.evict(Student.class, id);");
            System.out.println("");
            System.out.println("// Evict all entities of a type");
            System.out.println("cache.evict(Student.class);");
            System.out.println("");
            System.out.println("// Clear entire cache");
            System.out.println("cache.evictAll();");
            System.out.println("```");

        } finally {
            em.close();
        }
    }

    /**
     * DEMONSTRATION 3: QUERY CACHE
     */
    public static void demonstrateQueryCache() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║              QUERY CACHE                         ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        System.out.println("\n📝 CONFIGURATION:");
        System.out.println("```xml");
        System.out.println("<property name=\"hibernate.cache.use_query_cache\" value=\"true\"/>");
        System.out.println("```");

        System.out.println("\n💡 USAGE:");
        System.out.println("```java");
        System.out.println("TypedQuery<Student> query = em.createQuery(");
        System.out.println("    \"FROM Student WHERE age > :age\", Student.class");
        System.out.println(");");
        System.out.println("query.setParameter(\"age\", 20);");
        System.out.println("query.setHint(\"org.hibernate.cacheable\", true); // Enable query cache");
        System.out.println("List<Student> students = query.getResultList();");
        System.out.println("```");

        System.out.println("\n⚙️  HOW IT WORKS:");
        System.out.println("1. First execution: Query hits database");
        System.out.println("2. Results stored in query cache");
        System.out.println("3. Subsequent executions: Results from cache");
        System.out.println("4. Cache invalidated if data changes");

        System.out.println("\n⚠️  IMPORTANT:");
        System.out.println("- Query cache stores entity IDs, not entities");
        System.out.println("- Still needs L2 cache for entity data");
        System.out.println("- Same query + same parameters = cache hit");
        System.out.println("- Different parameters = cache miss");

        System.out.println("\n✅ GOOD FOR:");
        System.out.println("  ✓ Frequently executed queries");
        System.out.println("  ✓ With same parameters");
        System.out.println("  ✓ Results don't change often");

        System.out.println("\n❌ NOT GOOD FOR:");
        System.out.println("  ✗ Dynamic queries");
        System.out.println("  ✗ Frequently changing data");
        System.out.println("  ✗ Large result sets");
    }

    /**
     * COMPREHENSIVE CACHE SUMMARY
     */
    public static void demonstrateSummary() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║            CACHING SUMMARY                       ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        System.out.println("\n┌──────────────────────────────────────────────────────────────┐");
        System.out.println("│                  CACHE COMPARISON                            │");
        System.out.println("├─────────────┬────────────────┬────────────────┬──────────────┤");
        System.out.println("│    Cache    │     Scope      │    Enabled     │    Stores    │");
        System.out.println("├─────────────┼────────────────┼────────────────┼──────────────┤");
        System.out.println("│ First-Level │ EntityManager  │ Always         │ Entities     │");
        System.out.println("│ (L1)        │ (Session)      │ (automatic)    │              │");
        System.out.println("├─────────────┼────────────────┼────────────────┼──────────────┤");
        System.out.println("│ Second-Level│ SessionFactory │ Optional       │ Entities     │");
        System.out.println("│ (L2)        │ (Application)  │ (configure)    │              │");
        System.out.println("├─────────────┼────────────────┼────────────────┼──────────────┤");
        System.out.println("│ Query Cache │ SessionFactory │ Optional       │ Query results│");
        System.out.println("│             │ (Application)  │ (configure)    │ (IDs only)   │");
        System.out.println("└─────────────┴────────────────┴────────────────┴──────────────┘");

        System.out.println("\n🎯 DECISION TREE:");
        System.out.println("");
        System.out.println("Need caching?");
        System.out.println("  ├─ L1 Cache → Always available (free)");
        System.out.println("  │");
        System.out.println("  ├─ L2 Cache → Reference data, shared across sessions");
        System.out.println("  │  ├─ Never changes → READ_ONLY");
        System.out.println("  │  ├─ Occasional updates → READ_WRITE");
        System.out.println("  │  └─ Critical data → TRANSACTIONAL");
        System.out.println("  │");
        System.out.println("  └─ Query Cache → Repeated queries with same params");
        System.out.println("     └─ Requires L2 cache enabled");

        System.out.println("\n📋 BEST PRACTICES:");
        System.out.println("1. ✅ Always use L1 cache (it's free!)");
        System.out.println("2. ✅ Enable L2 cache for reference data");
        System.out.println("3. ✅ Choose appropriate cache strategy");
        System.out.println("4. ✅ Monitor cache hit rates");
        System.out.println("5. ✅ Set cache sizes appropriately");
        System.out.println("6. ❌ Don't cache everything");
        System.out.println("7. ❌ Don't use L2 for frequently changing data");
        System.out.println("8. ❌ Don't ignore cache invalidation");

        System.out.println("\n⚡ PERFORMANCE IMPACT:");
        System.out.println("Without cache:");
        System.out.println("  1000 finds = 1000 database queries");
        System.out.println("");
        System.out.println("With L1 cache:");
        System.out.println("  1000 finds in same session = 1 database query");
        System.out.println("");
        System.out.println("With L2 cache:");
        System.out.println("  1000 finds across sessions = 1 database query");
        System.out.println("");
        System.out.println("💡 Caching can improve performance by 10-100x!");
    }

    /**
     * Run all caching demonstrations
     */
    public static void runAllDemos() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║         CACHING DEMONSTRATIONS                   ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        demonstrateFirstLevelCache();
        demonstrateSecondLevelCache();
        demonstrateQueryCache();
        demonstrateSummary();

        System.out.println("\n\n✅ All caching demonstrations completed!");
        System.out.println("\n📚 Summary:");
        System.out.println("  ✓ First-Level Cache (Session)");
        System.out.println("  ✓ Second-Level Cache (Application)");
        System.out.println("  ✓ Query Cache");
        System.out.println("  ✓ Cache strategies and best practices");
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
