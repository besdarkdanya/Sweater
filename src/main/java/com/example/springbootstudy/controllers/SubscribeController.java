package com.example.springbootstudy.controllers;

import com.example.springbootstudy.domain.User;
import com.example.springbootstudy.services.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SubscribeController {

    private final SubscribeService subscribeService;

    @Autowired
    public SubscribeController(SubscribeService subscribeService) {
       this.subscribeService = subscribeService;
    }

    @GetMapping("/subscribe/{user}")
    public String subscribe(@AuthenticationPrincipal User authUser,
                            @PathVariable User user) {

        subscribeService.subscribe(authUser,user);

        return "redirect:/page/" + user.getId();
    }

    @GetMapping("/unsubscribe/{user}")
    public String unsubscribe(@AuthenticationPrincipal User authUser,
                            @PathVariable User user) {

        subscribeService.unsubscribe(authUser,user);

        return "redirect:/page/" + user.getId();
    }
}
