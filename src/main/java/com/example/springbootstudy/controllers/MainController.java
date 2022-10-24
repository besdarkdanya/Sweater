package com.example.springbootstudy.controllers;

import com.example.springbootstudy.domain.Message;
import com.example.springbootstudy.domain.User;
import com.example.springbootstudy.repos.MessageRepo;
import com.example.springbootstudy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;


@Controller
public class MainController {

    MessageRepo messageRepo;

    private final UserService userService;

    @Autowired
    public MainController(MessageRepo messageRepo,UserService userService) {
        this.messageRepo = messageRepo;
        this.userService = userService;
    }

    @Value("${upload.path}")
    private String uploadPath;


    @GetMapping("")
    public String main(Model model,@AuthenticationPrincipal User user) {
        if (user != null) {
            Iterable<Message> messages = messageRepo.findAll(Sort.by(Sort.Direction.DESC,"id"));

            model.addAttribute("messages",messages);
            return "main";
        }
        return "redirect:login";
    }

    @GetMapping("/send")
    public String sendPage() {
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

            model.mergeAttributes(errorsMap);


        } else {
            if (file != null) {
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
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages",messages);

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
    public String myProfile() {
        return "profile";
    }

    @PostMapping("/profile")
    public String editUser(@AuthenticationPrincipal User currentUser ,User user) {

        userService.updateProfile(currentUser,user);

        return "redirect:profile";
    }
}
