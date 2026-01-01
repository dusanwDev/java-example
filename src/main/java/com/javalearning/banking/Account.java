package com.javalearning.banking;

/**
 * ABSTRACTION (#48)
 *
 * Abstract class - Cannot be instantiated directly, must be extended
 * Provides common functionality for all account types
 *
 * REAL-WORLD USE CASE:
 * Abstract classes define common behavior for a family of related classes
 * Examples:
 * - Shape (extended by Circle, Rectangle, Triangle)
 * - Vehicle (extended by Car, Motorcycle, Truck)
 * - Account (extended by SavingsAccount, CheckingAccount)
 * Frequency: Common in enterprise applications
 *
 * TYPESCRIPT COMPARISON:
 * TypeScript:
 * abstract class Account {
 *   abstract calculateInterest(): number;
 *   deposit(amount: number) { ... }
 * }
 *
 * Java:
 * public abstract class Account {
 *   public abstract double calculateInterest();
 *   public void deposit(double amount) { ... }
 * }
 *
 * Very similar! Both support:
 * - Abstract methods (must be implemented by subclasses)
 * - Concrete methods (inherited by subclasses)
 * - Cannot be instantiated directly
 *
 * ABSTRACTION vs INTERFACE:
 * Abstract Class:
 * - Can have constructors
 * - Can have instance variables
 * - Can have concrete methods
 * - Single inheritance (extend one class)
 *
 * Interface:
 * - No constructors
 * - Only constants (public static final)
 * - Methods are abstract (or default since Java 8)
 * - Multiple inheritance (implement many interfaces)
 */
public abstract class Account implements Transactable {

    // ============================================
    // FIELDS (Common to all accounts)
    // ============================================

    protected String accountNumber;
    protected String accountHolder;
    protected double balance;
    protected static int accountCounter = 1000; // For generating account numbers

    // ============================================
    // CONSTRUCTOR
    // ============================================

    /**
     * Constructor for Account
     * Even though Account is abstract, it can have a constructor
     * Called by subclass constructors using super()
     */
    public Account(String accountHolder, double initialBalance) {
        this.accountNumber = generateAccountNumber();
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
    }

    // ============================================
    // CONCRETE METHODS (Inherited by all subclasses)
    // ============================================

    /**
     * Generate unique account number
     */
    private String generateAccountNumber() {
        return "ACC" + (accountCounter++);
    }

    /**
     * Deposit implementation (from Transactable interface)
     */
    @Override
    public void deposit(double amount) {
        if (!Transactable.isValidAmount(amount)) {
            System.out.println("❌ Invalid amount");
            return;
        }

        balance += amount;
        System.out.println("✓ Deposited: $" + String.format("%.2f", amount));
        System.out.println("  New balance: $" + String.format("%.2f", balance));
    }

    /**
     * Withdraw implementation (from Transactable interface)
     * Can be overridden by subclasses for different behavior
     */
    @Override
    public boolean withdraw(double amount) {
        if (!Transactable.isValidAmount(amount)) {
            System.out.println("❌ Invalid amount");
            return false;
        }

        if (amount > balance) {
            System.out.println("❌ Insufficient funds");
            return false;
        }

        balance -= amount;
        System.out.println("✓ Withdrew: $" + String.format("%.2f", amount));
        System.out.println("  New balance: $" + String.format("%.2f", balance));
        return true;
    }

    /**
     * Get balance (from Transactable interface)
     */
    @Override
    public double getBalance() {
        return balance;
    }

    // ============================================
    // ABSTRACT METHODS (Must be implemented by subclasses)
    // ============================================

    /**
     * Calculate interest - different for each account type
     * This is an ABSTRACT method - no implementation here
     * Subclasses MUST implement this
     */
    public abstract double calculateInterest();

    /**
     * Get account type - different for each account type
     */
    public abstract String getAccountType();

    // ============================================
    // GETTERS
    // ============================================

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    // ============================================
    // TOSTRING METHOD (#47)
    // ============================================

    /**
     * toString method - can be overridden by subclasses
     */
    @Override
    public String toString() {
        return String.format("%s Account [%s] - %s: $%.2f",
            getAccountType(),
            accountNumber,
            accountHolder,
            balance
        );
    }

    /* KEY CONCEPTS:
     *
     * PROTECTED modifier:
     * - Accessible in subclasses
     * - Not accessible outside package (unless subclassed)
     * Like TypeScript's protected
     *
     * ABSTRACT methods:
     * - Declared but not implemented
     * - Subclasses MUST implement
     * - Forces consistent API across subclasses
     *
     * WHY USE ABSTRACTION?
     * 1. Code reuse (common functionality in base class)
     * 2. Enforce structure (subclasses must implement abstract methods)
     * 3. Polymorphism (treat all accounts the same way)
     * 4. Encapsulation (hide implementation details)
     *
     * REAL-WORLD USE:
     * - Payment processors (CreditCard, PayPal, Crypto)
     * - Notification services (Email, SMS, Push)
     * - Data sources (Database, API, File)
     * Frequency: Very common in enterprise applications
     */
}
