package com.example.springbootstudy.controllers;

import com.example.springbootstudy.domain.User;
import com.example.springbootstudy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@Controller
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addNewUser(@RequestParam("password2") String password2, @Valid User user, BindingResult bindingResult, Model model) {

        boolean isConfirmEmpty = password2.isEmpty();

        if (isConfirmEmpty) {
            model.addAttribute("password2Error","Password confirmation can't be empty");
        }


        if (!user.getPassword().equals(password2)) {
            model.addAttribute("passwordError","Passwords are not the same");
            return "registration";
        }

        if (isConfirmEmpty || bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.addAttribute("map", errorsMap);
            return "registration";
        }

        if (!userService.createUser(user)) {
            model.addAttribute("map","User with this username already exists");
            return "registration";
        }

        return "redirect:login";
    }

    @GetMapping("/activate/{code}")
    public String activation(Model model, @PathVariable String code) {

        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message","User successfully activated");
        } else {
            model.addAttribute("message","Activation code is not found");
        }

        return "login";
    }

}
