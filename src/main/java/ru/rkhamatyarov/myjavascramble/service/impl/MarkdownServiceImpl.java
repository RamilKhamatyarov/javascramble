package ru.rkhamatyarov.myjavascramble.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rkhamatyarov.myjavascramble.entity.MarkdownFile;
import ru.rkhamatyarov.myjavascramble.service.MarkdownService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Service implementation for markdown file operations.
 */
@Service
public final class MarkdownServiceImpl implements MarkdownService {

    /**
     * Directory path for markdown files from application properties.
     */
    @Value("${app.markdown.directory}")
    private String markdownDirectory;

    /**
     * Maximum depth for recursive directory search.
     */
    private static final int MAX_SEARCH_DEPTH = 5;

    /**
     * Supported image file extensions.
     */
    private static final List<String> IMAGE_EXTENSIONS = List.of(
            ".png", ".jpg", ".jpeg", ".gif", ".bmp", ".webp"
    );

    /**
     * Get all markdown files from configured directory recursively up
     * to 5 levels.
     *
     * @return list of markdown files
     */
    @Override
    public List<MarkdownFile> getAllMarkdownFiles() {
        try {
            Path directoryPath = Paths.get(markdownDirectory);

            if (!Files.exists(directoryPath)
                    || !Files.isDirectory(directoryPath)) {
                throw new IllegalArgumentException(
                        "Markdown directory does not exist or is not a "
                                + "directory: " + markdownDirectory
                );
            }

            List<MarkdownFile> markdownFiles = new ArrayList<>();
            searchMarkdownFilesRecursively(
                    directoryPath, markdownFiles, 0
            );

            return markdownFiles;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error reading markdown files from directory: "
                            + markdownDirectory, e
            );
        }
    }

    /**
     * Get all image files from configured directory recursively up
     * to 5 levels.
     *
     * @return list of image files
     */
    @Override
    public List<MarkdownFile> getAllImageFiles() {
        try {
            Path directoryPath = Paths.get(markdownDirectory);

            if (!Files.exists(directoryPath)
                    || !Files.isDirectory(directoryPath)) {
                throw new IllegalArgumentException(
                        "Markdown directory does not exist or is not a "
                                + "directory: " + markdownDirectory
                );
            }

            List<MarkdownFile> imageFiles = new ArrayList<>();
            searchImageFilesRecursively(
                    directoryPath, imageFiles, 0
            );

            return imageFiles;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error reading image files from directory: "
                            + markdownDirectory, e
            );
        }
    }

    /**
     * Recursively search for markdown files up to maximum depth.
     *
     * @param directory    current directory to search
     * @param files        list to collect found files
     * @param currentDepth current search depth
     */
    private void searchMarkdownFilesRecursively(
            final Path directory,
            final List<MarkdownFile> files,
            final int currentDepth
    ) {
        if (currentDepth >= MAX_SEARCH_DEPTH) {
            return;
        }

        try (Stream<Path> pathStream = Files.list(directory)) {
            List<Path> paths = pathStream.toList();

            for (Path path : paths) {
                if (Files.isRegularFile(path)
                        && path.toString().toLowerCase().endsWith(".md")) {
                    files.add(new MarkdownFile(path, directory));
                } else if (Files.isDirectory(path)) {
                    searchMarkdownFilesRecursively(
                            path, files, currentDepth + 1
                    );
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error searching directory: " + directory, e
            );
        }
    }

    /**
     * Recursively search for image files up to maximum depth.
     *
     * @param directory    current directory to search
     * @param files        list to collect found files
     * @param currentDepth current search depth
     */
    private void searchImageFilesRecursively(
            final Path directory,
            final List<MarkdownFile> files,
            final int currentDepth
    ) {
        if (currentDepth >= MAX_SEARCH_DEPTH) {
            return;
        }

        try (Stream<Path> pathStream = Files.list(directory)) {
            List<Path> paths = pathStream.toList();

            for (Path path : paths) {
                if (Files.isRegularFile(path) && isImageFile(path)) {
                    files.add(new MarkdownFile(path, directory));
                } else if (Files.isDirectory(path)) {
                    searchImageFilesRecursively(
                            path, files, currentDepth + 1
                    );
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error searching directory: " + directory, e
            );
        }
    }

    /**
     * Check if file is an image by extension.
     *
     * @param filePath the file path to check
     * @return true if file is an image, false otherwise
     */
    private boolean isImageFile(final Path filePath) {
        String fileName = filePath.getFileName().toString().toLowerCase();
        return IMAGE_EXTENSIONS.stream().anyMatch(fileName::endsWith);
    }

    /**
     * Get markdown file content by filepath (relative to markdown
     * directory).
     *
     * @param filepath the markdown filepath (can include subdirectories)
     * @return markdown file content or empty string if file not found
     */
    @Override
    public String getMarkdownFileContent(final String filepath) {
        try {
            Path filePath = Paths.get(markdownDirectory, filepath);

            if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
                return "";
            }

            Path normalizedFilePath = filePath.normalize();
            Path normalizedMarkdownDir =
                    Paths.get(markdownDirectory).normalize();

            if (!normalizedFilePath.startsWith(normalizedMarkdownDir)) {
                throw new SecurityException(
                        "Access to file outside markdown directory is not allowed"
                );
            }

            return Files.readString(filePath);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error reading markdown file: "
                            + filepath, e
            );
        }
    }

    /**
     * Get image file as byte array by filepath (relative to markdown
     * directory).
     *
     * @param filepath the image filepath (can include subdirectories)
     * @return image file bytes or empty array if file not found
     */
    @Override
    public byte[] getImageFileContent(final String filepath) {
        try {
            Path filePath = Paths.get(markdownDirectory, filepath);

            if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
                return new byte[0];
            }

            if (!isImageFile(filePath)) {
                throw new SecurityException(
                        "File is not a supported image type"
                );
            }

            Path normalizedFilePath = filePath.normalize();
            Path normalizedMarkdownDir =
                    Paths.get(markdownDirectory).normalize();

            if (!normalizedFilePath.startsWith(normalizedMarkdownDir)) {
                throw new SecurityException(
                        "Access to file outside markdown directory is not allowed"
                );
            }

            return Files.readAllBytes(filePath);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error reading image file: " + filepath, e
            );
        }
    }

    /**
     * Get all files (markdown and images) from configured directory
     * recursively.
     *
     * @return list of all supported files
     */
    @Override
    public List<MarkdownFile> getAllFiles() {
        List<MarkdownFile> allFiles = new ArrayList<>();
        allFiles.addAll(getAllMarkdownFiles());
        allFiles.addAll(getAllImageFiles());
        return allFiles;
    }
}
