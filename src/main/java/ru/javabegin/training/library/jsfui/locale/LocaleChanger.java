package ru.javabegin.training.library.jsfui.locale;

import ru.javabegin.training.library.jsfui.util.CookieHelper;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.Locale;

@ManagedBean(eager = true)
@SessionScoped
public class LocaleChanger implements Serializable {

    // хранится текущая выбранная пользователем локаль
    private Locale currentLocale = new Locale("ru");

    public LocaleChanger() {

        // есть ли сохраненная локаль
        if (CookieHelper.getCookie(CookieHelper.COOKIE_LANG)==null){
            return;
        }

        String cookieLang = CookieHelper.getCookie(CookieHelper.COOKIE_LANG).getValue();
        if (cookieLang != null){
            currentLocale = new Locale(cookieLang);
        }
    }

    public void changeLocale(String localeCode) {
        currentLocale = new Locale(localeCode);

        // сохранить в куки браузера выбранный язык
        CookieHelper.setCookie(CookieHelper.COOKIE_LANG, currentLocale.getLanguage(), 3600);

    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }



}
