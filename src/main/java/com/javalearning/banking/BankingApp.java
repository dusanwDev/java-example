package com.javalearning.banking;

import java.util.ArrayList;

/**
 * BANKING APPLICATION
 *
 * This application demonstrates:
 * - Abstraction (#48) - Account is abstract
 * - Interfaces (#49) - Transactable interface
 * - Polymorphism (#50) - Treating different accounts the same way
 * - Runtime polymorphism (#51) - Method calls resolved at runtime
 * - Inheritance (#44) - SavingsAccount, CheckingAccount extend Account
 * - Super keyword (#45) - Calling parent constructor/methods
 * - Method overriding (#46) - Different withdraw implementations
 * - Aggregation (#53) - Customer has accounts
 * - Composition (#54) - Strong ownership relationships
 * - Array of objects (#42) - Managing collections of accounts
 *
 * REAL-WORLD COMPARISON:
 * Like building an Angular banking application with:
 * - Models/Interfaces (Account, Customer)
 * - Services (Bank operations)
 * - Components (User interface)
 * - Inheritance (Different account types)
 *
 * TYPESCRIPT TO JAVA MAPPING:
 * ┌──────────────────────────────┬─────────────────────────────┐
 * │ TypeScript/Angular           │ Java                        │
 * ├──────────────────────────────┼─────────────────────────────┤
 * │ abstract class Account {}    │ abstract class Account {}   │
 * │ interface Transactable {}    │ interface Transactable {}   │
 * │ class extends Account        │ class extends Account       │
 * │ class implements Interface   │ class implements Interface  │
 * │ super()                      │ super()                     │
 * │ override method()            │ @Override method()          │
 * │ polymorphism works same way  │ polymorphism works same way │
 * └──────────────────────────────┴─────────────────────────────┘
 */
public class BankingApp {

    /**
     * POLYMORPHISM DEMONSTRATION
     */
    public static void demonstratePolymorphism() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║           POLYMORPHISM DEMONSTRATION             ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        /**
         * POLYMORPHISM (#50)
         *
         * Poly = Many, Morphism = Forms
         * Same interface, different implementations
         *
         * Key concept: Parent reference can hold child object
         * Account account = new SavingsAccount(...);
         *
         * TypeScript:
         * const account: Account = new SavingsAccount(...);
         *
         * Same concept in both languages!
         */

        // Parent reference, child object
        Account savings = new SavingsAccount("Alice Johnson", 1000.00, 3.0);
        Account checking = new CheckingAccount("Bob Smith", 500.00, 300.00);

        // Both are treated as Account, but behave differently!
        System.out.println("\n--- Testing polymorphism ---");
        System.out.println("Savings account: " + savings.getAccountType());
        System.out.println("Checking account: " + checking.getAccountType());

        /**
         * RUNTIME POLYMORPHISM (#51)
         *
         * Method called is determined at RUNTIME, not compile time
         * Even though reference is Account, actual method from
         * SavingsAccount/CheckingAccount is called
         *
         * This is called "Dynamic Method Dispatch" or "Late Binding"
         */

        System.out.println("\n--- Runtime polymorphism in action ---");

        // Same method call, different behavior!
        savings.withdraw(100);  // Uses SavingsAccount's withdraw
        checking.withdraw(600); // Uses CheckingAccount's withdraw (overdraft!)

        System.out.println("\nInterest calculation:");
        System.out.println("Savings interest: $" +
                          String.format("%.2f", savings.calculateInterest()));
        System.out.println("Checking interest: $" +
                          String.format("%.2f", checking.calculateInterest()));

        /**
         * POLYMORPHISM WITH ARRAYS/LISTS
         *
         * Can store different account types in same array!
         * Like storing different component types in Angular:
         * components: Component[] = [new HeaderComponent(), new FooterComponent()];
         */

        System.out.println("\n--- Polymorphic array ---");

        Account[] accounts = {
            new SavingsAccount("Carol White", 2000.00, 2.5),
            new CheckingAccount("Dave Brown", 1500.00, 500.00),
            new SavingsAccount("Eve Davis", 3000.00, 3.5),
            new CheckingAccount("Frank Miller", 800.00, 200.00)
        };

        // Process all accounts polymorphically
        System.out.println("Processing all accounts:");
        for (Account account : accounts) {
            System.out.println("\n  " + account);
            System.out.println("  Interest: $" + String.format("%.2f", account.calculateInterest()));
        }

