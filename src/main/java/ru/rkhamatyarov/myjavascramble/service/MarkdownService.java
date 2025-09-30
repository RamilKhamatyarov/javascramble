package ru.rkhamatyarov.myjavascramble.service;

import ru.rkhamatyarov.myjavascramble.entity.MarkdownFile;

import java.util.List;

/**
 * Service for markdown file operations.
 */
public interface MarkdownService {

    /**
     * Get all markdown files from configured directory.
     *
     * @return list of markdown files
     */
    List<MarkdownFile> getAllMarkdownFiles();

    /**
     * Get markdown file content by filename.
     *
     * @param filename the markdown filename
     * @return markdown file content or empty string if file not found
     */
    String getMarkdownFileContent(String filename);
}
