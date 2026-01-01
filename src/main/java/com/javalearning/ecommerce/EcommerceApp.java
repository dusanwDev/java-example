package com.javalearning.ecommerce;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * E-COMMERCE APPLICATION
 *
 * This application demonstrates:
 * - Generics (#66) - InventoryManager<T>
 * - HashMaps (#67) - Product storage, cart management
 * - ArrayLists (#56) - Product collections
 * - Enums (#68) - ProductCategory
 *
 * REAL-WORLD COMPARISON:
 * Like building an Angular e-commerce application with:
 * - Models (Product, ShoppingCart)
 * - Services (InventoryManager)
 * - State management (HashMap for quick lookups)
 * - Generic components (InventoryManager<T>)
 *
 * TYPESCRIPT TO JAVA COMPARISON:
 * ┌────────────────────────────────┬─────────────────────────────┐
 * │ TypeScript/Angular             │ Java                        │
 * ├────────────────────────────────┼─────────────────────────────┤
 * │ Map<string, Product>           │ HashMap<String, Product>    │
 * │ Array<Product>                 │ ArrayList<Product>          │
 * │ class Manager<T> {}            │ class Manager<T> {}         │
 * │ enum Category {}               │ enum Category {}            │
 * │ map.set(key, value)            │ map.put(key, value)         │
 * │ map.get(key)                   │ map.get(key)                │
 * │ map.has(key)                   │ map.containsKey(key)        │
 * └────────────────────────────────┴─────────────────────────────┘
 */
public class EcommerceApp {

    /**
     * GENERICS DEMONSTRATION
     */
    public static void demonstrateGenerics() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║           GENERICS DEMONSTRATION                 ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        /**
         * GENERICS (#66)
         *
         * Generic classes provide type safety and reusability
         * InventoryManager<Product> ensures we only store Products
         *
         * TypeScript:
         * const inventory = new InventoryManager<Product>();
         *
         * Java:
         * InventoryManager<Product> inventory = new InventoryManager<>();
         *
         * Benefits:
         * - Type safety at compile time
         * - No casting needed
         * - Code reusability
         * - Better IDE support
         */

        System.out.println("\n📦 Creating generic inventory manager...");

        // Generic type parameter ensures type safety
        InventoryManager<Product> inventory = new InventoryManager<>();

        Product laptop = new Product(
            "Gaming Laptop",
            "High-performance laptop for gaming",
            1299.99,
            15,
            ProductCategory.ELECTRONICS,
            "TechBrand"
        );

        Product tshirt = new Product(
            "Cotton T-Shirt",
            "Comfortable cotton t-shirt",
            19.99,
            100,
            ProductCategory.CLOTHING,
            "FashionCo"
        );

        // Type-safe additions
        inventory.addProduct(laptop);
        inventory.addProduct(tshirt);

        // Compile-time type checking
        // inventory.addProduct("string"); // ERROR: Type mismatch!
        // inventory.addProduct(123); // ERROR: Type mismatch!

        System.out.println("\n✓ Generics provide compile-time type safety!");

        /* WHY GENERICS ARE POWERFUL:
         *
         * Without Generics (old Java):
         * ArrayList list = new ArrayList();
         * list.add("string");
         * list.add(123);
         * String s = (String) list.get(1); // Runtime ERROR!
         *
         * With Generics:
         * ArrayList<String> list = new ArrayList<>();
         * list.add("string");
         * list.add(123); // Compile-time ERROR!
         * String s = list.get(0); // No casting needed!
         *
         * REAL-WORLD USES:
         * - Collections (List<T>, Map<K,V>, Set<T>)
         * - Repository pattern (Repository<User>, Repository<Order>)
         * - API responses (Response<T>)
         * - Builder pattern (Builder<T>)
         * - Optional values (Optional<T>)
         *
         * Frequency: Used everywhere in modern Java!
         */
    }

    /**
     * HASHMAP DEMONSTRATION
     */
    public static void demonstrateHashMaps() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║            HASHMAP DEMONSTRATION                 ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        /**
         * HASHMAPS (#67)
         *
         * HashMap stores key-value pairs for fast lookup
         * Like JavaScript Map or plain objects
         *
         * TypeScript:
         * const products = new Map<string, Product>();
         * products.set("PROD001", laptop);
         * const item = products.get("PROD001");
         *
         * Java:
         * HashMap<String, Product> products = new HashMap<>();
         * products.put("PROD001", laptop);
         * Product item = products.get("PROD001");
         *
         * Time Complexity:
         * - get(): O(1) average
         * - put(): O(1) average
         * - containsKey(): O(1) average
         * - remove(): O(1) average
         */

        System.out.println("\n🗺️ Creating HashMap for product lookup...");

        HashMap<String, Product> productMap = new HashMap<>();

        // Add products
        Product phone = new Product("Smartphone", "Latest smartphone", 699.99, 25,
                                   ProductCategory.ELECTRONICS, "PhoneCorp");
        Product book = new Product("Java Programming", "Learn Java", 49.99, 50,
                                  ProductCategory.BOOKS, "TechPublishing");

        productMap.put(phone.getProductId(), phone);
        productMap.put(book.getProductId(), book);

        System.out.println("✓ Added " + productMap.size() + " products to HashMap");

        // Fast lookup by key
        System.out.println("\n🔍 Looking up product by ID...");
        Product found = productMap.get(phone.getProductId());
        System.out.println("Found: " + found.getName());

        // Check if key exists
        System.out.println("\nChecking product existence:");
        System.out.println("Has " + phone.getProductId() + "? " +
                          productMap.containsKey(phone.getProductId()));
        System.out.println("Has PROD999? " + productMap.containsKey("PROD999"));

        // Iterate through HashMap
        System.out.println("\nIterating through HashMap:");
        for (HashMap.Entry<String, Product> entry : productMap.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue().getName());
        }

        /* HASHMAP vs ARRAYLIST:
         *
         * Use HashMap when:
         * - Need fast lookup by key
         * - Want to associate keys with values
         * - Don't care about order
         * - Need to check if key exists
         *
         * Use ArrayList when:
         * - Need ordered collection
         * - Access by index
         * - Don't need key-value pairs
         * - Need to maintain insertion order
         *
         * REAL-WORLD USES of HashMap:
         * - Caching (userId -> User object)
         * - Configuration (key -> value)
         * - Counting (word -> frequency)
         * - Indexing (productId -> Product)
         * - Session management (sessionId -> Session)
         *
         * Frequency: Very common (daily use)
         */
    }

    /**
     * COMPLETE E-COMMERCE SCENARIO
     */
    public static void runEcommerceScenario() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║      COMPLETE E-COMMERCE SCENARIO                ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        // ============================================
        // SETUP INVENTORY (Generics + HashMap)
        // ============================================

        System.out.println("\n📦 Setting up inventory...");

        InventoryManager<Product> inventory = new InventoryManager<>();

        // Add electronics
        Product laptop = new Product(
            "Gaming Laptop Pro",
            "Intel i9, 32GB RAM, RTX 4080",
            2499.99, 10,
            ProductCategory.ELECTRONICS, "TechMaster"
        );

        Product phone = new Product(
            "Smartphone X12",
            "6.7\" OLED, 256GB, 5G",
            899.99, 25,
            ProductCategory.ELECTRONICS, "PhoneCorp"
        );

        Product headphones = new Product(
            "Wireless Headphones",
            "Noise cancelling, 30hr battery",
            299.99, 50,
            ProductCategory.ELECTRONICS, "AudioTech"
        );

        // Add clothing
        Product jeans = new Product(
            "Blue Jeans",
            "Classic fit denim jeans",
            59.99, 100,
            ProductCategory.CLOTHING, "DenimCo"
        );

        Product tshirt = new Product(
            "Cotton T-Shirt",
            "100% organic cotton",
            24.99, 200,
            ProductCategory.CLOTHING, "EcoWear"
        );

        // Add books
        Product javaBook = new Product(
            "Effective Java",
            "Best practices for Java programming",
            45.99, 30,
            ProductCategory.BOOKS, "TechBooks"
        );

        Product designBook = new Product(
            "Design Patterns",
            "Gang of Four design patterns",
            54.99, 20,
            ProductCategory.BOOKS, "TechBooks"
        );

        // Add to inventory
        inventory.addProduct(laptop);
        inventory.addProduct(phone);
        inventory.addProduct(headphones);
        inventory.addProduct(jeans);
        inventory.addProduct(tshirt);
        inventory.addProduct(javaBook);
        inventory.addProduct(designBook);

        // ============================================
        // DISPLAY INVENTORY
        // ============================================

        inventory.displayAllProducts();
        inventory.displayProductsByCategory();
        inventory.displayStatistics();

        // ============================================
        // DEMONSTRATE SEARCH (HashMap operations)
        // ============================================

        System.out.println("\n\n🔍 SEARCH OPERATIONS");

        System.out.println("\nSearching for 'laptop':");
        ArrayList<Product> searchResults = inventory.searchProducts("laptop");
        for (Product p : searchResults) {
            System.out.println("  • " + p);
        }

        System.out.println("\nProducts by brand 'TechBooks':");
        ArrayList<Product> techBooks = inventory.getProductsByBrand("TechBooks");
        for (Product p : techBooks) {
            System.out.println("  • " + p);
        }

        // ============================================
        // SHOPPING CART (HashMap for cart items)
        // ============================================

        System.out.println("\n\n🛒 SHOPPING CART OPERATIONS");

        ShoppingCart cart = new ShoppingCart(inventory);

        System.out.println("\nAdding items to cart...");
        cart.addItem(laptop.getProductId(), 1);
        cart.addItem(headphones.getProductId(), 2);
        cart.addItem(javaBook.getProductId(), 1);

        cart.displayCart();

        // ============================================
        // DEMONSTRATE ENUM FEATURES
        // ============================================

        System.out.println("\n\n📊 ENUM DEMONSTRATION (Tax Calculation)");

        System.out.println("\nTax rates by category:");
        for (ProductCategory category : ProductCategory.values()) {
            double samplePrice = 100.00;
            double tax = category.calculateTax(samplePrice);
            System.out.println(String.format("  %s: %.1f%% ($%.2f on $100)",
                category, category.getTaxRate() * 100, tax));
        }

        // ============================================
        // INVENTORY MANAGEMENT
        // ============================================

        System.out.println("\n\n📋 INVENTORY MANAGEMENT");

        // Add ratings
        laptop.addRating(4.5);
        laptop.addRating(5.0);
        laptop.addRating(4.0);

        phone.addRating(4.8);
        phone.addRating(4.9);

        System.out.println("\nProduct ratings:");
        System.out.println("  " + laptop.getName() + ": " +
                          String.format("%.1f/5.0 (%d reviews)",
                          laptop.getRating(), laptop.getReviewCount()));
        System.out.println("  " + phone.getName() + ": " +
                          String.format("%.1f/5.0 (%d reviews)",
                          phone.getRating(), phone.getReviewCount()));

        // Stock management
        System.out.println("\n📦 Stock management:");
        headphones.reduceStock(45); // Now at 5 units
        System.out.println("  " + headphones.getName() +
                          " stock: " + headphones.getStockQuantity());
        if (headphones.isLowStock()) {
            System.out.println("  ⚠️ LOW STOCK WARNING!");
            headphones.restock(50);
        }

        // ============================================
        // CHECKOUT
        // ============================================

        System.out.println("\n\n💳 CHECKOUT PROCESS");

        cart.displayCart();

        System.out.println("\nApplying 10% discount:");
        double discountedTotal = cart.applyDiscount(10);
        System.out.println("  Discounted total: $" + String.format("%.2f", discountedTotal));

        System.out.println("\nProcessing checkout...");
        cart.checkout();

        // ============================================
        // VERIFY STOCK UPDATE
        // ============================================

        System.out.println("\n📊 Inventory after checkout:");
        System.out.println("  Laptop stock: " + laptop.getStockQuantity());
        System.out.println("  Headphones stock: " + headphones.getStockQuantity());
        System.out.println("  Java Book stock: " + javaBook.getStockQuantity());

        inventory.displayStatistics();

        System.out.println("\n✅ E-commerce scenario completed successfully!");
    }

    /**
     * Main method
     */
    public static void main(String[] args) {
        demonstrateGenerics();
        demonstrateHashMaps();
        runEcommerceScenario();

        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║    ALL CONCEPTS DEMONSTRATED SUCCESSFULLY!       ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        System.out.println("\nConcepts covered:");
        System.out.println("✓ Generics (#66) - Type-safe InventoryManager<T>");
        System.out.println("✓ HashMaps (#67) - Fast key-value lookups");
        System.out.println("✓ ArrayLists (#56) - Dynamic collections");
        System.out.println("✓ Enums (#68) - Type-safe constants with methods");
    }
}
