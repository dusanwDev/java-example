package com.javalearning.banking;

/**
 * INTERFACES (#49)
 *
 * Interface defines a contract - what methods a class MUST implement
 * Similar to TypeScript interfaces, but used differently
 *
 * REAL-WORLD USE CASE:
 * Interfaces define capabilities/behaviors that classes can implement
 * Examples:
 * - Comparable (can be compared)
 * - Serializable (can be converted to bytes)
 * - Runnable (can be run as a thread)
 * - Transactable (can perform transactions)
 * Frequency: Very common (daily in enterprise Java)
 *
 * TYPESCRIPT COMPARISON:
 * TypeScript interfaces define structure:
 * interface User {
 *   name: string;
 *   email: string;
 * }
 *
 * Java interfaces define behavior (methods):
 * interface Transactable {
 *   void deposit(double amount);
 *   boolean withdraw(double amount);
 * }
 *
 * Key differences:
 * - TS interfaces are for type checking (compile-time only)
 * - Java interfaces are implemented by classes (runtime contracts)
 * - Java classes can implement multiple interfaces (like multiple inheritance)
 * - Java interfaces can have default methods (since Java 8)
 */
public interface Transactable {

    /**
     * Deposit money into the account
     * @param amount Amount to deposit
     */
    void deposit(double amount);

    /**
     * Withdraw money from the account
     * @param amount Amount to withdraw
     * @return true if successful, false if insufficient funds
     */
    boolean withdraw(double amount);

    /**
     * Get current balance
     * @return Current balance
     */
    double getBalance();

    /**
     * Default method (Java 8+)
     * Interface methods can have implementations!
     */
    default void printBalance() {
        System.out.println("Current balance: $" + String.format("%.2f", getBalance()));
    }

    /**
     * Static method in interface (Java 8+)
     */
    static boolean isValidAmount(double amount) {
        return amount > 0;
    }

    /* REAL-WORLD USE:
     *
     * Why use interfaces?
     * 1. Define contracts (what a class MUST do)
     * 2. Enable polymorphism (treat different classes the same way)
     * 3. Support multiple inheritance (class can implement many interfaces)
     * 4. Design APIs (define what your code expects)
     *
     * Common in:
     * - Repository patterns (Spring Data JPA)
     * - Service contracts
     * - Event handlers
     * - Strategy patterns
     *
     * Frequency: Used daily in professional Java development
     */
}