        /* WHY IS POLYMORPHISM USEFUL?
         *
         * 1. Code Flexibility:
         *    - Write code that works with parent type
         *    - Automatically works with all child types
         *
         * 2. Extensibility:
         *    - Add new account types without changing existing code
         *    - Just extend Account and override methods
         *
         * 3. Maintainability:
         *    - Common interface reduces code duplication
         *    - Changes in one place affect all children
         *
         * REAL-WORLD USES:
         * - Payment processing (different payment methods)
         * - UI frameworks (different widget types)
         * - Notifications (email, SMS, push)
         * - Data sources (database, API, cache)
         *
         * Frequency: Core OOP concept, used everywhere!
         */
    }

    /**
     * INTERFACE DEMONSTRATION
     */
    public static void demonstrateInterfaces() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║            INTERFACE DEMONSTRATION               ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        /**
         * INTERFACES (#49)
         *
         * Can reference objects by their interface
         * All accounts implement Transactable
         */

        Transactable account1 = new SavingsAccount("John Doe", 1000.00);
        Transactable account2 = new CheckingAccount("Jane Doe", 500.00);

        System.out.println("Both referenced as Transactable interface:");

        // Can call interface methods
        account1.deposit(200);
        account2.deposit(300);

        account1.printBalance(); // Default method from interface
        account2.printBalance();

        // Static method from interface
        System.out.println("\nValidating amounts:");
        System.out.println("Is $100 valid? " + Transactable.isValidAmount(100));
        System.out.println("Is $-50 valid? " + Transactable.isValidAmount(-50));

        /* INTERFACE BENEFITS:
         *
         * 1. Contract: Guarantees certain methods exist
         * 2. Multiple inheritance: Class can implement many interfaces
         * 3. Decoupling: Code depends on interface, not implementation
         * 4. Testing: Easy to mock/stub for unit tests
         *
         * REAL-WORLD USE in Angular/Spring:
         * - Repository interfaces (JPA)
         * - Service contracts
         * - API definitions
         * - Plugin systems
         */
    }

    /**
     * COMPLETE BANKING SCENARIO
     */
    public static void runBankingScenario() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         COMPLETE BANKING SCENARIO                ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        // ============================================
        // CREATE CUSTOMERS (Aggregation #53)
        // ============================================

        System.out.println("\n📋 Creating customers...");

        Customer customer1 = new Customer(
            "Alice Johnson",
            "alice@email.com",
            "555-0101"
        );

        Customer customer2 = new Customer(
            "Bob Smith",
            "bob@email.com",
            "555-0102"
        );

        // ============================================
        // CREATE ACCOUNTS (Array of Objects #42)
        // ============================================

        System.out.println("\n💰 Opening accounts...");

        SavingsAccount aliceSavings = new SavingsAccount("Alice Johnson", 5000.00, 3.0);
        CheckingAccount aliceChecking = new CheckingAccount("Alice Johnson", 2000.00, 500.00);

        SavingsAccount bobSavings = new SavingsAccount("Bob Smith", 10000.00, 2.5);
        CheckingAccount bobChecking = new CheckingAccount("Bob Smith", 1500.00, 300.00);

        // Link accounts to customers (Aggregation)
        customer1.addAccount(aliceSavings);
        customer1.addAccount(aliceChecking);

        customer2.addAccount(bobSavings);
        customer2.addAccount(bobChecking);

        // ============================================
        // PERFORM TRANSACTIONS
        // ============================================

        System.out.println("\n💸 Performing transactions...");

        System.out.println("\n--- Alice's transactions ---");
        aliceSavings.deposit(1000);
        aliceSavings.withdraw(500);
        aliceSavings.withdraw(200);

        aliceChecking.deposit(500);
        aliceChecking.withdraw(2800); // Uses overdraft!

        System.out.println("\n--- Bob's transactions ---");
        bobSavings.deposit(2000);
        bobSavings.applyInterest(); // Savings-specific method

        bobChecking.withdraw(100);
        bobChecking.checkFeeWaiver(); // Checking-specific method

        // ============================================
        // DISPLAY CUSTOMER ACCOUNTS
        // ============================================

        System.out.println("\n👤 Customer Information:");
        System.out.println(customer1);
        customer1.displayAccounts();

        System.out.println("\n" + customer2);
        customer2.displayAccounts();

        // ============================================
        // POLYMORPHIC OPERATIONS
        // ============================================

        System.out.println("\n📊 Bank-wide Operations (Polymorphism):");

        ArrayList<Account> allAccounts = new ArrayList<>();
        allAccounts.addAll(customer1.getAccounts());
        allAccounts.addAll(customer2.getAccounts());

        double totalBalance = 0;
        double totalInterest = 0;

        for (Account account : allAccounts) {
            totalBalance += account.getBalance();
            totalInterest += account.calculateInterest();
        }

        System.out.println("Total bank deposits: $" + String.format("%.2f", totalBalance));
        System.out.println("Total interest liability: $" + String.format("%.2f", totalInterest));

        // ============================================
        // MONTHLY OPERATIONS
        // ============================================

        System.out.println("\n📅 End of Month Operations:");

        for (Account account : allAccounts) {
            System.out.println("\nProcessing: " + account.getAccountNumber());

            // Runtime polymorphism - method called depends on actual type
            if (account instanceof SavingsAccount) {
                SavingsAccount savings = (SavingsAccount) account;
                savings.applyInterest();
                savings.resetWithdrawalCount();
            } else if (account instanceof CheckingAccount) {
                CheckingAccount checking = (CheckingAccount) account;
                checking.checkFeeWaiver();
                checking.chargeMonthlyFee();
            }
        }

        // ============================================
        // FINAL SUMMARY
        // ============================================

        System.out.println("\n\n📈 Final Summary:");
        customer1.displayAccounts();
        customer2.displayAccounts();

        System.out.println("\n✅ Banking scenario completed successfully!");
    }

    /**
     * Main method - run all demonstrations
     */
    public static void main(String[] args) {
        demonstratePolymorphism();
        demonstrateInterfaces();
        runBankingScenario();

        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║   ALL OOP CONCEPTS DEMONSTRATED SUCCESSFULLY!    ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        System.out.println("\nConcepts covered:");
        System.out.println("✓ Abstraction (#48) - Abstract Account class");
        System.out.println("✓ Interfaces (#49) - Transactable interface");
        System.out.println("✓ Polymorphism (#50) - Same interface, different behavior");
        System.out.println("✓ Runtime Polymorphism (#51) - Dynamic method dispatch");
        System.out.println("✓ Inheritance (#44) - Account -> Savings/Checking");
        System.out.println("✓ Super (#45) - Parent constructor calls");
        System.out.println("✓ Method Overriding (#46) - Custom withdraw methods");
        System.out.println("✓ Aggregation (#53) - Customer has Accounts");
        System.out.println("✓ Array of Objects (#42) - Managing account collections");
    }
}
