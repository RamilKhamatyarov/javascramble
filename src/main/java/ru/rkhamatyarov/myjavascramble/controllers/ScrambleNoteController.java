package ru.rkhamatyarov.myjavascramble.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.rkhamatyarov.myjavascramble.entity.MarkdownFile;
import ru.rkhamatyarov.myjavascramble.entity.ScrambleNote;
import ru.rkhamatyarov.myjavascramble.service.MarkdownService;
import ru.rkhamatyarov.myjavascramble.service.ScrambleService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Controller for Scramble notes pages and CRUD operations.
 */
@Controller
public final class ScrambleNoteController {

    /**
     * Date sort order constant for ascending.
     */
    private static final String ASC_DATE_WAY_OF_SORT = "ASC";

    /**
     * Date sort order constant for descending.
     */
    private static final String DESC_DATE_WAY_OF_SORT = "DESC";

    /**
     * Current sort order (ASC or DESC).
     */
    private String dateWayOfSort = ASC_DATE_WAY_OF_SORT;

    /**
     * Service for scramble notes operations.
     */
    @Autowired
    private ScrambleService scrambleService;

    /**
     * Service for searching markdown files.
     */
    @Autowired
    private MarkdownService markdownService;

    /**
     * List all notes.
     *
     * @param model the model to add attributes
     * @return the view name
     */
    @GetMapping("/")
    public String list(final Model model) {
        final List<ScrambleNote> scrambleNoteList = sortScrambleNoteList();
        model.addAttribute("notes", scrambleNoteList);
        model.addAttribute("sort", dateWayOfSort);
        return "index";
    }

    /**
     * Change sorting order and redirect to list.
     *
     * @param dateWayOfSortParam sort order parameter
     * @return redirect to list
     */
    @GetMapping("/sort/{dateWayOfSort}")
    public String sortByDate(@PathVariable final String dateWayOfSortParam) {
        this.dateWayOfSort = dateWayOfSortParam;
        return "redirect:/";
    }

    /**
     * Create a new note.
     *
     * @param message the note message
     * @return redirect to list
     */
    @PostMapping("/save")
    public String updateScrambleNote(@RequestParam final String message) {
        scrambleService.saveScrambleNote(new ScrambleNote(message));
        return "redirect:/";
    }

    /**
     * Show new note form.
     *
     * @return the view name
     */
    @GetMapping("/new")
    public String newScrambleNote() {
        return "operations/new";
    }

    /**
     * Show edit form for a note.
     *
     * @param id    the note id
     * @param model the model to add attributes
     * @return the view name
     */
    @GetMapping("/edit/{id}")
    public String saveScrambleNote(
            @PathVariable final Long id,
            final Model model
    ) {
        final ScrambleNote scrambleNote = scrambleService.getNoteById(id);
        model.addAttribute("note", scrambleNote);
        return "operations/edit";
    }

    /**
     * Update existing note.
     *
     * @param id      the note id
     * @param message the note message
     * @param done    completion status
     * @return redirect to list
     */
    @PostMapping("/update")
    public String saveScrambleNote(
            @RequestParam final Long id,
            @RequestParam final String message,
            @RequestParam(value = "done", required = false)
            final boolean done
    ) {
        scrambleService.updateScrambleNote(id, message, done);
        return "redirect:/";
    }

    /**
     * Delete note by id.
     *
     * @param id the note id
     * @return redirect to list
     */
    @GetMapping("/delete/{id}")
    public String deleteScrambleNote(@PathVariable final Long id) {
        scrambleService.deleteScrambleNote(id);
        return "redirect:/";
    }

    /**
     * List all markdown files.
     *
     * @param model the model to add attributes
     * @return the view name
     */
    @GetMapping("/markdown")
    public String listMarkdownFiles(final Model model) {
        final List<MarkdownFile> markdownFiles =
                markdownService.getAllMarkdownFiles();
        model.addAttribute("markdownFiles", markdownFiles);
        return "markdowns/list";
    }

