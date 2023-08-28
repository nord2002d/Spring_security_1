package ru.kata.spring.boot_security.demo.web.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.web.dao.UserDaoImpl;
import ru.kata.spring.boot_security.demo.web.exeptions.UserNameException;
import ru.kata.spring.boot_security.demo.web.exeptions.UserNotFoundException;
import ru.kata.spring.boot_security.demo.web.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    private UserDaoImpl userDaoImp;

    public UserServiceImpl(UserDaoImpl userDaoImp) {
        this.userDaoImp = userDaoImp;
    }

    @Transactional
    @Override
    public void add(User user) throws UserNameException {
        if(!checkUserName(user)) {
            throw new UserNameException(
                    String.format("Пользователь с именем %s уже существует, укажите другое имя",user.getUsername()));
        }
        try {
            userDaoImp.add(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Transactional
    @Override
    public void update(User updateUser) throws UserNameException {
        User userFromDB = userDaoImp.getUser(updateUser.getId());
        if(!checkUserName(updateUser)) {
            throw new UserNameException("Пользователь уже существует, укажите другое имя или id");
        }
        userFromDB.setUsername(updateUser.getUsername());
        userFromDB.setSurName(updateUser.getSurName());
        userFromDB.setAge(updateUser.getAge());
        verificationPassword(updateUser,userFromDB);
        userFromDB.setRoles(updateUser.getRoles());
        userDaoImp.add(userFromDB);
    }
    @Transactional
    @Override
    public List<User> listUsers() {
        try {
            Iterable<User> users = userDaoImp.listUsers();
            List<User> result = new ArrayList<>();
             users.forEach(result::add);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }
    @Transactional
    @Override
    public User getUser(long id) throws UserNotFoundException {
        Optional<User> user = Optional.empty();
        try {
            user = Optional.ofNullable(userDaoImp.getUser(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user.orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с таки id - %d не найден", id)));
    }
    @Transactional
    @Override
    public void removeUser(long id) throws UserNotFoundException {
        Optional<User> user = Optional.ofNullable(userDaoImp.getUser(id));
        if (user.isPresent()) {
            userDaoImp.removeUser(user.get());
        } else {
            throw new UserNotFoundException(String.format("Пользователь с таки id - %d не найден", id));
        }
    }
    @Transactional
    @Override
    public User getUserOnNme(String username) {
        Optional<User> user = Optional.empty();
        try {
           user = userDaoImp.findByNameLike(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user.orElseThrow();
    }
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDaoImp.findByNameLike(username);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User %s not found",username));
        }
        return user.get();
    }

    private void verificationPassword(User updateUser, User userFromDB) {
        if (updateUser.getPassword().equals(userFromDB.getPassword())) {
            userFromDB.setPassword(userFromDB.getPassword());
        }
        userFromDB.setPassword(new BCryptPasswordEncoder().encode(updateUser.getPassword()));
    }

    @Transactional
    public boolean checkUserName(User updateUser) {
        Optional<User> userName =  userDaoImp.findByNameLike(updateUser.getUsername());
        if(userName.isEmpty() || (userName.get().getId().equals(updateUser.getId())
                && userName.get().getUsername().equals(updateUser.getUsername()))) {
            return true;
        }
        return false;
    }

}
