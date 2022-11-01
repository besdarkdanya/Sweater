package com.example.springbootstudy.controllers;

import com.example.springbootstudy.domain.User;
import com.example.springbootstudy.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Controller
public class ProfileController {


    private final UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String myProfile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("avatar",user.getAvatarFilename());
        model.addAttribute("currentUserId", user.getId());
        return "user-settings";
    }

    @PostMapping("/profile")
    public String editUser(@AuthenticationPrincipal User currentUser ,
                           @RequestParam(required = false) MultipartFile file,
                           User user,
                           BindingResult bindingResult,
                           Model model) throws IOException {

        model.addAttribute("map",ControllerUtils.getErrors(bindingResult));

        userService.updateProfile(currentUser,user,file);

        return "redirect:profile";
    }
}
