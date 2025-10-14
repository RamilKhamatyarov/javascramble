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
     * Get all image files from configured directory recursively up to 5 levels.
     *
     * @return list of image files
     */
    List<MarkdownFile> getAllImageFiles();

    /**
     * Get markdown file content by filename.
     *
     * @param filename the markdown filename
     * @return markdown file content or empty string if file not found
     */
    String getMarkdownFileContent(String filename);

    /**
     * Get image file as byte array by filepath (relative to markdown
     * directory).
     *
     * @param filepath the image filepath (can include subdirectories)
     * @return image file bytes or empty array if file not found
     */
    byte[] getImageFileContent(String filepath);

    /**
     * Get all files (markdown and images) from configured directory
     * recursively.
     *
     * @return list of all supported files
     */
    List<MarkdownFile> getAllFiles();
}
