package com.example.springbootstudy.controllers;

import com.example.springbootstudy.domain.User;
import com.example.springbootstudy.services.FileService;
import com.example.springbootstudy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;


@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addNewUser(@RequestParam("password2") String password2,
                             @Valid User user,
                             BindingResult bindingResult,
                             Model model,
                             @RequestParam("file") MultipartFile file) throws IOException {

        if (password2.isEmpty()) {
            model.addAttribute("validationError","Password confirmation can't be empty");
            return "registration";
        }

        if (!user.getPassword().equals(password2)) {
            model.addAttribute("validationError","Passwords are not the same");
            return "registration";
        }

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.addAttribute("map", errorsMap);
            return "registration";
        }

        user.setAvatarFilename(fileService.getFilenameForUserAvatar(file));
        user.setBackgroundFilename("default_background_image.jpeg");

        try {
            userService.createUser(user);
        } catch (UsernameNotFoundException exception) {
            model.addAttribute("validationError","User with this username already exists");
            return "registration";
        }

        return "redirect:login";
    }

    @GetMapping("/activate/{code}")
    public String activation(Model model, @PathVariable String code) {

        if (userService.activateUser(code)) {
            model.addAttribute("message","User successfully activated");
        } else {
            model.addAttribute("message","Activation code is not found");
        }

        return "login";
    }

}
