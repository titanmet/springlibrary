package ru.javabegin.training.library.jsfui.controller;


import lombok.Getter;
import lombok.Setter;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.ManagedBean;
import javax.faces.bean.RequestScoped;


// общий контроллер для всех справочников
@ManagedBean
@Component
@RequestScoped
@Getter
@Setter
public class SprController {

    @Autowired
    private AuthorController authorController;

    @Autowired
    private GenreController genreController;

    @Autowired
    private PublisherController publisherController;

    private String listId = "tabView:authorForm:authorList";// по-умолчанию - открытым табом является таб для редактирования авторов

    private String searchText; // текст поиска

    private String selectedTab = "tabAuthors";// выбранный пользователем таб, по-умолчанию выбран первый таб с авторами

    // вызывается при поиске справочных значений
    public void search(){
        RequestContext context = RequestContext.getCurrentInstance();
        context.update(listId);
    }


    // возвращает нужный id для обновления области на странице
    public void onTabChange(TabChangeEvent event) {

        searchText = null;

        selectedTab = event.getTab().getId();

        switch (selectedTab){
            case "tabAuthors":
                listId = "tabView:authorForm:authorList";
                break;

            case "tabGenres":
                listId = "tabView:genreForm:genreList";
                break;

            case "tabPublishers":
                listId = "tabView:publisherForm:publisherList";
                break;
        }

        RequestContext.getCurrentInstance().update("searchForm:searchInput");


    }


    // в зависимости от выбранного таба - будем показываться соответствующее диалоговое окно
    public void addAction(){
        switch (selectedTab){
            case "tabAuthors":
                authorController.addAction();
                RequestContext.getCurrentInstance().update("tabView:dialogAuthor");
                break;

            case "tabGenres":
                genreController.addAction();
                RequestContext.getCurrentInstance().update("tabView:dialogGenre");
                break;

            case "tabPublishers":
                publisherController.addAction();
                RequestContext.getCurrentInstance().update("tabView:dialogPublisher");
                break;
        }





    }

}
