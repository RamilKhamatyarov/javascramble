package ru.rkhamatyarov.myjavascramble.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rkhamatyarov.myjavascramble.entity.ScrambleNote;

import java.util.List;

/**
 * Repository for ScrambleNote entities.
 */
@Repository
public interface ScrambleRepository extends JpaRepository<ScrambleNote, Long> {

    /**
     * Return all notes ordered by date ascending.
     *
     * @return ordered list of notes
     */
    List<ScrambleNote> findAllByOrderByDateAsc();

    /**
     * Return all notes ordered by date descending.
     *
     * @return ordered list of notes
     */
    List<ScrambleNote> findAllByOrderByDateDesc();
}
