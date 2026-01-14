package com.javalearning.jpa.relationships;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * PASSPORT ENTITY - One-to-One Relationship (Inverse Side)
 *
 * This is the INVERSE (non-owning) side of the one-to-one relationship
 * Person is the OWNER (has the foreign key)
 */
@Entity
@Table(name = "passports")
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "passport_number", unique = true, nullable = false, length = 20)
    private String passportNumber;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(length = 50)
    private String issuingCountry;

    // ============================================
    // BIDIRECTIONAL ONE-TO-ONE (Optional)
    // ============================================

    /**
     * Bidirectional relationship back to Person
     *
     * mappedBy = "passport":
     * - Indicates this is the INVERSE side
     * - "passport" refers to field name in Person class
     * - This side does NOT have foreign key
     * - Person side has the foreign key
     *
     * UNIDIRECTIONAL vs BIDIRECTIONAL:
     *
     * If you want UNIDIRECTIONAL (Person → Passport only):
     * - Comment out or remove this field
     * - Person knows about Passport
     * - Passport doesn't know about Person
     *
     * If you want BIDIRECTIONAL (Person ↔ Passport):
     * - Keep this field
     * - Both sides know about each other
     * - Can navigate both ways
     *
     * TypeScript:
     * @OneToOne(() => Person, person => person.passport)
     * person: Person;
     */
    @OneToOne(mappedBy = "passport", fetch = FetchType.LAZY)
    private Person person;

    // ============================================
    // CONSTRUCTORS
    // ============================================

    public Passport() {
        // Required by JPA
    }

    public Passport(String passportNumber, LocalDate issueDate, LocalDate expiryDate) {
        this.passportNumber = passportNumber;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
    }

    public Passport(String passportNumber, LocalDate issueDate,
                   LocalDate expiryDate, String issuingCountry) {
        this.passportNumber = passportNumber;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.issuingCountry = issuingCountry;
    }

    // ============================================
    // BUSINESS METHODS
    // ============================================

    /**
     * Check if passport is expired
     */
    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    /**
     * Check if passport expires soon (within 6 months)
     */
    public boolean expiresSoon() {
        LocalDate sixMonthsFromNow = LocalDate.now().plusMonths(6);
        return expiryDate.isBefore(sixMonthsFromNow) && !isExpired();
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

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getIssuingCountry() {
        return issuingCountry;
    }

    public void setIssuingCountry(String issuingCountry) {
        this.issuingCountry = issuingCountry;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    // ============================================
    // TOSTRING
    // ============================================

    @Override
    public String toString() {
        return String.format("Passport[id=%d, number='%s', country='%s', expires=%s, expired=%s]",
            id, passportNumber, issuingCountry, expiryDate, isExpired());
    }

    /*
     * ═══════════════════════════════════════════════════════════════
     * OWNER vs INVERSE SIDE
     * ═══════════════════════════════════════════════════════════════
     *
     * OWNER SIDE (Person):
     * - Has @JoinColumn
     * - Has foreign key in database
     * - Controls the relationship
     * - Changes here update the database
     *
     * INVERSE SIDE (Passport):
     * - Has mappedBy
     * - No foreign key
     * - Read-only relationship
     * - Changes here don't update database
     *
     * WHICH SHOULD BE OWNER?
     * - Usually the entity that "owns" the relationship
     * - Person owns Passport (makes sense)
     * - Choose based on business logic
     *
     * ═══════════════════════════════════════════════════════════════
     */
}
