package com.javalearning.banking;

/**
 * INHERITANCE & METHOD OVERRIDING
 *
 * Topics demonstrated:
 * - Inheritance (#44) - extends Account
 * - Super keyword (#45) - calling parent constructor
 * - Method overriding (#46) - implementing abstract methods
 *
 * REAL-WORLD USE CASE:
 * Savings account with interest and withdrawal limitations
 * Common in banking applications
 *
 * TYPESCRIPT COMPARISON:
 * TypeScript:
 * class SavingsAccount extends Account {
 *   constructor(holder: string, balance: number) {
 *     super(holder, balance);
 *   }
 * }
 *
 * Java:
 * public class SavingsAccount extends Account {
 *   public SavingsAccount(String holder, double balance) {
 *     super(holder, balance);
 *   }
 * }
 *
 * Very similar syntax!
 */
public class SavingsAccount extends Account {

    // ============================================
    // ADDITIONAL FIELDS (specific to savings)
    // ============================================

    private double interestRate; // Annual interest rate
    private int withdrawalLimit; // Max withdrawals per month
    private int withdrawalCount; // Current month withdrawals

    // ============================================
    // CONSTRUCTORS
    // ============================================

    /**
     * SUPER KEYWORD (#45)
     *
     * super() calls the parent class constructor
     * Must be first statement in constructor
     *
     * Similar to TypeScript's super()
     */
    public SavingsAccount(String accountHolder, double initialBalance, double interestRate) {
        // Call parent constructor
        super(accountHolder, initialBalance);

        // Initialize savings-specific fields
        this.interestRate = interestRate;
        this.withdrawalLimit = 6; // Federal regulation limit
        this.withdrawalCount = 0;

        System.out.println("✓ Savings account created with " + interestRate + "% interest");
    }

    /**
     * Overloaded constructor with default interest rate
     */
    public SavingsAccount(String accountHolder, double initialBalance) {
        this(accountHolder, initialBalance, 2.5); // Default 2.5% interest
    }

    // ============================================
    // METHOD OVERRIDING (#46)
    // ============================================

    /**
     * METHOD OVERRIDING (#46)
     *
     * Override parent's withdraw method to add withdrawal limit
     * @Override annotation ensures we're actually overriding
     * (compiler error if method signature doesn't match parent)
     *
     * TypeScript:
     * override withdraw(amount: number): boolean {
     *   // custom implementation
     * }
     *
     * Java:
     * @Override
     * public boolean withdraw(double amount) {
     *   // custom implementation
     * }
     */
    @Override
    public boolean withdraw(double amount) {
        // Check withdrawal limit
        if (withdrawalCount >= withdrawalLimit) {
            System.out.println("❌ Withdrawal limit reached for this month");
            System.out.println("   Savings accounts limited to " + withdrawalLimit + " withdrawals/month");
            return false;
        }

        // Call parent's withdraw method using super
        boolean success = super.withdraw(amount);

        if (success) {
            withdrawalCount++;
            System.out.println("   Withdrawals used: " + withdrawalCount + "/" + withdrawalLimit);
        }

        return success;
    }

    /**
     * Implement abstract method from Account
     */
    @Override
    public double calculateInterest() {
        return balance * (interestRate / 100);
    }

    /**
     * Implement abstract method from Account
     */
    @Override
    public String getAccountType() {
        return "Savings";
    }

    // ============================================
    // SAVINGS-SPECIFIC METHODS
    // ============================================

    /**
     * Apply monthly interest
     */
    public void applyInterest() {
        double interest = calculateInterest();
        balance += interest;
        System.out.println("✓ Interest applied: $" + String.format("%.2f", interest));
        System.out.println("  New balance: $" + String.format("%.2f", balance));
    }

    /**
     * Reset withdrawal count (called monthly)
     */
    public void resetWithdrawalCount() {
        withdrawalCount = 0;
        System.out.println("✓ Withdrawal limit reset");
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
        System.out.println("✓ Interest rate updated to " + interestRate + "%");
    }

    public int getWithdrawalCount() {
        return withdrawalCount;
    }

    public int getWithdrawalLimit() {
        return withdrawalLimit;
    }

    // ============================================
    // OVERRIDE TOSTRING
    // ============================================

    /**
     * Override parent's toString to include savings-specific info
     */
    @Override
    public String toString() {
        return super.toString() +
               String.format(" | Interest: %.2f%% | Withdrawals: %d/%d",
                   interestRate, withdrawalCount, withdrawalLimit);
    }

    /* KEY CONCEPTS:
     *
     * INHERITANCE:
     * - SavingsAccount IS-A Account
     * - Inherits all non-private fields and methods
     * - Can add new fields and methods
     * - Can override methods for custom behavior
     *
     * SUPER KEYWORD:
     * - super() calls parent constructor
     * - super.method() calls parent's method
     * - Used to access parent's implementation
     *
     * METHOD OVERRIDING:
     * - Same method signature as parent
     * - Different implementation
     * - @Override annotation (optional but recommended)
     * - Enables polymorphism
     *
     * REAL-WORLD USE:
     * - Specialized behavior for different account types
     * - Payment methods (CreditCard extends Payment)
     * - UI components (CustomButton extends Button)
     * - Data repositories (UserRepository extends BaseRepository)
     * Frequency: Very common in OOP applications
     */
}
