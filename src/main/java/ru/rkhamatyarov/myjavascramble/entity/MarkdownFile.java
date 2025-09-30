package ru.rkhamatyarov.myjavascramble.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Entity representing a markdown file.
 */
public class MarkdownFile {

    private String filename;
    private String filePath;
    private long fileSize;
    private LocalDateTime lastModified;

    public MarkdownFile() {
    }

    public MarkdownFile(Path filePath) {
        try {
            this.filename = filePath.getFileName().toString();
            this.filePath = filePath.toString();

            BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
            this.fileSize = attrs.size();
            this.lastModified = LocalDateTime.ofInstant(
                    attrs.lastModifiedTime().toInstant(), ZoneId.systemDefault()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error reading file attributes", e);
        }
    }

    // Getters and setters
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }
}