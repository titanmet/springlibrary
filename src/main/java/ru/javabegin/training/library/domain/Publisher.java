package ru.javabegin.training.library.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.util.List;

// JPA
@Entity // все поля класса будут автоматически связаны со столбцами таблицы
@Table(catalog = "library")

// Lombok
@EqualsAndHashCode(of = "id")
@Getter @Setter // генерация гетеров-сетеров для всех полей класса

// Hibernate
@DynamicUpdate // обновляет только те поля, которые изменились
@DynamicInsert // вставляет только те поля, у которых есть значение
@SelectBeforeUpdate // проверить объект перед обновлением, нужно ли его обновлять
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement
    private Long id;

    private String name;

    // двухсторонняя связь с Book
    @Basic(fetch = FetchType.LAZY) // коллекция будет запрашиваться только по требованию (ленивая инициализация)
    @OneToMany(mappedBy = "publisher") // publisher должно совпадать с именем поля в классе Book
    private List<Book> books;

    @Override
    public String toString() {
        return name;
    }

}
