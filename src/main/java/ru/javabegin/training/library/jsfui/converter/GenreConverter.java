package ru.javabegin.training.library.jsfui.converter;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.javabegin.training.library.dao.GenreDao;
import ru.javabegin.training.library.domain.Genre;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

// для преобразования выбранного значения из выпадающего списка в конкретный объект Genre
@FacesConverter(forClass = Genre.class)
@Component
public class GenreConverter implements Converter {

    @Autowired
    private GenreDao genreDao;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        if (Strings.isNullOrEmpty(value)) {

            return null;
        }

        return genreDao.get(Integer.valueOf(value));
    }


    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        if (value == null) {
            return null;
        }

        return ((Genre) value).getId().toString();
    }
}
