package ru.kata.spring.boot_security.demo.web.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.web.model.User;

import java.util.List;

public interface UserService extends UserDetailsService  {
    void add(User user);

    List<User> listUsers();

    User getUser(long id);

    void removeUser(long id);
    User getUserOnNme(String username);
}
