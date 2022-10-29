package com.example.springbootstudy.controllers;

import com.example.springbootstudy.domain.User;
import com.example.springbootstudy.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping("/profile")
    public String myProfile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("avatar",user.getFilename());
        model.addAttribute("currentUserId", user.getId());
        return "profile";
    }

    @PostMapping("/profile")
    public String editUser(@AuthenticationPrincipal User currentUser ,User user) {

        userService.updateProfile(currentUser,user);

        return "redirect:profile";
    }
}
