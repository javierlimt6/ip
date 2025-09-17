package friday;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Manages tags for tasks using a separate text file.
 * Tag format in file: taskIndex:tag1,tag2,tag3
 */
public class TagManager {
    private final Path tagFile;
    private final Map<Integer, Set<String>> tagMap;

    /**
     * Constructs a TagManager with the given tag file path.
     *
     * @param tagFile The path to the tag file.
     */
    public TagManager(Path tagFile) {
        this.tagFile = tagFile;
        this.tagMap = new HashMap<>();
        loadTags();
    }

    /**
     * Loads tags from the tag file.
     */
    private void loadTags() {
        if (!Files.exists(tagFile)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(tagFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                parseTagLine(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error loading tags: " + e.getMessage());
        }
    }

    /**
     * Parses a single line from the tag file.
     * Format: taskIndex:tag1,tag2,tag3
     *
     * @param line The line to parse.
     */
    private void parseTagLine(String line) {
        if (line.isEmpty()) {
            return;
        }

        int colonIndex = line.indexOf(':');
        if (colonIndex == -1) {
            return; // Invalid format
        }

        try {
            int taskIndex = Integer.parseInt(line.substring(0, colonIndex).trim());
            String tagsPart = line.substring(colonIndex + 1).trim();

            if (!tagsPart.isEmpty()) {
                Set<String> tags = new HashSet<>();
                String[] tagArray = tagsPart.split(",");
                for (String tag : tagArray) {
                    String trimmedTag = tag.trim();
                    if (!trimmedTag.isEmpty()) {
                        tags.add(trimmedTag.toLowerCase());
                    }
                }
                tagMap.put(taskIndex, tags);
            }
        } catch (NumberFormatException e) {
            // Invalid task index, skip this line
        }
    }

    /**
     * Saves all tags to the tag file.
     */
    public void saveTags() {
        try {
            Files.createDirectories(tagFile.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(tagFile)) {
                for (Map.Entry<Integer, Set<String>> entry : tagMap.entrySet()) {
                    if (!entry.getValue().isEmpty()) {
                        writer.write(entry.getKey() + ":");
                        String tagsStr = String.join(",", entry.getValue());
                        writer.write(tagsStr);
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving tags: " + e.getMessage());
        }
    }

    /**
     * Adds a tag to a task.
     *
     * @param taskIndex The 1-based index of the task.
     * @param tag       The tag to add (without # prefix).
     */
    public void addTag(int taskIndex, String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            return;
        }

        tagMap.computeIfAbsent(taskIndex, k -> new HashSet<>())
                .add(tag.trim().toLowerCase());
        saveTags();
    }

    /**
     * Removes a tag from a task.
     *
     * @param taskIndex The 1-based index of the task.
     * @param tag       The tag to remove (without # prefix).
     */
    public void removeTag(int taskIndex, String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            return;
        }

        Set<String> tags = tagMap.get(taskIndex);
        if (tags != null) {
            tags.remove(tag.trim().toLowerCase());
            if (tags.isEmpty()) {
                tagMap.remove(taskIndex);
            }
            saveTags();
        }
    }

    /**
     * Gets all tags for a task.
     *
     * @param taskIndex The 1-based index of the task.
     * @return The set of tags for the task.
     */
    public Set<String> getTags(int taskIndex) {
        return tagMap.getOrDefault(taskIndex, new HashSet<>());
    }

    /**
     * Checks if a task has a specific tag.
     *
     * @param taskIndex The 1-based index of the task.
     * @param tag       The tag to check (without # prefix).
     * @return True if the task has the tag, false otherwise.
     */
    public boolean hasTag(int taskIndex, String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            return false;
        }

        Set<String> tags = tagMap.get(taskIndex);
        return tags != null && tags.contains(tag.trim().toLowerCase());
    }

    /**
     * Gets the display string for tags of a task.
     *
     * @param taskIndex The 1-based index of the task.
     * @return The tag display string (e.g., " #fun #work").
     */
    public String getTagDisplayString(int taskIndex) {
        Set<String> tags = getTags(taskIndex);
        if (tags.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (String tag : tags) {
            sb.append(" #").append(tag);
        }
        return sb.toString();
    }
}
