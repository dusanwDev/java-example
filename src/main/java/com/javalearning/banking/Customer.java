package com.javalearning.banking;

import java.util.ArrayList;

/**
 * CUSTOMER CLASS
 *
 * Demonstrates:
 * - Aggregation (#53) - Customer HAS-A relationship with Account
 * - Composition (#54) - Strong ownership relationship
 * - Array of objects (#42) - Managing multiple accounts
 *
 * REAL-WORLD USE CASE:
 * Customer can have multiple bank accounts
 * Like a user in Angular having multiple associated entities
 *
 * TYPESCRIPT COMPARISON:
 * class Customer {
 *   constructor(
 *     private name: string,
 *     private accounts: Account[] = []
 *   ) {}
 * }
 */
public class Customer {

    // ============================================
    // FIELDS
    // ============================================

    private String customerId;
    private String name;
    private String email;
    private String phone;

    /**
     * AGGREGATION (#53) vs COMPOSITION (#54)
     *
     * AGGREGATION: HAS-A relationship (weak)
     * - Customer HAS-A list of accounts
     * - Accounts can exist independently
     * - If customer is deleted, accounts can remain
     *
     * COMPOSITION: PART-OF relationship (strong)
     * - Account has transactions that are PART-OF account
     * - If account is deleted, transactions are deleted too
     * - Stronger ownership
     *
     * This example shows AGGREGATION:
     * - Customer has accounts
     * - Accounts exist independently
     * - Account can be transferred to another customer
     */
    private ArrayList<Account> accounts; // AGGREGATION

    private static int customerCounter = 1;

    // ============================================
    // CONSTRUCTOR
    // ============================================

    public Customer(String name, String email, String phone) {
        this.customerId = "CUST" + String.format("%04d", customerCounter++);
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.accounts = new ArrayList<>();

        System.out.println("✓ Customer created: " + name + " [" + customerId + "]");
    }

    // ============================================
    // ACCOUNT MANAGEMENT (Array of Objects #42)
    // ============================================

    /**
     * ARRAY OF OBJECTS (#42)
     *
     * Managing multiple Account objects
     * Like managing a list of entities in Angular service
     */
    public void addAccount(Account account) {
        accounts.add(account);
        System.out.println("✓ Account " + account.getAccountNumber() +
                          " linked to customer " + name);
    }

    public void removeAccount(String accountNumber) {
        accounts.removeIf(account -> account.getAccountNumber().equals(accountNumber));
        System.out.println("✓ Account " + accountNumber + " removed");
    }

    /**
     * Get account by account number
     */
    public Account getAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    /**
     * Get all accounts
     */
    public ArrayList<Account> getAccounts() {
        return new ArrayList<>(accounts); // Return copy
    }

    /**
     * Get total balance across all accounts
     */
    public double getTotalBalance() {
        double total = 0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        return total;
    }

    /**
     * Display all accounts
     */
    public void displayAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts for this customer");
            return;
        }

        System.out.println("\n=== Accounts for " + name + " ===");
        for (Account account : accounts) {
            System.out.println("  " + account);
        }
        System.out.println("Total balance: $" + String.format("%.2f", getTotalBalance()));
    }

    // ============================================
    // GETTERS AND SETTERS
    // ============================================

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // ============================================
    // TOSTRING
    // ============================================

    @Override
    public String toString() {
        return String.format("Customer [%s]: %s (Email: %s, Phone: %s) - %d accounts",
            customerId, name, email, phone, accounts.size());
    }

    /* AGGREGATION vs COMPOSITION EXPLAINED:
     *
     * AGGREGATION (HAS-A):
     * ┌──────────┐         ┌─────────┐
     * │ Customer │◇───────→│ Account │
     * └──────────┘         └─────────┘
     * Customer has accounts, but accounts can exist independently
     *
     * Example in code:
     * Customer customer = new Customer("John");
     * Account account = new SavingsAccount("John", 1000);
     * customer.addAccount(account); // Account exists independently
     *
     * COMPOSITION (PART-OF):
     * ┌─────────┐         ┌─────────────┐
     * │ Account │♦───────→│ Transaction │
     * └─────────┘         └─────────────┘
     * Account owns transactions, if account deleted, transactions deleted
     *
     * Example in code:
     * class Account {
     *   private ArrayList<Transaction> transactions = new ArrayList<>();
     *   // Transactions created and owned by account
     * }
     *
     * REAL-WORLD ANALOGIES:
     *
     * Aggregation:
     * - Student HAS-A Courses (student can drop course, course remains)
     * - Car HAS-A Driver (driver can leave, car remains)
     * - Library HAS-A Books (can remove book, book exists)
     *
     * Composition:
     * - House PART-OF Rooms (destroy house, rooms destroyed)
     * - Car PART-OF Engine (car scrapped, engine scrapped)
     * - Order PART-OF OrderItems (delete order, items deleted)
     *
     * TYPESCRIPT/ANGULAR COMPARISON:
     * In Angular:
     * - Component HAS-A Services (aggregation via DI)
     * - Component PART-OF Template (composition - template dies with component)
     *
     * Frequency: Understanding this is important for system design
     */
}
