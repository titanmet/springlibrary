package ru.javabegin.training.library.spring.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.javabegin.training.library.domain.Author;

import java.util.List;

@Repository // создается специальный Spring bean, выполняет запросы в БД
public interface AuthorRepository extends JpaRepository<Author, Long> {// JpaRepository - содержит CRUD функционал + постраничность

    // на основании имени метода будет построен Hibernate запрос
    List<Author> findByFioContainingIgnoreCaseOrderByFio(String fio);

    // Page cодержит результаты выполнения запроса и служебные данные для постраничности
    Page<Author> findByFioContainingIgnoreCaseOrderByFio(String fio, Pageable pageable);// Pageable - параметры для постраничности

}
