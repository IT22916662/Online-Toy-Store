package com.toystore.dao;

import com.toystore.model.Toy;
import com.toystore.model.ToyFactory;
import com.toystore.util.FileHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for {@link Toy}.
 * <p>
 * Owns every CRUD operation against the toys data file. The rest of the app
 * goes through this class so file I/O and parsing rules stay in one place.
 */
public class ToyDAO {

    private final FileHandler fileHandler;

    public ToyDAO(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    /** CREATE — append the toy as a new line. */
    public void add(Toy toy) {
        if (findById(toy.getId()).isPresent()) {
            throw new IllegalArgumentException("Toy ID already exists: " + toy.getId());
        }
        fileHandler.appendLine(toy.toFileLine());
    }

    /** READ — load every toy from disk. */
    public List<Toy> findAll() {
        List<Toy> toys = new ArrayList<>();
        for (String line : fileHandler.readAllLines()) {
            toys.add(ToyFactory.fromFileLine(line));
        }
        return toys;
    }

    /** READ — locate one toy by ID. */
    public Optional<Toy> findById(String id) {
        for (Toy toy : findAll()) {
            if (toy.getId().equalsIgnoreCase(id)) {
                return Optional.of(toy);
            }
        }
        return Optional.empty();
    }

    /** READ — case-insensitive search by name fragment and/or category. */
    public List<Toy> search(String nameQuery, String category) {
        List<Toy> matches = new ArrayList<>();
        String name = nameQuery == null ? "" : nameQuery.trim().toLowerCase();
        String cat = category == null ? "" : category.trim();
        for (Toy toy : findAll()) {
            boolean nameMatch = name.isEmpty()
                    || toy.getName().toLowerCase().contains(name);
            boolean categoryMatch = cat.isEmpty()
                    || toy.getCategory().equalsIgnoreCase(cat);
            if (nameMatch && categoryMatch) {
                matches.add(toy);
            }
        }
        return matches;
    }

    /** UPDATE — replace the toy whose ID matches; returns true if a row was changed. */
    public boolean update(Toy updated) {
        List<Toy> toys = findAll();
        boolean found = false;
        for (int i = 0; i < toys.size(); i++) {
            if (toys.get(i).getId().equalsIgnoreCase(updated.getId())) {
                toys.set(i, updated);
                found = true;
                break;
            }
        }
        if (found) {
            persist(toys);
        }
        return found;
    }

    /** DELETE — remove the toy with the given ID; returns true if a row was removed. */
    public boolean delete(String id) {
        List<Toy> toys = findAll();
        boolean removed = toys.removeIf(t -> t.getId().equalsIgnoreCase(id));
        if (removed) {
            persist(toys);
        }
        return removed;
    }

    /** Generates the next sequential ID (T001, T002, …). */
    public String nextId() {
        int max = 0;
        for (Toy toy : findAll()) {
            String id = toy.getId();
            if (id != null && id.startsWith("T")) {
                try {
                    int n = Integer.parseInt(id.substring(1));
                    if (n > max) {
                        max = n;
                    }
                } catch (NumberFormatException ignored) {
                    // skip malformed IDs
                }
            }
        }
        return String.format("T%03d", max + 1);
    }

    private void persist(List<Toy> toys) {
        List<String> lines = new ArrayList<>();
        for (Toy toy : toys) {
            lines.add(toy.toFileLine());
        }
        fileHandler.writeAllLines(lines);
    }
}
