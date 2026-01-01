package com.javalearning.ecommerce;

import java.util.HashMap;
import java.util.Map;

/**
 * SHOPPING CART
 *
 * Demonstrates advanced HashMap usage
 * Product ID -> Quantity mapping
 *
 * REAL-WORLD USE CASE:
 * E-commerce shopping cart functionality
 * Similar to cart state management in Angular
 */
public class ShoppingCart {

    // ============================================
    // FIELDS
    // ============================================

    private String cartId;
    private HashMap<String, Integer> items;  // Product ID -> Quantity
    private InventoryManager<Product> inventory;

    private static int cartCounter = 1;

    // ============================================
    // CONSTRUCTOR
    // ============================================

    public ShoppingCart(InventoryManager<Product> inventory) {
        this.cartId = "CART" + String.format("%04d", cartCounter++);
        this.items = new HashMap<>();
        this.inventory = inventory;
    }

    // ============================================
    // CART OPERATIONS
    // ============================================

    /**
     * Add item to cart
     */
    public boolean addItem(String productId, int quantity) {
        Product product = inventory.getProduct(productId);

        if (product == null) {
            System.out.println("❌ Product not found");
            return false;
        }

        if (!product.isInStock()) {
            System.out.println("❌ Product out of stock");
            return false;
        }

        if (quantity > product.getStockQuantity()) {
            System.out.println("❌ Not enough stock (available: " +
                              product.getStockQuantity() + ")");
            return false;
        }

        // Update quantity if already in cart
        items.put(productId, items.getOrDefault(productId, 0) + quantity);

        System.out.println("✓ Added to cart: " + product.getName() +
                          " (quantity: " + quantity + ")");
        return true;
    }

    /**
     * Remove item from cart
     */
    public void removeItem(String productId) {
        if (items.containsKey(productId)) {
            items.remove(productId);
            System.out.println("✓ Item removed from cart");
        } else {
            System.out.println("❌ Item not in cart");
        }
    }

    /**
     * Update item quantity
     */
    public boolean updateQuantity(String productId, int newQuantity) {
        if (!items.containsKey(productId)) {
            System.out.println("❌ Item not in cart");
            return false;
        }

        Product product = inventory.getProduct(productId);
        if (newQuantity > product.getStockQuantity()) {
            System.out.println("❌ Not enough stock");
            return false;
        }

        items.put(productId, newQuantity);
        System.out.println("✓ Quantity updated");
        return true;
    }

    /**
     * Clear cart
     */
    public void clear() {
        items.clear();
        System.out.println("✓ Cart cleared");
    }

    /**
     * Get cart size (number of different products)
     */
    public int getItemCount() {
        return items.size();
    }

    /**
     * Get total quantity (sum of all quantities)
     */
    public int getTotalQuantity() {
        int total = 0;
        for (int quantity : items.values()) {
            total += quantity;
        }
        return total;
    }

    /**
     * Calculate subtotal
     */
    public double getSubtotal() {
        double subtotal = 0;

        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String productId = entry.getKey();
            int quantity = entry.getValue();

            Product product = inventory.getProduct(productId);
            if (product != null) {
                subtotal += product.getPrice() * quantity;
            }
        }

        return subtotal;
    }

    /**
     * Calculate tax
     */
    public double getTax() {
        double tax = 0;

        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String productId = entry.getKey();
            int quantity = entry.getValue();

            Product product = inventory.getProduct(productId);
            if (product != null) {
                tax += product.getCategory().calculateTax(product.getPrice()) * quantity;
            }
        }

        return tax;
    }

    /**
     * Calculate total (subtotal + tax)
     */
    public double getTotal() {
        return getSubtotal() + getTax();
    }

    /**
     * Apply discount
     */
    public double applyDiscount(double discountPercent) {
        double subtotal = getSubtotal();
        double discount = subtotal * (discountPercent / 100);
        return getTotal() - discount;
    }

    /**
     * Checkout - process order and reduce inventory
     */
    public boolean checkout() {
        if (items.isEmpty()) {
            System.out.println("❌ Cart is empty");
            return false;
        }

        // Check stock availability for all items
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            Product product = inventory.getProduct(entry.getKey());
            if (product == null || !product.reduceStock(entry.getValue())) {
                System.out.println("❌ Checkout failed - stock issue");
                return false;
            }
        }

        System.out.println("✓ Order processed successfully!");
        System.out.println("  Total: $" + String.format("%.2f", getTotal()));
        clear();
        return true;
    }

    // ============================================
    // DISPLAY METHODS
    // ============================================

    /**
     * Display cart contents
     */
    public void displayCart() {
        if (items.isEmpty()) {
            System.out.println("🛒 Cart is empty");
            return;
        }

        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║              SHOPPING CART [" + cartId + "]              ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        System.out.println("\nItems:");

        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String productId = entry.getKey();
            int quantity = entry.getValue();

            Product product = inventory.getProduct(productId);
            if (product != null) {
                double itemTotal = product.getPrice() * quantity;
                System.out.println(String.format("  • %s", product.getName()));
                System.out.println(String.format("    $%.2f × %d = $%.2f",
                    product.getPrice(), quantity, itemTotal));
            }
        }

        System.out.println("\n" + "─".repeat(50));
        System.out.println(String.format("Subtotal:    $%.2f", getSubtotal()));
        System.out.println(String.format("Tax:         $%.2f", getTax()));
        System.out.println(String.format("Total:       $%.2f", getTotal()));
        System.out.println("─".repeat(50));
        System.out.println("Total items: " + getTotalQuantity());
    }

    public HashMap<String, Integer> getItems() {
        return new HashMap<>(items);
    }
}
