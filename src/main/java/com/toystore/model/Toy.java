package com.toystore.model;

/**
 * Abstract base class for every toy in the catalog.
 * Demonstrates ENCAPSULATION (private fields exposed via getters/setters)
 * and ABSTRACTION (abstract methods that subclasses must implement).
 */
public abstract class Toy {

    private String id;
    private String name;
    private double price;
    private int stock;
    private String ageGroup;

    public Toy() {
    }

    public Toy(String id, String name, double price, int stock, String ageGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.ageGroup = ageGroup;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        this.stock = stock;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    /** Each subclass declares which catalog category it belongs to. */
    public abstract String getCategory();

    /** Category-specific discount logic. Demonstrates polymorphism. */
    public abstract double calculateDiscount();

    /** Subclass-specific extra attribute, serialized into the data file. */
    public abstract String getExtraField();

    /** Selling price after the category discount has been applied. */
    public double getDiscountedPrice() {
        return price - calculateDiscount();
    }

    /** Serializes this toy to a single pipe-delimited line. */
    public String toFileLine() {
        return String.join("|",
                id,
                getCategory(),
                name,
                String.valueOf(price),
                String.valueOf(stock),
                ageGroup,
                getExtraField());
    }
}