    /**
     * Show markdown file content by relative filepath.
     *
     * @param filepath the markdown filepath (can include subdirectories)
     * @param model    the model to add attributes
     * @return the view name
     */
    @GetMapping("/markdown/{filepath:.+}")
    public String showMarkdownFile(
            @PathVariable final String filepath,
            final Model model
    ) {
        try {
            // Decode URL-encoded filepath
            String decodedFilepath = URLDecoder.decode(
                    filepath, StandardCharsets.UTF_8
            );

            final String content =
                    markdownService.getMarkdownFileContent(decodedFilepath);

            String renderedContent;
            if (content != null && !content.trim().isEmpty()) {
                try {
                    List<Extension> extensions = Arrays.asList(
                            TablesExtension.create(),
                            AutolinkExtension.create()
                    );

                    Parser parser = Parser.builder()
                            .extensions(extensions)
                            .build();

                    HtmlRenderer renderer = HtmlRenderer.builder()
                            .extensions(extensions)
                            .build();

                    var document = parser.parse(content);
                    renderedContent = renderer.render(document);

                } catch (Exception e) {
                    renderedContent = "<div class='alert alert-danger'>"
                            + "Error rendering markdown: "
                            + e.getMessage()
                            + "</div>";
                }
            } else {
                renderedContent =
                        "<div class='alert alert-info'>No content available."
                                + "</div>";
            }

            model.addAttribute("filename", decodedFilepath);
            model.addAttribute("content", content);
            model.addAttribute("renderedContent", renderedContent);
        } catch (Exception e) {
            model.addAttribute("filename", filepath);
            model.addAttribute("error", "Error loading markdown file: "
                    + e.getMessage());
        }

        return "markdowns/view";
    }

    /**
     * List all image files.
     *
     * @param model the model to add attributes
     * @return the view name
     */
    @GetMapping("/images")
    public String listImageFiles(final Model model) {
        final List<MarkdownFile> imageFiles =
                markdownService.getAllImageFiles();
        model.addAttribute("imageFiles", imageFiles);
        return "images/list";
    }

    /**
     * Serve image file.
     *
     * @param filepath the image filepath
     * @param response the HTTP response
     */
    @GetMapping("/images/{filepath:.+}")
    public void serveImageFile(
            @PathVariable final String filepath,
            final HttpServletResponse response
    ) {
        try {
            // Decode URL-encoded filepath
            String decodedFilepath = URLDecoder.decode(
                    filepath, StandardCharsets.UTF_8
            );

            byte[] imageData =
                    markdownService.getImageFileContent(decodedFilepath);

            if (imageData.length == 0) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            String contentType = getContentType(decodedFilepath);
            response.setContentType(contentType);
            response.setHeader("Cache-Control", "max-age=3600");
            response.getOutputStream().write(imageData);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Show individual image view page.
     *
     * @param filepath the image filepath
     * @param model    the model to add attributes
     * @return the view name
     */
    @GetMapping("/image-view/{filepath:.+}")
    public String viewImageFile(
            @PathVariable final String filepath,
            final Model model
    ) {
        try {
            // Decode URL-encoded filepath
            String decodedFilepath = URLDecoder.decode(
                    filepath, StandardCharsets.UTF_8
            );

            byte[] imageData =
                    markdownService.getImageFileContent(decodedFilepath);
            if (imageData.length == 0) {
                model.addAttribute("filename", decodedFilepath);
                model.addAttribute(
                        "error",
                        "Image file not found or inaccessible: "
                                + decodedFilepath
                );
                return "images/view";
            }

            MarkdownFile fileInfo = markdownService.getAllImageFiles()
                    .stream()
                    .filter(file -> file.relativePath()
                            .equals(decodedFilepath))
                    .findFirst()
                    .orElse(null);

            model.addAttribute("filename", decodedFilepath);
            model.addAttribute("fileInfo", fileInfo);

        } catch (Exception e) {
            model.addAttribute("filename", filepath);
            model.addAttribute("error", "Error loading image: "
                    + e.getMessage());
        }

        return "images/view";
    }

    /**
     * Determine content type based on file extension.
     *
     * @param filename the filename to check
     * @return the MIME content type
     */
    private String getContentType(final String filename) {
        String lowerFilename = filename.toLowerCase();
        if (lowerFilename.endsWith(".png")) {
            return "image/png";
        } else if (lowerFilename.endsWith(".jpg")
                || lowerFilename.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lowerFilename.endsWith(".gif")) {
            return "image/gif";
        } else if (lowerFilename.endsWith(".bmp")) {
            return "image/bmp";
        } else if (lowerFilename.endsWith(".webp")) {
            return "image/webp";
        } else {
            return "application/octet-stream";
        }
    }

    /**
     * Return list sorted according to current sort order.
     *
     * @return sorted list of notes
     */
    private List<ScrambleNote> sortScrambleNoteList() {

        return switch (dateWayOfSort) {
            case ASC_DATE_WAY_OF_SORT ->
                    scrambleService.findAllByDateAscOrder();
            case DESC_DATE_WAY_OF_SORT ->
                    scrambleService.findAllByDateDescOrder();
            default -> scrambleService.findAllByDateAscOrder();
        };
    }
}
