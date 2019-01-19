package ru.javabegin.training.library.jsfui.controller;


import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.javabegin.training.library.dao.GenreDao;
import ru.javabegin.training.library.domain.Author;
import ru.javabegin.training.library.domain.Genre;
import ru.javabegin.training.library.jsfui.model.LazyDataTable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;

@ManagedBean
@SessionScoped
@Component
@Getter
@Setter
public class GenreController extends AbstractController<Genre> {


    // из JSF таблицы обязательно должна быть ссылки на переменные, иначе при использовании постраничности dataTable работает некорректно
    // также - выбранное пользователем значение (кол-во записей на странице) будет сохраняться
    private int rowsCount = 20;
    private int first;

    @Autowired
    private GenreDao genreDao;

    @Autowired
    private SprController sprController;

    private Genre selectedGenre;  // над каким жанром в данный момент производим действие (удаление, редактирование)

    private LazyDataTable<Author> lazyModel; // справочные значения также выводятся постранично, как и книги (создается новый экземпляр lazyModel)

    private Page<Genre> genrePages; // найденные жанры


    @PostConstruct
    public void init() {
        lazyModel = new LazyDataTable(this);

    }


    public List<Genre> find(String name) {
        return genreDao.search(name);
    }







    public void save() {
        genreDao.save(selectedGenre);
        RequestContext.getCurrentInstance().execute("PF('dialogGenre').hide()");
    }


    // автоматически вызывается из LazyDataTable
    @Override
    public Page<Genre> search(int pageNumber, int pageSize, String sortField, Sort.Direction sortDirection) {

        if (sortField == null) {
            sortField = "name";
        }

        // для удобной проверки строк - используем библиотеку Google Guava и метод isNullOrEmpty
        if (Strings.isNullOrEmpty(sprController.getSearchText())) {
            genrePages = genreDao.getAll(pageNumber, pageSize, sortField, sortDirection);
        } else {
            genrePages = genreDao.search(pageNumber, pageSize, sortField, sortDirection, sprController.getSearchText());
        }

        return genrePages;

    }

    @Override
    public void addAction() {
        selectedGenre = new Genre();
        showEditDialog();
    }

    @Override
    public void editAction() {

        // выбранный genre уже будет записан в переменную selectedGenre (как только пользователь кликнет на редактирование)
        // он отобразится в диалоговом окне
        showEditDialog();

    }


    @Override
    public void deleteAction() {
        // выбранный genre уже будет записан в переменную selectedGenre (как только пользователь кликнет на удаление)
        genreDao.delete(selectedGenre);
    }

    private void showEditDialog() {

        // показывает диалоговое окно со значениями selectedGenre
        RequestContext.getCurrentInstance().execute("PF('dialogGenre').show()");
    }

    // вызывается для отображения всех жанров слева на странице
    public List<Genre> getAll() {
        return genreDao.getAll(new Sort(Sort.Direction.ASC, "name"));
    }


}
