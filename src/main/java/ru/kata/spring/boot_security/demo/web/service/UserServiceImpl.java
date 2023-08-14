package ru.kata.spring.boot_security.demo.web.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.web.exeptions.UserNameException;
import ru.kata.spring.boot_security.demo.web.model.User;
import ru.kata.spring.boot_security.demo.web.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    private UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void add(User user) throws UserNameException {
        User user1 = userRepository.findByNameLike(user.getUsername());
        if(user1 == null) {
            userRepository.save(user);
        } else {
            throw new UserNameException("Пользователь с таким именем уже существует, укажите другое имя");
        }
    }

    @Override
    public void update(User user) throws UserNameException {
        User user1 = userRepository.findByNameLike(user.getUsername());
        if(user.getId() != null && user1 == null){
            userRepository.save(user);
        } else if(user1.getId().equals(user.getId()) && user1.getUsername().equals(user.getUsername())) {
            userRepository.save(user);
        } else {
            throw new UserNameException("Пользователь с таким именем уже существует, укажите другое имя");
        }
    }

    @Override
    public List<User> listUsers() {
        try {
            Iterable<User> users = userRepository.findAll();
            List<User> result = new ArrayList<>();
             users.forEach(result::add);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }
    @Override
    public User getUser(long id) {
        Optional<User> user = Optional.empty();
        try {
            user = userRepository.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user.orElse(new User()) ;
    }
    @Override
    public void removeUser(long id) {
        try {
           userRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUserOnNme(String username) {
        User user = null;
        try {
           user = userRepository.findByNameLike(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByNameLike(username);
        if(user == null) {
            throw new UsernameNotFoundException(String.format("User %s not found",username));
        }
        return user;
    }

}
