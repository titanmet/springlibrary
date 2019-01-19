package ru.javabegin.training.library.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

// общее поведения для всех DAO объектов
public interface GeneralDAO<T>{

    // получение всех записей (без постраничности)
    List<T> getAll();

    // поиск записей с любым количествомм параметров
    List<T> search(String... searchString);

    // получение объекта по id
    T get(long id);

    // save - обновляет или добавляет объект (один метод на 2 действия)
    T save(T obj);

    // удаление объекта
    void delete(T object);

    // получение всех записей с сортировкой результата
    List<T> getAll(Sort sort);

    // получение всех записей с постраничностью
    Page<T> getAll(int pageNumber, int pageSize, String sortField, Sort.Direction sortDirection);

    // поиск записей с постраничностью
    Page<T> search(int pageNumber, int pageSize, String sortField, Sort.Direction sortDirection, String... searchString);


}
