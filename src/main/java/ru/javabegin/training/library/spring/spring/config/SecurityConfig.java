package ru.javabegin.training.library.spring.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import ru.javabegin.training.library.spring.auth.AuthHandler;
import ru.javabegin.training.library.spring.spring.auth.AuthHandler;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private AuthHandler authHandler; // дополнительный обработчик при неудачном входе в систему

    @Autowired
    private DataSource dataSource;// этот бин создает автоматичеки spring boot, поэтому мы просто его используем

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(
                        "select username,password,enabled from library.user where username = ?")
                .authoritiesByUsernameQuery(
                        "select username, role from library.user_roles where username = ?").passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // настройка ограничений доступа к страницам
        http.authorizeRequests()
                .antMatchers("/**").permitAll()
                .antMatchers("/pages/spr.xhtml").hasRole("ADMIN")// здесь автоматически будет добавлен префикс ROLE_,  поэтому указываем название роли без него
                .antMatchers("/pages/books.xhtml").hasAnyRole("ADMIN", "USER")// здесь автоматически будет добавлен префикс ROLE_,  поэтому указываем название роли без него
                .anyRequest().authenticated()

                .and()

                .exceptionHandling().accessDeniedPage("/index.xhtml")// при ошибке доступа - будет перенправляться на страницу с книгами
                .and()

                .csrf().disable()

                // окно аутентификации
                .formLogin()
                .loginPage("/index.xhtml")
//                .failureHandler(authHandler)
                .defaultSuccessUrl("/pages/books.xhtml")
                .loginProcessingUrl("/login")
                .passwordParameter("password")
                .usernameParameter("username")
                .and()

                // настройка выхода пользователя из системы
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/index.xhtml")
                .deleteCookies("JSESSIONID", "SPRING_SECURITY_REMEMBER_ME_COOKIE")
                .invalidateHttpSession(true);




    }
}