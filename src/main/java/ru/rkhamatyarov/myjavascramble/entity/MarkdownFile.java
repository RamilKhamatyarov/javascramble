package ru.rkhamatyarov.myjavascramble.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Entity representing a markdown file.
 *
 * @param filename      the name of the file
 * @param filePath      the full path to the file
 * @param relativePath  the relative path to the file from base directory
 * @param fileSize      the size of the file in bytes
 * @param lastModified  the last modified time of the file
 */
public record MarkdownFile(
        String filename,
        String filePath,
        String relativePath,
        long fileSize,
        LocalDateTime lastModified
) {
    /** Constant for bytes to KB conversion. */
    private static final int BYTES_PER_KB = 1024;
    /** Constant for bytes to MB conversion. */
    private static final int BYTES_PER_MB = 1024 * 1024;
    /** Constant for KB to MB conversion (as double). */
    private static final double KB_TO_MB_DIVISOR = 1024.0;

    /**
     * Compact constructor to validate parameters.
     *
     * @param filename      the name of the file
     * @param filePath      the full path to the file
     * @param relativePath  the relative path to the file from base directory
     * @param fileSize      the size of the file in bytes
     * @param lastModified  the last modified time of the file
     */
    public MarkdownFile {
        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException(
                    "Filename cannot be null or blank."
            );
        }
        if (fileSize < 0) {
            throw new IllegalArgumentException(
                    "File size cannot be negative."
            );
        }
    }

    /**
     * Constructor that handles both cases with optional basePath.
     *
     * @param pathToFile the path to the file
     * @param basePath   the base path for relative path calculation
     *                   (can be null)
     */
    public MarkdownFile(final Path pathToFile, final Path basePath) {
        this(
                pathToFile.getFileName().toString(),
                pathToFile.toString(),
                basePath != null
                        ? basePath.relativize(pathToFile).toString()
                        : pathToFile.getFileName().toString(),
                getFileSize(pathToFile),
                getLastModified(pathToFile)
        );
    }

    /**
     * Convenience constructor without basePath.
     *
     * @param pathToFile the path to the file
     */
    public MarkdownFile(final Path pathToFile) {
        this(pathToFile, null);
    }

    /**
     * Get file size from path.
     *
     * @param pathToFile the path to the file
     * @return the file size in bytes
     * @throws RuntimeException if unable to read file attributes
     */
    private static long getFileSize(final Path pathToFile) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(
                    pathToFile, BasicFileAttributes.class
            );
            return attrs.size();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error reading file size for: " + pathToFile, e
            );
        }
    }

    /**
     * Get last modified time from path.
     *
     * @param pathToFile the path to the file
     * @return the last modified time as LocalDateTime
     * @throws RuntimeException if unable to read file attributes
     */
    private static LocalDateTime getLastModified(final Path pathToFile) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(
                    pathToFile, BasicFileAttributes.class
            );
            return LocalDateTime.ofInstant(
                    attrs.lastModifiedTime().toInstant(),
                    ZoneId.systemDefault()
            );
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error reading last modified time for: " + pathToFile, e
            );
        }
    }

    /**
     * Get formatted file size for display.
     *
     * @return formatted file size string
     */
    public String getFormattedFileSize() {
        if (fileSize < BYTES_PER_KB) {
            return fileSize + " B";
        } else if (fileSize < BYTES_PER_MB) {
            return String.format("%.1f KB", fileSize / KB_TO_MB_DIVISOR);
        } else {
            return String.format("%.1f MB",
                    fileSize / (KB_TO_MB_DIVISOR * KB_TO_MB_DIVISOR));
        }
    }

    /**
     * Get display name (prefers relative path, falls back to filename).
     *
     * @return display name
     */
    public String getDisplayName() {
        return relativePath != null ? relativePath : filename;
    }
}
