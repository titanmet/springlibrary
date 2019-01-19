package ru.javabegin.training.library.jsfui.model;


import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import ru.javabegin.training.library.jsfui.controller.AbstractController;

import java.util.List;
import java.util.Map;


// модель для постраничного вывода списка книг при любом поиске
// можно применять не только к книгам, но и к любым типам данных, т.к. используется Generics
@Getter
@Setter
public class LazyDataTable<T> extends LazyDataModel<T> {

    private List<T> list;

    private AbstractController<T> abstractController;

    public LazyDataTable(AbstractController<T> abstractController) {
        this.abstractController = abstractController;
    }


    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

        int pageNumber = first / pageSize;//

        Sort.Direction sortDirection = Sort.Direction.ASC;// по-умолчанию - сортировка по возрастанию

        if (sortOrder!=null) {
            // все текущие настройки DataTable (сортировка, поле сортировки) будут передаваться в SQL запрос
            switch (sortOrder) {
                case DESCENDING:
                    sortDirection = Sort.Direction.DESC;
                    break;
            }
        }

        Page<T> searchResult = abstractController.search(pageNumber, pageSize, sortField, sortDirection);

        this.setRowCount((int) searchResult.getTotalElements());

        return searchResult.getContent();
    }




}
