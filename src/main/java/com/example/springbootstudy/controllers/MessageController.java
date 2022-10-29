package com.example.springbootstudy.controllers;

import com.example.springbootstudy.domain.Message;
import com.example.springbootstudy.domain.User;
import com.example.springbootstudy.repos.MessageRepo;
import com.example.springbootstudy.services.FileService;
import com.example.springbootstudy.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final FileService fileService;
    private final MessageRepo messageRepo;
    private final MessageService messageService;

    @GetMapping("/send-message")
    public String sendMessagePage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("avatar",user.getFilename());
        model.addAttribute("currentUserId", user.getId());
        return "send";
    }


    @PostMapping("/send-message")
    public String sendNewMessage(@AuthenticationPrincipal User user,
                                 @Valid Message message,
                                 BindingResult bindingResult,
                                 Model model,
                                 @RequestParam("file") MultipartFile file) throws IOException {


        message.setAuthor(user);

        if (bindingResult.hasErrors())  {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.addAttribute("validationErrors",errorsMap);
            return "send";
        }

        message.setFilename(fileService.getFilenameForUserAvatar(file));
        messageRepo.save(message);

        return "redirect:/";
    }

    @GetMapping("/user-messages/{user}")
    public String getMyMessagesPage(@AuthenticationPrincipal User currentUser,
                                    @PathVariable User user,
                                    Model model) {
        model.addAttribute("messages",user.getMessages());
        model.addAttribute("isCurrentUser",currentUser.equals(user));
        model.addAttribute("currentUserId", user.getId());
        model.addAttribute("avatar",currentUser.getFilename());

        return "user-messages";
    }

    @DeleteMapping("/message-delete/{message}")
    public String deleteMessage(@PathVariable Message message,
                                @AuthenticationPrincipal User currentUser) {

        messageService.deleteMessage(message);

        return "redirect:/user-messages/" + currentUser.getId();
    }
}
