package ru.javabegin.training.library.spring.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.javabegin.training.library.domain.Book;

import java.util.List;

// примечания:
// при получении списка книг - контент для каждой книги - пустой. Только когда пользователь нажимает на чтение книги - делаем запрос в БД на получение контента (по требованию)
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // поиск книг по названию или автору
    // если имя метода получается слишком длинным - можно использовать @Query с HQL
    Page<Book> findByNameContainingIgnoreCaseOrAuthorFioContainingIgnoreCaseOrderByName(String name, String fio, Pageable pageable);

    @Query("select new ru.javabegin.training.library.domain.Book(b.id, b.name, b.pageCount, b.isbn, b.genre, b.author, b.publisher, b.publishYear, b.image, b.descr, b.viewCount, b.totalRating, b.totalVoteCount, b.avgRating) from Book b")
    Page<Book> findAllWithoutContent(Pageable pageable); // возвращает список книг (с постраничностью)

    // если запрос изменяет данные - нужен @Modifying
    @Modifying(clearAutomatically = true)
    @Query("update Book b set b.content=:content where b.id =:id")
    void updateContent(@Param("content") byte[] content, @Param("id") long id);

    // Для топовых книг показываем только изображение (поэтому остальные поля не выбираем)
    // В классе Book должен быть соответствующий конструктор
    @Query("select new ru.javabegin.training.library.domain.Book(b.id, b.image) from Book b")
    List<Book> findTopBooks(Pageable pageable);

    // поиск книг по жанру
    @Query("select new ru.javabegin.training.library.domain.Book(b.id, b.name, b.pageCount, b.isbn, b.genre, b.author, b.publisher, b.publishYear, b.image, b.descr, b.viewCount, b.totalRating, b.totalVoteCount, b.avgRating) from Book b where b.genre.id=:genreId")
    Page<Book> findByGenre(@Param("genreId") long genreId, Pageable pageable);

    // получение контента по id книги
    @Query("select b.content FROM Book b where b.id = :id")
    byte[] getContent(@Param("id") long id);

    // обновить статистику просмотров для книги
    @Modifying
    @Query("update Book b set b.viewCount=:viewCount where b.id =:id")
    void updateViewCount(@Param("viewCount") long viewCount, @Param("id") long id);

    // обновить данные рейтинга
    @Modifying
    @Query("update Book b set b.totalVoteCount=:totalVoteCount, b.totalRating=:totalRating, b.avgRating=:avgRating where b.id =:id")
    void updateRating(@Param("totalRating") long totalRating, @Param("totalVoteCount") long totalVoteCount,  @Param("avgRating") int avgRating, @Param("id") long id);



}
