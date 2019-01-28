package ru.rkhamatyarov.myjavascramble.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rkhamatyarov.myjavascramble.entity.ScrambleNote;
import ru.rkhamatyarov.myjavascramble.repository.ScrambleRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ScrambleServiceImpl implements ScrambleService {

    @Autowired
    public ScrambleRepository repository;

    @Override
    public ScrambleNote getNoteById(Integer id) {

        Optional<ScrambleNote> optionalScrambleNote = repository.findById(id);
        return optionalScrambleNote.orElse(null);
    }

    @Override
    public void saveScrambleNote(ScrambleNote scrambleNote) {
        repository.save(scrambleNote);
    }

    @Override
    public void updateScrambleNote(Integer id, String message, boolean done) {
        Optional<ScrambleNote> optionalUpdated = repository.findById(id);

        ScrambleNote scrambleNote = optionalUpdated.orElse(null);

        Objects.requireNonNull(scrambleNote).setMessage(message);
        scrambleNote.setDone(done);

        repository.save(scrambleNote);
    }

    @Override
    public void deleteScrambleNote(Integer id) {
        Optional<ScrambleNote> optionalScrambleNote = repository.findById(id);
        repository.delete(optionalScrambleNote.orElse(null));
    }

    @Override
    public List<ScrambleNote> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ScrambleNote> findAllByDateAscOrder() {
        return repository.findAllByOrderByDateAsc();
    }

    @Override
    public List<ScrambleNote> findAllByDateDescOrder() {
        return repository.findAllByOrderByDateDesc();
    }
}
