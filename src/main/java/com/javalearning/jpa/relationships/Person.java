package com.javalearning.jpa.relationships;

import jakarta.persistence.*;

/**
 * PERSON ENTITY - One-to-One Relationship Example
 *
 * Topics Demonstrated:
 * - @OneToOne mapping (#12-13 Mapping Relations)
 * - Unidirectional relationship
 * - Bidirectional relationship
 * - @JoinColumn customization
 * - Cascade operations
 * - Orphan removal
 *
 * WHAT IS ONE-TO-ONE?
 * - One Person has exactly ONE Passport
 * - One Passport belongs to exactly ONE Person
 * - Represented by foreign key in one table
 *
 * REAL-WORLD EXAMPLES:
 * - Person ↔ Passport
 * - User ↔ UserProfile
 * - Employee ↔ ParkingSpot
 * - Country ↔ Capital
 * - Student ↔ LibraryCard
 * Frequency: Common (weekly in enterprise apps)
 *
 * TYPESCRIPT/ANGULAR COMPARISON:
 * TypeScript (TypeORM):
 * @Entity()
 * class Person {
 *   @OneToOne(() => Passport)
 *   @JoinColumn()
 *   passport: Passport;
 * }
 *
 * Java (JPA/Hibernate):
 * @Entity
 * class Person {
 *   @OneToOne
 *   @JoinColumn(name = "passport_id")
 *   private Passport passport;
 * }
 */
