package com.example.springbootstudy.controllers;

import com.example.springbootstudy.domain.Message;
import com.example.springbootstudy.domain.User;
import com.example.springbootstudy.repos.MessageRepo;
import com.example.springbootstudy.services.MessageService;
import com.example.springbootstudy.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


@Controller
public class MainController {

    private final MessageRepo messageRepo;

    private final UserService userService;

    private final MessageService messageService;

    @Autowired
    public MainController(MessageRepo messageRepo,UserService userService,MessageService messageService) {
        this.messageRepo = messageRepo;
        this.userService = userService;
        this.messageService = messageService;
    }

    @Value("${upload.path}")
    private String uploadPath;


    @GetMapping("")
    public String main(Model model,@AuthenticationPrincipal User user) {
        if (user != null) {
            Iterable<Message> messages = messageRepo.findAll(Sort.by(Sort.Direction.DESC,"id"));
            model.addAttribute("messages",messages);
            model.addAttribute("avatar",user.getFilename());
            model.addAttribute("currentUserId", user.getId());
            return "main";
        }
        return "redirect:login";
    }

    @GetMapping("/send")
    public String sendPage(@AuthenticationPrincipal User user,Model model) {
        model.addAttribute("avatar",user.getFilename());
        model.addAttribute("currentUserId", user.getId());
        return "send";
    }


    @PostMapping("/send")
    public String add(@AuthenticationPrincipal User user,
                      @Valid Message message,
                      BindingResult bindingResult,
                      Model model,
                      @RequestParam("file")MultipartFile file) throws IOException {


        message.setAuthor(user);

        if (bindingResult.hasErrors())  {
           Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
           model.addAttribute("map",errorsMap);
           return "send";
        } else {
            if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
                File uploadDir = new File(uploadPath);

                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + file.getOriginalFilename();

                file.transferTo(new File(uploadPath + "/" + resultFilename));

                message.setFilename(resultFilename);
            }
            messageRepo.save(message);
        }
        Iterable<Message> messages = messageRepo.findAll(Sort.by(Sort.Direction.DESC,"id"));
        model.addAttribute("messages",messages);
        model.addAttribute("avatar",user.getFilename());
        model.addAttribute("currentUserId", user.getId());

        return "main";
    }

    @PostMapping("/filter")
    public String filter(@RequestParam String filter,Model model) {
        Iterable<Message> messages;
        if (filter != null) {
            messages = messageRepo.findByTag(filter);
            model.addAttribute("messages", messages);
        } else {
            messages = messageRepo.findAll();
            model.addAttribute("messages", messages);
        }
        return "main";
    }

    @GetMapping("/profile")
    public String myProfile(@AuthenticationPrincipal User user,Model model) {
        model.addAttribute("avatar",user.getFilename());
        model.addAttribute("currentUserId", user.getId());
        return "profile";
    }

    @PostMapping("/profile")
    public String editUser(@AuthenticationPrincipal User currentUser ,User user) {

        userService.updateProfile(currentUser,user);

        return "redirect:profile";
    }

    @GetMapping("/user-messages/{user}")
    public String getMyMessagesPage(@AuthenticationPrincipal User currentUser,
                                    @PathVariable User user,
                                    Model model) {
        model.addAttribute("messages",user.getMessages());
        model.addAttribute("isCurrentUser",currentUser.equals(user));
        model.addAttribute("currentUserId", user.getId());

        return "user-messages";
    }

    @DeleteMapping("/message-delete/{message}")
    public String deleteMessage(@PathVariable Message message,
                                @AuthenticationPrincipal User currentUser) {

        messageService.deleteMessage(message);

        return "redirect:/user-messages/" + currentUser.getId();
    }
}
