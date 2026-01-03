package com.javalearning.jpa.entities;

import jakarta.persistence.*;

/**
 * LAPTOP ENTITY - Demonstrating Relationships
 *
 * This entity will be used to demonstrate:
 * - One-to-One relationship with Student
 * - Many-to-One relationship (many laptops, one student)
 * - Different fetch strategies
 *
 * REAL-WORLD USE:
 * - Asset management systems
 * - Inventory tracking
 * - School/company equipment management
 *
 * TYPESCRIPT/ANGULAR COMPARISON:
 * @Entity()
 * export class Laptop {
 *   @ManyToOne(() => Student)
 *   student: Student;
 * }
 */
@Entity
@Table(name = "laptops")
public class Laptop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String brand;

    @Column(nullable = false, length = 100)
    private String model;

    @Column(name = "serial_number", unique = true, length = 50)
    private String serialNumber;

    @Column(precision = 10, scale = 2)
    private Double price;

    /**
     * This will be used for relationship mapping later
     * Commented for now, will uncomment in relationships section
     */
    // @ManyToOne
    // @JoinColumn(name = "student_id")
    // private Student student;

    // ============================================
    // CONSTRUCTORS
    // ============================================

    public Laptop() {
        // Required by JPA
    }

    public Laptop(String brand, String model, String serialNumber, Double price) {
        this.brand = brand;
        this.model = model;
        this.serialNumber = serialNumber;
        this.price = price;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    // ============================================
    // TOSTRING
    // ============================================

    @Override
    public String toString() {
        return String.format("Laptop[id=%d, brand='%s', model='%s', serial='%s', price=$%.2f]",
            id, brand, model, serialNumber, price);
    }
}
