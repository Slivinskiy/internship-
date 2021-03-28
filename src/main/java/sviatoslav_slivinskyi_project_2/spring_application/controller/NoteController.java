package sviatoslav_slivinskyi_project_2.spring_application.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sviatoslav_slivinskyi_project_2.spring_application.model.Note;
import sviatoslav_slivinskyi_project_2.spring_application.model.User;
import sviatoslav_slivinskyi_project_2.spring_application.service.NoteService;
import sviatoslav_slivinskyi_project_2.spring_application.service.UserService;

import java.util.List;


@Controller
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    NoteService noteService;
    @Autowired
    UserService userService;

    private static final int MIN_SQL_RESULT = 0;

    private static final Logger LOGGER = LogManager.getLogger(NoteController.class);

    @GetMapping
    public String viewNotes( Model model, Authentication authentication){
        String username = authentication.getName();
        List<Note> notes = noteService.getAllNotes(username);
        model.addAttribute("notes", notes);
        return "notes";
    }

    @GetMapping("/newNote")
    public String newNoteForm(@ModelAttribute("note") Note note){
        return "note-form";
    }

    @GetMapping("/updateNote/{noteId}")
    public String updateNoteForm(@ModelAttribute("note") Note note, Model model){
        note = noteService.getNote(note.getNoteId());
        model.addAttribute("noteid", note.getNoteId());
        model.addAttribute("note", note);
        return "note-form";
    }

    @GetMapping("/deleteNote/{noteId}")
    public String deleteNote( Note note, Model model){
        noteService.deleteNote(note.getNoteId());
        if (!noteService.getOptionalNote(note.getNoteId()).isPresent()){
            model.addAttribute("SuccessDelete", true);
            LOGGER.info("Note successfully deleted");
        } else {
            model.addAttribute("ErrorDelete", true);
            LOGGER.error("Note wasn`t deleted");
        }
        return "result";
    }

    @PostMapping("/newNote")
    public String addNote(@ModelAttribute("note") Note note, Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUserByName(username);
        Note note1 = new Note();
        note1.setUser(user);
        note1.setNoteTitle(note.getNoteTitle());
        note1.setNoteDescription(note.getNoteDescription());

        if (noteService.saveNote(note1) != null) {
            model.addAttribute("SuccessSaveNote", true);
            LOGGER.info("Note was saved");
        } else {
            model.addAttribute("ErrorSaveNote", true);
            LOGGER.error("An error occurs during saving the note");
        }
        return "result";
    }

    @PostMapping("/updateNote/{noteId}")
    public String updateNote(@ModelAttribute("note") Note note, Model model){
        int result = noteService.updateNote(note);
        if (result < MIN_SQL_RESULT){
           model.addAttribute("ErrorUpdateNote", true);
           LOGGER.error("An error occurs during updating the note");
        } else {
            model.addAttribute("SuccessUpdateNote", true);
            LOGGER.info("Note was updated");
        }
        return "result";
    }
}
