package com.toystore.model;

/**
 * Plush / stuffed toys (teddy bears, soft animals).
 * Stores the fabric or filling material used.
 */
public class SoftToy extends Toy {

    public static final String TYPE = "SOFT";

    private String material;

    public SoftToy() {
    }

    public SoftToy(String id, String name, double price, int stock,
                   String ageGroup, String material) {
        super(id, name, price, stock, ageGroup);
        this.material = material;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    @Override
    public String getCategory() {
        return TYPE;
    }

    /** Soft toys get a 5% discount. */
    @Override
    public double calculateDiscount() {
        return getPrice() * 0.05;
    }

    @Override
    public String getExtraField() {
        return material;
    }
}
