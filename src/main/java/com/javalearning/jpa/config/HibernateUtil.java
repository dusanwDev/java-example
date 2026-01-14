package com.javalearning.jpa.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * HIBERNATE UTILITY CLASS
 *
 * Manages EntityManagerFactory and EntityManager lifecycle
 * This is a singleton pattern to ensure single EntityManagerFactory
 *
 * WHAT IS THIS?
 * - EntityManagerFactory: Creates EntityManagers (heavy object, create once)
 * - EntityManager: Interface to interact with database (like a session)
 *
 * REAL-WORLD USE:
 * - Every Hibernate/JPA application needs this
 * - Manages database connections
 * - Handles entity lifecycle
 * - Frequency: Used everywhere in JPA applications
 *
 * TYPESCRIPT/ANGULAR COMPARISON:
 * This is similar to:
 * - TypeORM DataSource (manages connections)
 * - Angular HttpClient (reusable service)
 * - Database connection pool
 *
 * TypeScript (TypeORM):
 * const dataSource = new DataSource({
 *   type: "postgres",
 *   ...config
 * });
 * await dataSource.initialize();
 * const manager = dataSource.manager;
 *
 * Java (JPA/Hibernate):
 * EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU");
 * EntityManager em = emf.createEntityManager();
 */
public class HibernateUtil {

    // ============================================
    // SINGLETON PATTERN
    // ============================================

    /**
     * Single EntityManagerFactory instance
     *
     * WHY STATIC?
     * - EntityManagerFactory is thread-safe
     * - Creating it is expensive (slow)
     * - Should be created ONCE per application
     * - Shared across entire application
     *
     * IMPORTANT:
     * - EntityManagerFactory = THREAD-SAFE (one per application)
     * - EntityManager = NOT THREAD-SAFE (one per request/transaction)
     */
    private static final EntityManagerFactory entityManagerFactory;

    // Static initializer block - runs when class is loaded
    static {
        try {
            System.out.println("═══════════════════════════════════════════════");
            System.out.println("🔧 Initializing Hibernate...");
            System.out.println("═══════════════════════════════════════════════");

            // Create EntityManagerFactory from persistence.xml
            // Looks for persistence-unit name "JavaLearningPU"
            entityManagerFactory = Persistence.createEntityManagerFactory("JavaLearningPU");

            System.out.println("✓ Hibernate initialized successfully!");
            System.out.println("✓ EntityManagerFactory created");
            System.out.println("═══════════════════════════════════════════════\n");

        } catch (Exception e) {
            System.err.println("❌ Error creating EntityManagerFactory!");
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    // ============================================
    // PUBLIC API
    // ============================================

    /**
     * Get EntityManagerFactory
     *
     * WHEN TO USE:
     * - Advanced scenarios
     * - Custom EntityManager creation
     * - Usually you'll use getEntityManager() instead
     *
     * @return EntityManagerFactory instance
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    /**
     * Create new EntityManager
     *
     * THIS IS WHAT YOU'LL USE MOST!
     *
     * IMPORTANT RULES:
     * 1. Create NEW EntityManager for each transaction
     * 2. EntityManager is NOT thread-safe
     * 3. ALWAYS close EntityManager when done
     * 4. Use try-with-resources for automatic closing
     *
     * PATTERN:
     * try (EntityManager em = HibernateUtil.getEntityManager()) {
     *     // Your code here
     * } // Automatically closed!
     *
     * @return New EntityManager instance
     */
    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    /**
     * Shutdown Hibernate
     *
     * WHEN TO CALL:
     * - Application shutdown
     * - End of program
     * - Releases all database connections
     *
     * IMPORTANT:
     * - Call this when your application exits
     * - Frees up resources
     * - Closes connection pool
     */
    public static void shutdown() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            System.out.println("\n🔌 Shutting down Hibernate...");
            entityManagerFactory.close();
            System.out.println("✓ EntityManagerFactory closed");
            System.out.println("✓ Hibernate shutdown complete");
        }
    }

    /**
     * Check if Hibernate is initialized
     *
     * @return true if ready to use
     */
    public static boolean isInitialized() {
        return entityManagerFactory != null && entityManagerFactory.isOpen();
    }

    /*
     * ═══════════════════════════════════════════════════════════════
     * ENTITYMANAGER vs SESSION
     * ═══════════════════════════════════════════════════════════════
     *
     * JPA (Standard):
     * - EntityManager (interface)
     * - Part of Jakarta Persistence API
     * - Database agnostic
     * - Portable across JPA providers
     *
     * Hibernate (Implementation):
     * - Session (Hibernate-specific)
     * - Implements EntityManager
     * - Additional features beyond JPA
     * - Hibernate-specific APIs
     *
     * WHICH TO USE?
     * - Use EntityManager (JPA standard) for portability
     * - Use Session only for Hibernate-specific features
     * - Most applications use EntityManager
     *
     * ═══════════════════════════════════════════════════════════════
     * LIFECYCLE COMPARISON: TypeScript vs Java
     * ═══════════════════════════════════════════════════════════════
     *
     * TypeScript (TypeORM):
     * -----------------
     * 1. Create DataSource (once)
     *    const dataSource = new DataSource(config);
     *    await dataSource.initialize();
     *
     * 2. Get Manager (per operation)
     *    const manager = dataSource.manager;
     *    await manager.save(entity);
     *
     * 3. Shutdown
     *    await dataSource.destroy();
     *
     * Java (Hibernate/JPA):
     * -------------------
     * 1. Create EntityManagerFactory (once)
     *    EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU");
     *
     * 2. Get EntityManager (per transaction)
     *    EntityManager em = emf.createEntityManager();
     *    em.getTransaction().begin();
     *    em.persist(entity);
     *    em.getTransaction().commit();
     *    em.close();
     *
     * 3. Shutdown
     *    emf.close();
     *
     * ═══════════════════════════════════════════════════════════════
     * REAL-WORLD ANALOGY
     * ═══════════════════════════════════════════════════════════════
     *
     * EntityManagerFactory = Database Connection Pool
     * - Created once when application starts
     * - Expensive to create
     * - Shared by entire application
     * - Thread-safe
     *
     * EntityManager = Database Connection
     * - Created for each transaction/request
     * - Lightweight
     * - One per thread/transaction
     * - NOT thread-safe
     *
     * Think of it like:
     * - Factory = Car Factory (one factory, makes many cars)
     * - Manager = Individual Car (many cars from one factory)
     *
     * ═══════════════════════════════════════════════════════════════
     * BEST PRACTICES
     * ═══════════════════════════════════════════════════════════════
     *
     * ✓ DO:
     * - Create EntityManagerFactory ONCE
     * - Create EntityManager per transaction
     * - Use try-with-resources for EntityManager
     * - Close EntityManager after use
     * - Shutdown EntityManagerFactory on exit
     *
     * ✗ DON'T:
     * - Create multiple EntityManagerFactory instances
     * - Share EntityManager between threads
     * - Forget to close EntityManager
     * - Use EntityManager after transaction ends
     *
     * ═══════════════════════════════════════════════════════════════
     */
}
