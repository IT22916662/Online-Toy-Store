package com.toystore.model;

/**
 * Learning-focused toys (puzzles, science kits, building blocks).
 * Carries the skill the toy is intended to develop.
 */
public class EducationalToy extends Toy {

    public static final String TYPE = "EDUCATIONAL";

    private String skillType;

    public EducationalToy() {
    }

    public EducationalToy(String id, String name, double price, int stock,
                          String ageGroup, String skillType) {
        super(id, name, price, stock, ageGroup);
        this.skillType = skillType;
    }

    public String getSkillType() {
        return skillType;
    }

    public void setSkillType(String skillType) {
        this.skillType = skillType;
    }

    @Override
    public String getCategory() {
        return TYPE;
    }

    /** Educational toys get a 15% discount to encourage learning purchases. */
    @Override
    public double calculateDiscount() {
        return getPrice() * 0.15;
    }

    @Override
    public String getExtraField() {
        return skillType;
    }
}
