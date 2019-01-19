package ru.javabegin.training.library.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;

// JPA
@Entity // все поля класса будут автоматически связаны со столбцами таблицы
@Table(catalog = "library")

// Lombok
@EqualsAndHashCode(of = "id")
@Getter @Setter // генерация гетеров-сетеров для всех полей класса

// аннотации Hibernate
@DynamicUpdate // обновляет только те поля, которые изменились
@DynamicInsert // вставляет только те поля, у которых есть значение
@SelectBeforeUpdate // проверить объект перед обновлением, нужно ли его обновлять
public class Book {

    public Book() {
    }

    // здесь нет заполнения поля content - чтобы не грузить страницу (контент получаем по требованию)
    public Book(Long id, String name, Integer pageCount, String isbn, Genre genre, Author author, Publisher publisher, Integer publishYear, byte[] image, String descr, long viewCount, long totalRating, long totalVoteCount, int avgRating) {
        this.id = id;
        this.name = name;
        this.pageCount = pageCount;
        this.isbn = isbn;
        this.genre = genre;
        this.author = author;
        this.publisher = publisher;
        this.publishYear = publishYear;
        this.image = image;
        this.descr = descr;
        this.viewCount = viewCount;
        this.totalRating = totalRating;
        this.totalVoteCount=totalVoteCount;
        this.avgRating = avgRating;
    }

    public Book(Long id, byte[] image) {
        this.id = id;
        this.image = image;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement
    private Long id;

    private String name;

    @Lob
    @Column(updatable = false)// updatable = false: при апдейте это поле не будет добавляться (content будем обновлять отдельным запросом)
    private byte[] content;

    @Column(name = "page_count")
    private Integer pageCount;

    private String isbn;



    @ManyToOne // ссылка foreign key идет из таблицы Book в таблицу Genre
    // по-умолчанию Hibernate пытается связать по полю genre_id (как в нашей таблице), если имя столбца другое, нужно задавать атрибут name у @JoinColumn
    @JoinColumn // для получения готового объекта Genre по id
    private Genre genre;

    @ManyToOne
    @JoinColumn
    private Author author;

    @ManyToOne
    @JoinColumn
    private Publisher publisher;


    @Column(name = "publish_year")// если имя столбца не совпадает с именем переменной
    private Integer publishYear;


    private byte[] image;

    private String descr;

    @Column(name = "view_count")
    private long viewCount;

    @Column(name = "total_rating")
    private long totalRating;

    @Column(name = "total_vote_count")
    private long totalVoteCount;

    @Column(name = "avg_rating")
    private int avgRating;

    @Override
    public String toString() {
        return name;
    }

}
