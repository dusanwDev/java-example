package com.javalearning.ecommerce;

/**
 * PRODUCT CLASS
 *
 * Demonstrates:
 * - Object-oriented design
 * - Enums for categories
 * - Business logic methods
 *
 * This class will be used with Generics and HashMaps
 */
public class Product {

    // ============================================
    // FIELDS
    // ============================================

    private String productId;
    private String name;
    private String description;
    private double price;
    private int stockQuantity;
    private ProductCategory category;
    private String brand;
    private double rating;
    private int reviewCount;

    private static int productCounter = 1000;

    // ============================================
    // CONSTRUCTORS
    // ============================================

    public Product(String name, String description, double price,
                  int stockQuantity, ProductCategory category, String brand) {
        this.productId = "PROD" + (productCounter++);
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.brand = brand;
        this.rating = 0.0;
        this.reviewCount = 0;
    }

    public Product(String name, double price, int stockQuantity, ProductCategory category) {
        this(name, "", price, stockQuantity, category, "Generic");
    }

    // ============================================
    // BUSINESS METHODS
    // ============================================

    /**
     * Check if product is in stock
     */
    public boolean isInStock() {
        return stockQuantity > 0;
    }

    /**
     * Check if product is low stock
     */
    public boolean isLowStock() {
        return stockQuantity > 0 && stockQuantity < 10;
    }

    /**
     * Reduce stock (when item is purchased)
     */
    public boolean reduceStock(int quantity) {
        if (quantity > stockQuantity) {
            return false;
        }
        stockQuantity -= quantity;
        return true;
    }

    /**
     * Restock product
     */
    public void restock(int quantity) {
        stockQuantity += quantity;
        System.out.println("✓ Restocked " + name + ": +" + quantity + " units");
    }

    /**
     * Calculate price with tax
     */
    public double getPriceWithTax() {
        return price + category.calculateTax(price);
    }

    /**
     * Add rating
     */
    public void addRating(double rating) {
        double totalRating = this.rating * reviewCount;
        reviewCount++;
        this.rating = (totalRating + rating) / reviewCount;
    }

    /**
     * Calculate discount price
     */
    public double getDiscountedPrice(double discountPercent) {
        return price * (1 - discountPercent / 100);
    }

    // ============================================
    // GETTERS AND SETTERS
    // ============================================

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getRating() {
        return rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    // ============================================
    // TOSTRING
    // ============================================

    @Override
    public String toString() {
        return String.format("%s [%s] - %s | $%.2f | Stock: %d | Rating: %.1f (%d reviews)",
            productId, category, name, price, stockQuantity, rating, reviewCount);
    }

    /**
     * Detailed product information
     */
    public String getDetailedInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════╗\n");
        sb.append(String.format("  Product: %s\n", name));
        sb.append("╚══════════════════════════════════════════╝\n");
        sb.append(String.format("ID: %s\n", productId));
        sb.append(String.format("Brand: %s\n", brand));
        sb.append(String.format("Category: %s\n", category));
        sb.append(String.format("Price: $%.2f (+ $%.2f tax)\n", price, category.calculateTax(price)));
        sb.append(String.format("Stock: %d units", stockQuantity));

        if (isLowStock()) {
            sb.append(" ⚠️ LOW STOCK");
        } else if (!isInStock()) {
            sb.append(" ❌ OUT OF STOCK");
        }

        sb.append("\n");
        sb.append(String.format("Rating: %.1f/5.0 (%d reviews)\n", rating, reviewCount));
        sb.append(String.format("Description: %s", description));

        return sb.toString();
    }
}
