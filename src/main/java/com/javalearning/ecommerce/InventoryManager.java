package com.javalearning.ecommerce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * INVENTORY MANAGER
 *
 * Topics demonstrated:
 * - Generics (#66)
 * - HashMaps (#67)
 * - ArrayLists (#56)
 *
 * REAL-WORLD USE CASE:
 * Managing product inventory in e-commerce system
 * Similar to state management in Angular (NgRx store)
 *
 * TYPESCRIPT COMPARISON:
 * TypeScript:
 * private products: Map<string, Product> = new Map();
 * private inventory = new Map<string, number>();
 *
 * Java:
 * private HashMap<String, Product> products = new HashMap<>();
 * private HashMap<String, Integer> inventory = new HashMap<>();
 *
 * Very similar Map/HashMap concept!
 */
public class InventoryManager<T extends Product> {

    // ============================================
    // GENERICS (#66)
    // ============================================

    /**
     * GENERICS (#66)
     *
     * Generic type parameter <T> makes class reusable with different types
     * Similar to TypeScript generics!
     *
     * TypeScript:
     * class Manager<T> {
     *   private items: T[] = [];
     *   add(item: T): void { ... }
     * }
     *
     * Java:
     * class Manager<T> {
     *   private ArrayList<T> items = new ArrayList<>();
     *   public void add(T item) { ... }
     * }
     *
     * BENEFITS:
     * - Type safety at compile time
     * - Code reusability
     * - No type casting needed
     * - Catches errors early
     *
     * REAL-WORLD USE:
     * - Collections (ArrayList<String>, HashMap<Integer, User>)
     * - Repository patterns (Repository<User>, Repository<Product>)
     * - API responses (Response<T>)
     * - Optional values (Optional<T>)
     * Frequency: Extremely common - used daily!
     */

    // ============================================
    // HASHMAPS (#67)
    // ============================================

    /**
     * HASHMAPS (#67)
     *
     * Key-value pairs - like JavaScript/TypeScript Map or object
     * Fast lookup by key (O(1) average time complexity)
     *
     * TypeScript:
     * const map = new Map<string, Product>();
     * map.set("key", product);
     * const product = map.get("key");
     *
     * Java:
     * HashMap<String, Product> map = new HashMap<>();
     * map.put("key", product);
     * Product product = map.get("key");
     *
     * REAL-WORLD USE:
     * - Caching data
     * - Quick lookups (user by ID, product by SKU)
     * - Counting occurrences
     * - Configuration settings
     * Frequency: Very common (daily)
     */

    private HashMap<String, T> productsById;           // Product ID -> Product
    private HashMap<ProductCategory, ArrayList<T>> productsByCategory; // Category -> Products

    // ============================================
    // CONSTRUCTOR
    // ============================================

    public InventoryManager() {
        this.productsById = new HashMap<>();
        this.productsByCategory = new HashMap<>();

        // Initialize category lists
        for (ProductCategory category : ProductCategory.values()) {
            productsByCategory.put(category, new ArrayList<>());
        }
    }

    // ============================================
    // HASHMAP OPERATIONS
    // ============================================

    /**
     * Add product to inventory
     * Demonstrates HashMap.put()
     */
    public void addProduct(T product) {
        // Add to products map
        productsById.put(product.getProductId(), product);

        // Add to category map
        ArrayList<T> categoryProducts = productsByCategory.get(product.getCategory());
        if (categoryProducts != null) {
            categoryProducts.add(product);
        }

        System.out.println("✓ Product added: " + product.getName());
    }

    /**
     * Get product by ID
     * Demonstrates HashMap.get()
     */
    public T getProduct(String productId) {
        return productsById.get(productId);
    }

    /**
     * Remove product
     * Demonstrates HashMap.remove()
     */
    public T removeProduct(String productId) {
        T product = productsById.remove(productId);

        if (product != null) {
            ArrayList<T> categoryProducts = productsByCategory.get(product.getCategory());
            categoryProducts.remove(product);
            System.out.println("✓ Product removed: " + product.getName());
        }

        return product;
    }

    /**
     * Check if product exists
     * Demonstrates HashMap.containsKey()
     */
    public boolean hasProduct(String productId) {
        return productsById.containsKey(productId);
    }

    /**
     * Get all products
     * Demonstrates HashMap.values()
     */
    public ArrayList<T> getAllProducts() {
        return new ArrayList<>(productsById.values());
    }

    /**
     * Get products by category
     */
    public ArrayList<T> getProductsByCategory(ProductCategory category) {
        return new ArrayList<>(productsByCategory.get(category));
    }

    /**
     * Get product count
     * Demonstrates HashMap.size()
     */
    public int getProductCount() {
        return productsById.size();
    }

    /**
     * Search products by name
     */
    public ArrayList<T> searchProducts(String searchTerm) {
        ArrayList<T> results = new ArrayList<>();
        String lowerSearch = searchTerm.toLowerCase();

        // Iterate through HashMap values
        for (T product : productsById.values()) {
            if (product.getName().toLowerCase().contains(lowerSearch) ||
                product.getDescription().toLowerCase().contains(lowerSearch)) {
                results.add(product);
            }
        }

        return results;
    }

    /**
     * Get low stock products
     */
    public ArrayList<T> getLowStockProducts() {
        ArrayList<T> lowStock = new ArrayList<>();

        for (T product : productsById.values()) {
            if (product.isLowStock()) {
                lowStock.add(product);
            }
        }

        return lowStock;
    }

    /**
     * Get out of stock products
     */
    public ArrayList<T> getOutOfStockProducts() {
        ArrayList<T> outOfStock = new ArrayList<>();

        for (T product : productsById.values()) {
            if (!product.isInStock()) {
                outOfStock.add(product);
            }
        }

        return outOfStock;
    }

    /**
     * Get products by brand
     */
    public ArrayList<T> getProductsByBrand(String brand) {
        ArrayList<T> results = new ArrayList<>();

        for (T product : productsById.values()) {
            if (product.getBrand().equalsIgnoreCase(brand)) {
                results.add(product);
            }
        }

        return results;
    }

    /**
     * Calculate total inventory value
     */
    public double getTotalInventoryValue() {
        double total = 0;

        for (T product : productsById.values()) {
            total += product.getPrice() * product.getStockQuantity();
        }

        return total;
    }

    /**
     * Get category statistics
     * Demonstrates advanced HashMap usage
     */
    public HashMap<ProductCategory, Integer> getCategoryStatistics() {
        HashMap<ProductCategory, Integer> stats = new HashMap<>();

        for (ProductCategory category : ProductCategory.values()) {
            stats.put(category, productsByCategory.get(category).size());
        }

        return stats;
    }

    // ============================================
    // DISPLAY METHODS
    // ============================================

    /**
     * Display all products
     */
    public void displayAllProducts() {
        if (productsById.isEmpty()) {
            System.out.println("No products in inventory");
            return;
        }

        System.out.println("\n=== ALL PRODUCTS ===");
        for (T product : productsById.values()) {
            System.out.println("  " + product);
        }
    }

    /**
     * Display products by category
     */
    public void displayProductsByCategory() {
        System.out.println("\n=== PRODUCTS BY CATEGORY ===");

        for (ProductCategory category : ProductCategory.values()) {
            ArrayList<T> products = productsByCategory.get(category);

            System.out.println("\n" + category + " (" + products.size() + " products):");

            if (products.isEmpty()) {
                System.out.println("  (none)");
            } else {
                for (T product : products) {
                    System.out.println("  - " + product.getName() +
                                      " ($" + String.format("%.2f", product.getPrice()) + ")");
                }
            }
        }
    }

    /**
     * Display inventory statistics
     */
    public void displayStatistics() {
        System.out.println("\n=== INVENTORY STATISTICS ===");
        System.out.println("Total products: " + productsById.size());
        System.out.println("Total inventory value: $" +
                          String.format("%.2f", getTotalInventoryValue()));
        System.out.println("Low stock items: " + getLowStockProducts().size());
        System.out.println("Out of stock items: " + getOutOfStockProducts().size());

        System.out.println("\nProducts by category:");
        HashMap<ProductCategory, Integer> stats = getCategoryStatistics();
        for (Map.Entry<ProductCategory, Integer> entry : stats.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
    }

    /* HASHMAP METHODS SUMMARY (vs TypeScript Map):
     *
     * Java HashMap                  | TypeScript Map
     * ------------------------------|----------------------------
     * map.put(key, value)           | map.set(key, value)
     * map.get(key)                  | map.get(key)
     * map.remove(key)               | map.delete(key)
     * map.containsKey(key)          | map.has(key)
     * map.size()                    | map.size
     * map.isEmpty()                 | map.size === 0
     * map.clear()                   | map.clear()
     * map.keySet()                  | map.keys()
     * map.values()                  | map.values()
     * map.entrySet()                | map.entries()
     *
     * Very similar APIs!
     */

    /* GENERICS BEST PRACTICES:
     *
     * 1. Type Bounds:
     *    <T extends Product> - T must be Product or subclass
     *    <T super Integer> - T must be Integer or superclass
     *
     * 2. Multiple Type Parameters:
     *    class Pair<K, V> { ... }
     *    HashMap<String, Integer> map = new HashMap<>();
     *
     * 3. Wildcards:
     *    List<?> - list of unknown type
     *    List<? extends Number> - list of Number or subclass
     *
     * 4. Generic Methods:
     *    public <T> T findItem(List<T> items, Predicate<T> condition)
     *
     * REAL-WORLD USES:
     * - Spring Data JPA: JpaRepository<User, Long>
     * - Optional: Optional<User>
     * - Collections: List<String>, Set<Integer>
     * - API responses: Response<T>
     *
     * Frequency: Used everywhere in Java!
     */
}
