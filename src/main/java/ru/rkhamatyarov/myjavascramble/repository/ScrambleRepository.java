package ru.rkhamatyarov.myjavascramble.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rkhamatyarov.myjavascramble.entity.ScrambleNote;

import java.util.List;

public interface ScrambleRepository extends JpaRepository<ScrambleNote, Integer> {
    List<ScrambleNote> findAllByOrderByDateAsc();

    List<ScrambleNote> findAllByOrderByDateDesc();
}
