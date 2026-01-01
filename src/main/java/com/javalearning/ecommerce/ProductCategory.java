package com.javalearning.ecommerce;

/**
 * Product Category Enum
 * Demonstrates enum usage in e-commerce context
 */
public enum ProductCategory {
    ELECTRONICS("Electronics", 0.10),      // 10% tax
    CLOTHING("Clothing", 0.08),            // 8% tax
    FOOD("Food", 0.05),                    // 5% tax
    BOOKS("Books", 0.00),                  // Tax-free
    HOME_GARDEN("Home & Garden", 0.08),
    SPORTS("Sports & Outdoors", 0.08);

    private final String displayName;
    private final double taxRate;

    ProductCategory(String displayName, double taxRate) {
        this.displayName = displayName;
        this.taxRate = taxRate;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getTaxRate() {
        return taxRate;
    }

    /**
     * Calculate tax for given price
     */
    public double calculateTax(double price) {
        return price * taxRate;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