@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    // ============================================
    // ONE-TO-ONE RELATIONSHIP
    // ============================================

    /**
     * @OneToOne - Defines one-to-one relationship
     *
     * OWNER SIDE:
     * - This side has the foreign key
     * - Has @JoinColumn
     * - Controls the relationship
     *
     * IMPORTANT ANNOTATIONS:
     *
     * @JoinColumn - Specifies foreign key column
     * - name: Foreign key column name (default: field_id)
     * - nullable: Can be null? (default: true)
     * - unique: Should be unique? (usually true for one-to-one)
     * - referencedColumnName: Column in referenced table (default: id)
     *
     * CASCADE OPTIONS:
     * - CascadeType.ALL: All operations cascade
     * - CascadeType.PERSIST: Save passport when saving person
     * - CascadeType.MERGE: Update passport when updating person
     * - CascadeType.REMOVE: Delete passport when deleting person
     * - CascadeType.REFRESH: Reload passport when reloading person
     * - CascadeType.DETACH: Detach passport when detaching person
     *
     * ORPHAN REMOVAL:
     * - orphanRemoval = true: Delete passport if removed from person
     * - Only use on "owns" relationship (person owns passport)
     *
     * TypeScript comparison:
     * @OneToOne(() => Passport, { cascade: true, eager: true })
     * @JoinColumn()
     * passport: Passport;
     */
    @OneToOne(
        cascade = CascadeType.ALL,  // All operations cascade to passport
        orphanRemoval = true,        // Delete passport if removed from person
        fetch = FetchType.LAZY       // Don't load passport automatically
    )
    @JoinColumn(
        name = "passport_id",        // Foreign key column name
        unique = true,               // Enforce one-to-one at DB level
        nullable = true              // Person can exist without passport
    )
    private Passport passport;

    // ============================================
    // CONSTRUCTORS
    // ============================================

    public Person() {
        // Required by JPA
    }

    public Person(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // ============================================
    // BUSINESS METHODS
    // ============================================

    /**
     * Set passport (helper method)
     *
     * BEST PRACTICE: Helper methods for bidirectional relationships
     * Ensures both sides are updated
     */
    public void setPassportBidirectional(Passport passport) {
        // Remove old passport
        if (this.passport != null) {
            this.passport.setPerson(null);
        }

        // Set new passport
        this.passport = passport;

        // Update other side
        if (passport != null) {
            passport.setPerson(this);
        }
    }

    /**
     * Get full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Passport getPassport() {
        return passport;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
    }

    // ============================================
    // TOSTRING
    // ============================================

    @Override
    public String toString() {
        return String.format("Person[id=%d, name='%s %s', email='%s', hasPassport=%s]",
            id, firstName, lastName, email, (passport != null));
    }

    /*
     * ═══════════════════════════════════════════════════════════════
     * ONE-TO-ONE RELATIONSHIP PATTERNS
     * ═══════════════════════════════════════════════════════════════
     *
     * UNIDIRECTIONAL (One way):
     * Person → Passport (Person knows about Passport, Passport doesn't know about Person)
     *
     * @Entity
     * class Person {
     *   @OneToOne
     *   @JoinColumn(name = "passport_id")
     *   private Passport passport;
     * }
     *
     * @Entity
     * class Passport {
     *   // No reference to Person
     * }
     *
     * BIDIRECTIONAL (Both ways):
     * Person ↔ Passport (Both know about each other)
     *
     * @Entity
     * class Person {
     *   @OneToOne(mappedBy = "person")
     *   private Passport passport;
     * }
     *
     * @Entity
     * class Passport {
     *   @OneToOne
     *   @JoinColumn(name = "person_id")
     *   private Person person;
     * }
     *
     * ═══════════════════════════════════════════════════════════════
     * DATABASE REPRESENTATION
     * ═══════════════════════════════════════════════════════════════
     *
     * Table: persons
     * ┌────┬────────────┬───────────┬──────────────┬─────────────┐
     * │ id │ first_name │ last_name │    email     │ passport_id │
     * ├────┼────────────┼───────────┼──────────────┼─────────────┤
     * │  1 │ John       │ Doe       │ john@...     │      101    │ ← Foreign Key
     * │  2 │ Jane       │ Smith     │ jane@...     │      102    │
     * └────┴────────────┴───────────┴──────────────┴─────────────┘
     *
     * Table: passports
     * ┌────┬────────────────┬────────────┬─────────────┐
     * │ id │ passport_number│ issue_date │ expiry_date │
     * ├────┼────────────────┼────────────┼─────────────┤
     * │101 │ US12345678     │ 2020-01-15 │ 2030-01-15  │
     * │102 │ US87654321     │ 2019-06-20 │ 2029-06-20  │
     * └────┴────────────────┴────────────┴─────────────┘
     *
     * ═══════════════════════════════════════════════════════════════
     * CASCADE OPERATIONS EXPLAINED
     * ═══════════════════════════════════════════════════════════════
     *
     * CascadeType.PERSIST:
     * em.persist(person);  // Also saves passport (if new)
     *
     * CascadeType.MERGE:
     * person.getPassport().setExpiryDate(newDate);
     * em.merge(person);  // Also updates passport
     *
     * CascadeType.REMOVE:
     * em.remove(person);  // Also deletes passport
     *
     * CascadeType.ALL:
     * All operations cascade (PERSIST, MERGE, REMOVE, REFRESH, DETACH)
     *
     * ORPHAN REMOVAL:
     * person.setPassport(null);
     * em.merge(person);  // Passport is deleted (orphan removal)
     *
     * ═══════════════════════════════════════════════════════════════
     * WHEN TO USE EACH PATTERN
     * ═══════════════════════════════════════════════════════════════
     *
     * Unidirectional:
     * ✓ Simple relationship
     * ✓ Only need to navigate one way
     * ✓ Cleaner code (less complexity)
     * ✓ Example: Person → Passport
     *
     * Bidirectional:
     * ✓ Need to navigate both ways
     * ✓ More queries require both directions
     * ✓ Example: Person ↔ UserProfile (both need each other)
     *
     * ═══════════════════════════════════════════════════════════════
     * COMMON MISTAKES TO AVOID
     * ═══════════════════════════════════════════════════════════════
     *
     * ✗ WRONG: Using Many-to-One for one-to-one
     * ✗ WRONG: Not using unique = true on join column
     * ✗ WRONG: Using CascadeType.ALL without understanding
     * ✗ WRONG: Forgetting to update both sides in bidirectional
     * ✗ WRONG: Using EAGER fetch everywhere
     *
     * ✓ CORRECT: Use @OneToOne with unique constraint
     * ✓ CORRECT: Use cascade carefully (only what you need)
     * ✓ CORRECT: Update both sides in bidirectional
     * ✓ CORRECT: Use LAZY fetch by default
     *
     * ═══════════════════════════════════════════════════════════════
     * REAL-WORLD USE CASES
     * ═══════════════════════════════════════════════════════════════
     *
     * E-commerce:
     * - User ↔ ShoppingCart (active cart)
     * - Product ↔ ProductDetails
     * - Order ↔ Invoice
     *
     * Social Media:
     * - User ↔ UserProfile
     * - User ↔ UserSettings
     * - Post ↔ PostStatistics
     *
     * HR System:
     * - Employee ↔ ParkingSpot
     * - Employee ↔ Desk
     * - Employee ↔ Locker
     *
     * Education:
     * - Student ↔ LibraryCard
     * - Teacher ↔ Classroom (assigned classroom)
     *
     * Frequency: Common in enterprise applications
     *
     * ═══════════════════════════════════════════════════════════════
     */
}
