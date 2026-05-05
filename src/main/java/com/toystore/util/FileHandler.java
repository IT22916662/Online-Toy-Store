package com.toystore.util;

import jakarta.servlet.ServletContext;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic text-file utility used as the storage layer.
 * Handles low-level read / write / append operations against a single file
 * so the DAO classes can stay focused on domain logic.
 */
public class FileHandler {

    private final String filePath;

    public FileHandler(String filePath) {
        this.filePath = filePath;
        ensureFileExists();
    }

    /**
     * Resolves the data file inside the deployed webapp so it survives redeploys
     * and works whether the app is run from IntelliJ or a packaged WAR.
     */
    public static FileHandler forContext(ServletContext context, String relativePath) {
        String dataDir = context.getRealPath("/WEB-INF/data");
        File dir = new File(dataDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new FileHandler(new File(dir, relativePath).getAbsolutePath());
    }

    public String getFilePath() {
        return filePath;
    }

    /** Reads every non-blank line from the file. */
    public List<String> readAllLines() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read " + filePath, e);
        }
        return lines;
    }

    /** Appends a single line followed by a newline. */
    public void appendLine(String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to append to " + filePath, e);
        }
    }

    /** Replaces the entire file contents with the given lines. */
    public void writeAllLines(List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write " + filePath, e);
        }
    }

    private void ensureFileExists() {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create " + filePath, e);
        }
    }
}
