package ru.rkhamatyarov.myjavascramble.service.impl;

import org.springframework.stereotype.Service;
import ru.rkhamatyarov.myjavascramble.entity.ScrambleNote;
import ru.rkhamatyarov.myjavascramble.repository.ScrambleRepository;
import ru.rkhamatyarov.myjavascramble.service.ScrambleService;

import java.util.List;

/**
 * Service implementation for managing ScrambleNote entities.
 */
@Service
public final class ScrambleServiceImpl implements ScrambleService {

    /**
     * Repository for ScrambleNote persistence operations.
     */
    private final ScrambleRepository repository;

    /**
     * Constructs the service with the given repository.
     *
     * @param repositoryParam repository
     */
    public ScrambleServiceImpl(final ScrambleRepository repositoryParam) {
        this.repository = repositoryParam;
    }

    /**
     * Retrieves a ScrambleNote by id.
     *
     * @param id identifier of the note
     * @return the found ScrambleNote or null if not found
     */
    @Override
    public ScrambleNote getNoteById(final Long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Persists a ScrambleNote.
     *
     * @param scrambleNote note to save
     */
    @Override
    public void saveScrambleNote(final ScrambleNote scrambleNote) {
        repository.save(scrambleNote);
    }

    /**
     * Updates a ScrambleNote fields.
     *
     * @param id      id of the note to update
     * @param message new message
     * @param done    new done flag
     */
    @Override
    public void updateScrambleNote(
            final Long id,
            final String message,
            final boolean done
    ) {
        ScrambleNote note = repository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Note not found"
                        )
                );
        note.setMessage(message);
        note.setDone(done);
        repository.save(note);
    }

    /**
     * Deletes a ScrambleNote by id.
     *
     * @param id id of note to delete
     */
    @Override
    public void deleteScrambleNote(final Long id) {
        repository.deleteById(id);
    }

    /**
     * Finds all ScrambleNotes.
     *
     * @return list of notes
     */
    @Override
    public List<ScrambleNote> findAll() {
        return repository.findAll();
    }

    /**
     * Finds all ScrambleNotes sorted by date ascending.
     *
     * @return list of notes sorted ascending by date
     */
    @Override
    public List<ScrambleNote> findAllByDateAscOrder() {
        return repository.findAllByOrderByDateAsc();
    }

    /**
     * Finds all ScrambleNotes sorted by date descending.
     *
     * @return list of notes sorted descending by date
     */
    @Override
    public List<ScrambleNote> findAllByDateDescOrder() {
        return repository.findAllByOrderByDateDesc();
    }
}
