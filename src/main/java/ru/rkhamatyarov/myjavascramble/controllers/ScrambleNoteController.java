package ru.rkhamatyarov.myjavascramble.controllers;

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
     * Service for searching markdown files
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
            @RequestParam(value = "done", required = false) final boolean done
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
        final List<MarkdownFile> markdownFiles = markdownService.getAllMarkdownFiles();
        model.addAttribute("markdownFiles", markdownFiles);
        return "markdowns/list";
    }

    /**
     * Show markdown file content.
     *
     * @param filename the markdown filename
     * @param model    the model to add attributes
     * @return the view name
     */
    @GetMapping("/markdown/{filename}")
    public String showMarkdownFile(
            @PathVariable final String filename,
            final Model model
    ) {
        final String content = markdownService.getMarkdownFileContent(filename);
        model.addAttribute("filename", filename);
        model.addAttribute("content", content);
        return "markdowns/view";
    }

    /**
     * Return list sorted according to current sort order.
     *
     * @return sorted list of notes
     */
    private List<ScrambleNote> sortScrambleNoteList() {

        return switch (dateWayOfSort) {
            case ASC_DATE_WAY_OF_SORT -> scrambleService.findAllByDateAscOrder();
            case DESC_DATE_WAY_OF_SORT -> scrambleService.findAllByDateDescOrder();
            default -> scrambleService.findAllByDateAscOrder();
        };
    }
}
