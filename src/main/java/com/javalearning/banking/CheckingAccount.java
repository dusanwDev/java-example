package com.javalearning.banking;

/**
 * CHECKING ACCOUNT - Another inheritance example
 *
 * Demonstrates:
 * - Inheritance from Account
 * - Method overriding with different behavior
 * - Overdraft protection feature
 *
 * REAL-WORLD USE CASE:
 * Checking account with overdraft protection
 * Different rules than savings account
 */
public class CheckingAccount extends Account {

    // ============================================
    // FIELDS
    // ============================================

    private double overdraftLimit;
    private double monthlyFee;
    private boolean feeWaived;

    // ============================================
    // CONSTRUCTORS
    // ============================================

    public CheckingAccount(String accountHolder, double initialBalance, double overdraftLimit) {
        super(accountHolder, initialBalance);
        this.overdraftLimit = overdraftLimit;
        this.monthlyFee = 12.00;
        this.feeWaived = false;

        System.out.println("✓ Checking account created with $" +
                          String.format("%.2f", overdraftLimit) + " overdraft protection");
    }

    public CheckingAccount(String accountHolder, double initialBalance) {
        this(accountHolder, initialBalance, 500.00); // Default $500 overdraft
    }

    // ============================================
    // METHOD OVERRIDING
    // ============================================

    /**
     * Override withdraw to allow overdraft
     * Different behavior than SavingsAccount!
     */
    @Override
    public boolean withdraw(double amount) {
        if (!Transactable.isValidAmount(amount)) {
            System.out.println("❌ Invalid amount");
            return false;
        }

        // Check if we have enough including overdraft
        double availableBalance = balance + overdraftLimit;

        if (amount > availableBalance) {
            System.out.println("❌ Insufficient funds (including overdraft)");
            System.out.println("   Available: $" + String.format("%.2f", availableBalance));
            return false;
        }

        balance -= amount;
        System.out.println("✓ Withdrew: $" + String.format("%.2f", amount));

        if (balance < 0) {
            System.out.println("⚠️ Account overdrawn!");
            System.out.println("   Overdraft used: $" + String.format("%.2f", Math.abs(balance)));
            System.out.println("   Overdraft remaining: $" +
                              String.format("%.2f", overdraftLimit + balance));
        } else {
            System.out.println("   New balance: $" + String.format("%.2f", balance));
        }

        return true;
    }

    /**
     * Checking accounts typically don't earn interest
     */
    @Override
    public double calculateInterest() {
        return 0.0; // No interest on checking
    }

    @Override
    public String getAccountType() {
        return "Checking";
    }

    // ============================================
    // CHECKING-SPECIFIC METHODS
    // ============================================

    /**
     * Charge monthly fee
     */
    public void chargeMonthlyFee() {
        if (feeWaived) {
            System.out.println("ℹ️ Monthly fee waived");
            return;
        }

        balance -= monthlyFee;
        System.out.println("✓ Monthly fee charged: $" + String.format("%.2f", monthlyFee));
        System.out.println("  New balance: $" + String.format("%.2f", balance));
    }

    /**
     * Waive fee for customers with high balance
     */
    public void checkFeeWaiver() {
        // Waive fee if balance > $1000
        if (balance >= 1000) {
            feeWaived = true;
            System.out.println("✓ Monthly fee waived (balance ≥ $1000)");
        } else {
            feeWaived = false;
        }
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
        System.out.println("✓ Overdraft limit updated to $" +
                          String.format("%.2f", overdraftLimit));
    }

    public double getMonthlyFee() {
        return monthlyFee;
    }

    public boolean isFeeWaived() {
        return feeWaived;
    }

    // ============================================
    // OVERRIDE TOSTRING
    // ============================================

    @Override
    public String toString() {
        return super.toString() +
               String.format(" | Overdraft: $%.2f | Fee: $%.2f%s",
                   overdraftLimit,
                   monthlyFee,
                   feeWaived ? " (waived)" : "");
    }

    /* COMPARISON: SavingsAccount vs CheckingAccount
     *
     * Both extend Account, but have DIFFERENT behaviors:
     *
     * SavingsAccount:
     * - Has interest rate
     * - Limited withdrawals
     * - No overdraft
     * - Earns interest
     *
     * CheckingAccount:
     * - Has overdraft protection
     * - Unlimited withdrawals
     * - Monthly fee
     * - No interest
     *
     * This is the power of INHERITANCE and METHOD OVERRIDING!
     * Same interface (both are Accounts), different implementations.
     *
     * REAL-WORLD ANALOGY in Angular:
     * Like having different form controls (text input, select, checkbox)
     * All implement ControlValueAccessor interface
     * But each has different behavior!
     */
}
