package ru.rkhamatyarov.myjavascramble.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.rkhamatyarov.myjavascramble.entity.ScrambleNote;
import ru.rkhamatyarov.myjavascramble.service.ScrambleService;

import java.util.List;

@Controller
public class ScrambleNoteController {
    private final static String ASC_DATE_WAY_OF_SORT = "ASC";
    private final static String DESC_DATE_WAY_OF_SORT = "DESC";

    private String dateWayOfSort = ASC_DATE_WAY_OF_SORT;

    @Autowired
    public ScrambleService scrambleService;

    @GetMapping("/")
    public String list(Model model) {
        List<ScrambleNote> scrambleNoteList =  sortScrambleNoteList();

        model.addAttribute("notes", scrambleNoteList);
        model.addAttribute("sort", dateWayOfSort);

        return "index";
    }

    @GetMapping("/sort/{dateWayOfSort}")
    public String sortByDate(@PathVariable String dateWayOfSort) {
        this.dateWayOfSort = dateWayOfSort;
        return "redirect:/";
    }

    @PostMapping("/save")
    public String updateScrambleNote(@RequestParam String message) {
        scrambleService.saveScrambleNote(new ScrambleNote(message));

        return "redirect:/";
    }

    @GetMapping("/new")
    public String newScrambleNote() {
        return "operations/new";
    }

    @GetMapping("/edit/{id}")
    public String saveScrambleNote(@PathVariable Integer id, Model model) {
        ScrambleNote scrambleNote = scrambleService.getNoteById(id);
        model.addAttribute("note", scrambleNote);
        return "operations/edit";
    }

    @PostMapping("/update")
    public String saveScrambleNote(
        @RequestParam Integer id,
        @RequestParam String message,
        @RequestParam(value = "done", required = false) boolean done
    ){
        scrambleService.updateScrambleNote(id, message, done);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteScrambleNote(@PathVariable Integer id) {
        scrambleService.deleteScrambleNote(id);
        return "redirect:/";
    }

    private List<ScrambleNote> sortScrambleNoteList() {
        List<ScrambleNote> scrambleNoteList = null;

        switch (dateWayOfSort) {
            case ASC_DATE_WAY_OF_SORT:
                scrambleNoteList = scrambleService.findAllByDateAscOrder();
                break;
            case DESC_DATE_WAY_OF_SORT:
                scrambleNoteList = scrambleService.findAllByDateDescOrder();
        }
        return scrambleNoteList;
    }
}
