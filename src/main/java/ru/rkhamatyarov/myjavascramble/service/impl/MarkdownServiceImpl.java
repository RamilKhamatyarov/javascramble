package ru.rkhamatyarov.myjavascramble.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rkhamatyarov.myjavascramble.entity.MarkdownFile;
import ru.rkhamatyarov.myjavascramble.service.MarkdownService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
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
     * Get all markdown files from configured directory.
     *
     * @return list of markdown files
     */
    @Override
    public List<MarkdownFile> getAllMarkdownFiles() {
        try {
            Path directoryPath = Paths.get(markdownDirectory);

            if (!Files.exists(directoryPath) || !Files.isDirectory(directoryPath)) {
                throw new IllegalArgumentException(
                        "Markdown directory does not exist or is not a directory: " + markdownDirectory
                );
            }

            try (Stream<Path> pathStream = Files.list(directoryPath)) {
                return pathStream
                        .filter(path -> Files.isRegularFile(path) &&
                                path.toString().toLowerCase().endsWith(".md"))
                        .map(MarkdownFile::new)
                        .collect(Collectors.toList());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error reading markdown files from directory: " + markdownDirectory, e);
        }
    }

    /**
     * Get markdown file content by filename.
     *
     * @param filename the markdown filename
     * @return markdown file content or empty string if file not found
     */
    @Override
    public String getMarkdownFileContent(String filename) {
        try {
            Path filePath = Paths.get(markdownDirectory, filename);

            if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
                return "";
            }

            // Security check: ensure the file is within the markdown directory
            Path normalizedFilePath = filePath.normalize();
            Path normalizedMarkdownDir = Paths.get(markdownDirectory).normalize();

            if (!normalizedFilePath.startsWith(normalizedMarkdownDir)) {
                throw new SecurityException("Access to file outside markdown directory is not allowed");
            }

            return Files.readString(filePath);

        } catch (Exception e) {
            throw new RuntimeException("Error reading markdown file: " + filename, e);
        }
    }
}
