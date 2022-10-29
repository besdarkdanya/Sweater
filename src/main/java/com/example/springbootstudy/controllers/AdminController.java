package com.example.springbootstudy.controllers;

import com.example.springbootstudy.domain.Role;
import com.example.springbootstudy.domain.User;
import com.example.springbootstudy.repos.UserRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin-panel")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final UserRepo userRepo;

    public AdminController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @GetMapping()
    public String userList(Model model) {
        Iterable<User> users = userRepo.findAll();
        model.addAttribute("users", users);
        return "userList";
    }

    @GetMapping("/{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    public String editUsers(@RequestParam(name = "userId") User user,
                           @RequestParam String username) {

        user.setUsername(username);
        userRepo.save(user);

        return "redirect:/user";
    }
}
