package ru.javabegin.training.library.spring.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.javabegin.training.library.domain.Genre;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    List<Genre> findByNameContainingIgnoreCaseOrderByName(String name);

    Page<Genre> findByNameContainingIgnoreCaseOrderByName(String name, Pageable pageable);


}
