package com.example.springbootstudy.controllers;

import com.example.springbootstudy.domain.Message;
import com.example.springbootstudy.domain.User;
import com.example.springbootstudy.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller

public class MainController {

    @Autowired
    private  MessageRepo messageRepo;

    @GetMapping("")
    public String main(Model model,@AuthenticationPrincipal User user) {
            Iterable<Message> messages = messageRepo.findAll(Sort.by(Sort.Direction.DESC,"id"));
            model.addAttribute("messages",messages);
            if (user != null) {
                model.addAttribute("userProfileAvatar",user.getAvatarFilename());
                model.addAttribute("currentUserId", user.getId());
            }
            return "main";
    }
}
