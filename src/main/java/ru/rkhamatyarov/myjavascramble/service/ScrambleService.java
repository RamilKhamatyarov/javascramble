package ru.rkhamatyarov.myjavascramble.service;

import ru.rkhamatyarov.myjavascramble.entity.ScrambleNote;

import java.util.List;

/**
 * Service API for managing {@link ScrambleNote} entities.
 */
public interface ScrambleService {

    /**
     * Returns a ScrambleNote by its id.
     *
     * @param id note id
     * @return the found {@link ScrambleNote} or null if not found
     */
    ScrambleNote getNoteById(Long id);

    /**
     * Persists a new ScrambleNote.
     *
     * @param scrambleNote note to save
     */
    void saveScrambleNote(ScrambleNote scrambleNote);

    /**
     * Updates an existing ScrambleNote.
     *
     * @param id      id of the note to update
     * @param message new message
     * @param done    completion flag
     */
    void updateScrambleNote(Long id, String message, boolean done);

    /**
     * Deletes a ScrambleNote by id.
     *
     * @param id id of the note to delete
     */
    void deleteScrambleNote(Long id);

    /**
     * Finds all ScrambleNotes.
     *
     * @return list of all notes
     */
    List<ScrambleNote> findAll();

    /**
     * Finds all ScrambleNotes ordered by date ascending.
     *
     * @return list ordered by date ascending
     */
    List<ScrambleNote> findAllByDateAscOrder();

    /**
     * Finds all ScrambleNotes ordered by date descending.
     *
     * @return list ordered by date descending
     */
    List<ScrambleNote> findAllByDateDescOrder();
}
