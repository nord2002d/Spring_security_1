package ru.kata.spring.boot_security.demo.web.model;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "users")
public class User implements UserDetails {
    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank( message = "Поле не может быть пустым, заполните поле")
    @Size( min = 2, max = 30, message = "Поел не может быть меньше 2 символов и больше 30")
    @Column(name = "name")
    private String name;
    @NotBlank( message = "Поле не может быть пустым, заполните поле")
    @Size( min = 2, max = 30, message = "Поел не может быть меньше 2 символов и больше 30")
    @Column(name = "sur_name")
    private String surName;
    @Min(value = 1, message = "Минимальное значение поля - 1, укажите корректное значение")
    @Max(value = 130, message = "Максимальное значение поля - 130, укажите корректное значение")
    @NotNull( message = "Поле не может быть пустым, заполните поле")
    @Column(name = "age")
    private Integer age;
    @NotBlank( message = "Поле не может быть пустым, заполните поле")
    @Column(name = "password")
    private String password;
    @NotEmpty(message = "Поле не может быть пустым, выберите РОЛЬ для пользователя")
    @ElementCollection(targetClass = Role.class,fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id",foreignKey = @javax.persistence.ForeignKey(name = "none")))
    @Enumerated(EnumType.STRING)
    @Column(name = "roles")
    private Set<Role> roles;

    public User() {
    }

    public User(String name,String surName,int age,String password, Set<Role> roles) {
        this.name = name;
        this.surName = surName;
        this.age = age;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return age == user.age && Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(surName, user.surName) && Objects.equals(password, user.password) && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surName, age, password, roles);
    }
}
