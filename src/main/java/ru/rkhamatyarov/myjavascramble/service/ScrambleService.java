package ru.rkhamatyarov.myjavascramble.service;

import ru.rkhamatyarov.myjavascramble.entity.ScrambleNote;

import java.util.List;

public interface ScrambleService {
    ScrambleNote getNoteById(Integer id);

    void saveScrambleNote(ScrambleNote scrambleNote);

    void updateScrambleNote(Integer id, String message, boolean done);

    void deleteScrambleNote(Integer id);

    List<ScrambleNote> findAll();

    List<ScrambleNote> findAllByDateAscOrder();

    List<ScrambleNote> findAllByDateDescOrder();
}
