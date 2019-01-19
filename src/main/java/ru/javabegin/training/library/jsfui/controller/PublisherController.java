package ru.javabegin.training.library.jsfui.controller;


import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.javabegin.training.library.dao.PublisherDao;
import ru.javabegin.training.library.domain.Author;
import ru.javabegin.training.library.domain.Publisher;
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
public class PublisherController extends AbstractController<Publisher> {


    // из JSF таблицы обязательно должна быть ссылки на переменные, иначе при использовании постраничности dataTable работает некорректно
    // также - выбранное пользователем значение (кол-во записей на странице) будет сохраняться
    private int rowsCount = 20;
    private int first;
    private Page<Publisher> publisherPages; // найденные издатели

    @Autowired
    private PublisherDao publisherDao;

    @Autowired
    private SprController sprController;


    private Publisher selectedPublisher; // над каким издателем в данный момент производим действие (удаление, редактирование)


    private LazyDataTable<Author> lazyModel; // справочные значения также выводятся постранично, как и книги (создается новый экземпляр lazyModel)

    @PostConstruct
    public void init() {
        lazyModel = new LazyDataTable(this);

    }



    public void save() {
        publisherDao.save(selectedPublisher);
        RequestContext.getCurrentInstance().execute("PF('dialogPublisher').hide()");
    }


    // автоматически вызывается из LazyDataTable
    @Override
    public Page<Publisher> search(int pageNumber, int pageSize, String sortField, Sort.Direction sortDirection) {


        if (sortField == null) {
            sortField = "name";
        }

        // для удобной проверки строк - используем библиотеку Google Guava и метод isNullOrEmpty
        if (Strings.isNullOrEmpty(sprController.getSearchText())) {
            publisherPages = publisherDao.getAll(pageNumber, pageSize, sortField, sortDirection);
        } else {
            publisherPages = publisherDao.search(pageNumber, pageSize, sortField, sortDirection, sprController.getSearchText());
        }


        return publisherPages;

    }

    @Override
    public void addAction() {
        selectedPublisher = new Publisher();

        showEditDialog();

    }


    @Override
    public void editAction() {

        // выбранный publisher уже будет записан в переменную selectedPublisher (как только пользователь кликнет на редактирование)
        // он отобразится в диалоговом окне
        showEditDialog();

    }

    @Override
    public void deleteAction() {

        // выбранный publisher уже будет записан в переменную selectedPublisher (как только пользователь кликнет на удаление)
        publisherDao.delete(selectedPublisher);
    }

    private void showEditDialog() {

        // показывает диалоговое окно со значениями selectedPublisher
        RequestContext.getCurrentInstance().execute("PF('dialogPublisher').show()");
    }

    public List<Publisher> find(String name) {
        return publisherDao.search(name);
    }
}
