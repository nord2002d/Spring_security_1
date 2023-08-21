package ru.kata.spring.boot_security.demo.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.web.exeptions.UserNameException;
import ru.kata.spring.boot_security.demo.web.exeptions.Violation;

import javax.validation.ConstraintViolationException;
import java.util.List;


@ControllerAdvice
public class ControllerExceptionHandler {
    private static final String ERROR = "error";
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ UserNameException.class })
    ModelAndView handleUserNameDuplicated(UserNameException ex) {
        ModelAndView modelAndView = new ModelAndView(ERROR);
        ModelMap modelMap = modelAndView.getModelMap();
        modelMap.addAttribute(ERROR,ex.getMessage());
        return modelAndView;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    ModelAndView onConstraintValidationException(
            ConstraintViolationException e) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> new Violation(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .toList();
        ModelAndView modelAndView = new ModelAndView(ERROR);
        ModelMap modelMap = modelAndView.getModelMap();
        modelMap.addAttribute(ERROR, violations);
        return modelAndView;
    }

}
