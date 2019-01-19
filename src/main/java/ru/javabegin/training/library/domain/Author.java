package ru.javabegin.training.library.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

// JPA
@Entity // все поля класса будут автоматически связаны со столбцами таблицы
@Table (catalog = "library")

// Lombok
@EqualsAndHashCode(of = "id")
@Getter @Setter // генерация гетеров-сетеров для всех полей класса

// аннотации Hibernate
@DynamicUpdate // обновляет только те поля, которые изменились
@DynamicInsert // вставляет только те поля, у которых есть значение
@SelectBeforeUpdate  // проверить объект перед обновлением, нужно ли его обновлять
public class Author {

    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement
    @Id
    private Long id;

    private String fio;

    private Date birthday;

    // двухсторонняя связь с Book
    @Basic(fetch = FetchType.LAZY) // коллекция будет запрашиваться только по требованию (ленивая инициализация)
    @OneToMany(mappedBy = "author") // author должно совпадать с именем поля в классе Book
    private List<Book> books;

    @Override
    public String toString() {
        return fio;
    }

}
