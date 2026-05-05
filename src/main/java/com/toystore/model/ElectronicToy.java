package com.toystore.model;

/**
 * Toys that run on batteries or electricity (remote cars, robots, etc.).
 * Inherits all common fields from {@link Toy} and adds battery information.
 */
public class ElectronicToy extends Toy {

    public static final String TYPE = "ELECTRONIC";

    private boolean needsBattery;

    public ElectronicToy() {
    }

    public ElectronicToy(String id, String name, double price, int stock,
                         String ageGroup, boolean needsBattery) {
        super(id, name, price, stock, ageGroup);
        this.needsBattery = needsBattery;
    }

    public boolean isNeedsBattery() {
        return needsBattery;
    }

    public void setNeedsBattery(boolean needsBattery) {
        this.needsBattery = needsBattery;
    }

    @Override
    public String getCategory() {
        return TYPE;
    }

    /** Electronics get a flat 10% discount. */
    @Override
    public double calculateDiscount() {
        return getPrice() * 0.10;
    }

    @Override
    public String getExtraField() {
        return String.valueOf(needsBattery);
    }
}
