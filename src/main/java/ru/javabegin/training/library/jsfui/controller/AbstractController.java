package ru.javabegin.training.library.jsfui.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

// описывает необходимое поведение для всех JSF контроллеров
public abstract class AbstractController<T> implements Serializable {

    // постранично выводит книги
    public abstract Page<T> search(int first, int count, String sortField, Sort.Direction sortDirection);

    // для основных действий (CRUD)
    public abstract void addAction();
    public abstract void editAction();
    public abstract void deleteAction();

}
