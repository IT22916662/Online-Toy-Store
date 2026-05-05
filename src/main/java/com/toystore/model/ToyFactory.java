package com.toystore.model;

/**
 * Centralised builder for {@link Toy} instances. Keeps the rest of the
 * codebase free from <code>switch</code> statements on category strings
 * and ensures the file format and runtime types stay aligned.
 */
public class ToyFactory {

    private ToyFactory() {
    }

    /** Build a toy from a UI form / servlet request. */
    public static Toy create(String type, String id, String name, double price,
                             int stock, String ageGroup, String extra) {
        switch (type) {
            case ElectronicToy.TYPE:
                return new ElectronicToy(id, name, price, stock, ageGroup,
                        Boolean.parseBoolean(extra));
            case EducationalToy.TYPE:
                return new EducationalToy(id, name, price, stock, ageGroup, extra);
            case SoftToy.TYPE:
                return new SoftToy(id, name, price, stock, ageGroup, extra);
            default:
                throw new IllegalArgumentException("Unknown toy type: " + type);
        }
    }

    /** Reconstruct a toy from one line of <code>toys.txt</code>. */
    public static Toy fromFileLine(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 7) {
            throw new IllegalArgumentException("Malformed toy line: " + line);
        }
        String id = parts[0];
        String type = parts[1];
        String name = parts[2];
        double price = Double.parseDouble(parts[3]);
        int stock = Integer.parseInt(parts[4]);
        String ageGroup = parts[5];
        String extra = parts[6];
        return create(type, id, name, price, stock, ageGroup, extra);
    }
}
