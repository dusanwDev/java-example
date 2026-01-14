package com.javalearning.jpa.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * EMBEDDABLE OBJECT - Address
 *
 * Topic: Embeddable Objects (#11 from course)
 *
 * WHAT IS EMBEDDABLE?
 * - A class that is part of another entity
 * - No separate table created
 * - Fields added to parent entity's table
 * - Reusable across multiple entities
 *
 * WHEN TO USE?
 * - Value objects (Address, Money, Coordinates)
 * - Composite data that belongs together
 * - Data that doesn't need its own ID
 * - Reduce code duplication
 *
 * EXAMPLE:
 * Student has Address (street, city, zip)
 * Instead of:
 *   students table: id, name, street, city, zip, state
 *
 * We organize as:
 *   Student{
 *     id, name,
 *     Address{street, city, zip, state}
 *   }
 *
 * But still one table!
 *
 * REAL-WORLD USE:
 * - Address (shipping, billing)
 * - Name (first, middle, last)
 * - Money (amount, currency)
 * - Coordinates (latitude, longitude)
 * - Dimensions (width, height, depth)
 * - Time Period (start, end)
 * Frequency: Very common (daily)
 *
 * TYPESCRIPT/ANGULAR COMPARISON:
 * TypeScript (TypeORM):
 * class Address {
 *   @Column()
 *   street: string;
 *
 *   @Column()
 *   city: string;
 * }
 *
 * @Entity()
 * class Student {
 *   @Column(() => Address)
 *   address: Address;
 * }
 *
 * Java (JPA/Hibernate):
 * @Embeddable
 * class Address {
 *   @Column
 *   private String street;
 *   @Column
 *   private String city;
 * }
 *
 * @Entity
 * class Student {
 *   @Embedded
 *   private Address address;
 * }
 *
 * Very similar concept!
 */

// ============================================
// @EMBEDDABLE ANNOTATION
// ============================================

/**
 * @Embeddable - Marks this class as embeddable
 *
 * WHAT IT DOES:
 * - Class can be embedded in entities
 * - No @Entity annotation
 * - No @Id needed
 * - Fields become columns in parent table
 *
 * REQUIREMENTS:
 * - Must be Serializable (best practice)
 * - Must have no-arg constructor
 * - Should override equals() and hashCode()
 */
@Embeddable
public class Address {

    // ============================================
    // FIELDS
    // ============================================

    /**
     * Address fields
     *
     * These become columns in the parent entity's table
     * with column names: street, city, state, zip_code
     */

    @Column(length = 200)
    private String street;

    @Column(length = 100)
    private String city;

    @Column(length = 50)
    private String state;

    @Column(name = "zip_code", length = 10)
    private String zipCode;

    @Column(length = 100)
    private String country;

    // ============================================
    // CONSTRUCTORS
    // ============================================

    /**
     * Default constructor (REQUIRED)
     */
    public Address() {
        // Required by JPA
    }

    /**
     * Parameterized constructor
     */
    public Address(String street, String city, String state, String zipCode, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }

    /**
     * Convenience constructor (US addresses)
     */
    public Address(String street, String city, String state, String zipCode) {
        this(street, city, state, zipCode, "USA");
    }

    // ============================================
    // GETTERS AND SETTERS
    // ============================================

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    // ============================================
    // BUSINESS METHODS
    // ============================================

    /**
     * Get formatted address
     */
    public String getFormattedAddress() {
        return String.format("%s, %s, %s %s, %s",
            street, city, state, zipCode, country);
    }

    // ============================================
    // EQUALS AND HASHCODE
    // ============================================

    /**
     * IMPORTANT: Override equals() and hashCode()
     *
     * WHY?
     * - Embeddable objects are value objects
     * - Two addresses with same values should be equal
     * - Used in collections (Set, Map)
     * - Important for Hibernate caching
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (street != null ? !street.equals(address.street) : address.street != null) return false;
        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        if (state != null ? !state.equals(address.state) : address.state != null) return false;
        if (zipCode != null ? !zipCode.equals(address.zipCode) : address.zipCode != null) return false;
        return country != null ? country.equals(address.country) : address.country == null;
    }

    @Override
    public int hashCode() {
        int result = street != null ? street.hashCode() : 0;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (zipCode != null ? zipCode.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
    }

    // ============================================
    // TOSTRING
    // ============================================

    @Override
    public String toString() {
        return getFormattedAddress();
    }

    /*
     * ═══════════════════════════════════════════════════════════════
     * EMBEDDABLE vs ENTITY
     * ═══════════════════════════════════════════════════════════════
     *
     * EMBEDDABLE (@Embeddable):
     * - No separate table
     * - No ID
     * - Part of parent entity
     * - Cannot exist independently
     * - Value object
     * - Example: Address, Money, Dimensions
     *
     * ENTITY (@Entity):
     * - Has own table
     * - Has ID (primary key)
     * - Independent lifecycle
     * - Can be referenced by multiple entities
     * - Example: User, Product, Order
     *
     * ═══════════════════════════════════════════════════════════════
     * DATABASE REPRESENTATION
     * ═══════════════════════════════════════════════════════════════
     *
     * Java Code:
     * -----------
     * @Entity
     * class Student {
     *   @Id Long id;
     *   String name;
     *
     *   @Embedded
     *   Address homeAddress;
     *
     *   @Embedded
     *   @AttributeOverrides({
     *     @AttributeOverride(name="street", column=@Column(name="work_street")),
     *     @AttributeOverride(name="city", column=@Column(name="work_city"))
     *   })
     *   Address workAddress;
     * }
     *
     * Database Table (students):
     * --------------------------
     * id | name | street | city | state | zip_code | country |
     *    |      | work_street | work_city | work_state | work_zip_code | work_country
     *
     * See? No separate address table!
     * All columns in one table!
     *
     * ═══════════════════════════════════════════════════════════════
     * ADVANTAGES OF EMBEDDABLE
     * ═══════════════════════════════════════════════════════════════
     *
     * ✓ Code Reusability:
     *   - Define Address once
     *   - Use in Student, Employee, Company, etc.
     *
     * ✓ Better Organization:
     *   - Group related fields
     *   - Cleaner code
     *   - Single Responsibility Principle
     *
     * ✓ Performance:
     *   - No JOIN needed
     *   - All data in one table
     *   - Faster queries
     *
     * ✓ Type Safety:
     *   - Address is a proper type
     *   - Can add validation
     *   - Can add business methods
     *
     * ═══════════════════════════════════════════════════════════════
     * WHEN TO USE EMBEDDABLE vs ENTITY
     * ═══════════════════════════════════════════════════════════════
     *
     * Use @Embeddable when:
     * ✓ Data belongs to parent
     * ✓ No need for separate ID
     * ✓ Won't be shared/referenced by multiple entities
     * ✓ Performance is priority (avoid JOINs)
     * ✓ Examples: Address, Money, Coordinates
     *
     * Use @Entity when:
     * ✓ Needs own ID
     * ✓ Independent lifecycle
     * ✓ Shared by multiple entities
     * ✓ Need to query it separately
     * ✓ Examples: User, Product, Order
     *
     * ═══════════════════════════════════════════════════════════════
     * REAL-WORLD EXAMPLES
     * ═══════════════════════════════════════════════════════════════
     *
     * E-commerce:
     * -----------
     * @Entity
     * class Order {
     *   @Embedded Address shippingAddress;
     *   @Embedded Address billingAddress;
     *   @Embedded Money totalAmount;
     * }
     *
     * HR System:
     * ----------
     * @Entity
     * class Employee {
     *   @Embedded PersonName name;  // firstName, middleName, lastName
     *   @Embedded Address homeAddress;
     *   @Embedded PhoneNumber phone;
     * }
     *
     * Geo-location App:
     * ----------------
     * @Entity
     * class Store {
     *   @Embedded Coordinates location;  // latitude, longitude
     *   @Embedded Address address;
     * }
     *
     * Frequency: Very common in enterprise applications!
     *
     * ═══════════════════════════════════════════════════════════════
     */
}
