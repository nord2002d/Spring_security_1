package ru.kata.spring.boot_security.demo.web.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.web.exeptions.UserNameException;
import ru.kata.spring.boot_security.demo.web.exeptions.UserNotFoundException;
import ru.kata.spring.boot_security.demo.web.model.User;
import ru.kata.spring.boot_security.demo.web.service.UserService;

import javax.validation.Valid;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    private static final String REDIRECT = "redirect:/admin/users";

    @GetMapping
    public String adminPage() {
        return "admin";
    }

    @GetMapping("/users")
    public String printWelcome(ModelMap model) {
        model.addAttribute("users", userService.listUsers());
        return "users";
    }

    @GetMapping("/formAdd")
    public String getFormAddUser(@ModelAttribute("user") User user) {

        return "add";
    }

    @PostMapping
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) throws UserNameException {
        if (bindingResult.hasErrors()) {
            return "add";
        }
        userService.add(user);
        return REDIRECT;
    }

    @DeleteMapping
    public String removeUser(@RequestParam("id") long id) throws UserNotFoundException {
        userService.removeUser(id);
        return REDIRECT;
    }

    @GetMapping("/formUpdate")
    public String getFormUpdate(ModelMap model, @RequestParam("id") long id) throws UserNotFoundException {
        model.addAttribute("user", userService.getUser(id));
        return "update";
    }

    @PatchMapping
    public String updateUsers(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) throws UserNameException {
        if (bindingResult.hasErrors()) {
            return "update";
        }
        userService.update(user);
        return REDIRECT;
    }

}
