package ru.javabegin.training.library.jsfui.util;

import lombok.extern.java.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.primefaces.context.RequestContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.UnexpectedRollbackException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.ResourceBundle;
import java.util.logging.Level;

@Log
@Aspect
@Component
public class AspectMessageHandler {


    // не нужно перехватывать все ошибки в коде, AOP все сделает за нас
    @Around("execution(* ru.javabegin.training.library.jsfui.controller.*.deleteAction(..))")
// перехватывать все ошибки во всех методах подпакетов и классов
    public void deleteConstraint(ProceedingJoinPoint jp) {

        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg");


        try {

            jp.proceed(); // выполнить метод

            context.addMessage(null, new FacesMessage(null, bundle.getString("deleted")));
            RequestContext.getCurrentInstance().update("info");


        } catch (Throwable throwable) {

            if (throwable instanceof UnexpectedRollbackException) {// если транзакция откатилась

                log.log(Level.WARNING, throwable.getMessage());
                throwable.printStackTrace();


                // если это ошибка constraint при удалении
                    if (((UnexpectedRollbackException) throwable).getMostSpecificCause() instanceof com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException) {

                        // показываем сообщение пользователю на странице
                        context.addMessage(null, new FacesMessage(null, bundle.getString("constraint_delete_record")));

                    }
                }
        }

        // чтобы компонент показал сообщение - его нужно обновить
        RequestContext.getCurrentInstance().update("info");


    }




    @Around("execution(* ru.javabegin.training.library.dao.*.save(..))")
    public void addNewSprValue(ProceedingJoinPoint jp) {

        try {
            Object obj = jp.proceed();

            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg");

            // показываем сообщение пользователю на странице
            context.addMessage(null, new FacesMessage(null, bundle.getString("added")+": \""+obj.toString()+"\""));

            RequestContext.getCurrentInstance().update("info");


        } catch (Throwable throwable) {
            log.log(Level.WARNING, throwable.getMessage());
            throwable.printStackTrace();

        }

    }




}
