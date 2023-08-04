package ru.kata.spring.boot_security.demo.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.web.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByNameLike(String userName);

}
