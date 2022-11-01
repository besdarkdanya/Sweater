package com.example.springbootstudy.controllers;

import com.example.springbootstudy.domain.Message;
import com.example.springbootstudy.domain.User;
import com.example.springbootstudy.repos.MessageRepo;
import com.example.springbootstudy.services.FileService;
import com.example.springbootstudy.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MessageController {

     @Autowired
     private FileService fileService;
     @Autowired
    private MessageRepo messageRepo;
    @Autowired
     private MessageService messageService;

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
            return "main";
        }

        message.setFilename(fileService.getFilenameForMessagePicture(file));
        messageRepo.save(message);

        return "redirect:/";
    }

    @GetMapping("/page/{user}")
    public String getProfilePage(@AuthenticationPrincipal User currentUser,
                                    @PathVariable User user,
                                    Model model) {
        model.addAttribute("messages",user.getMessages());
        try {
            model.addAttribute("currentUserId", user.getId());
            model.addAttribute("userProfileAvatar",user.getAvatarFilename());
            model.addAttribute("usernameOfThisPage",user.getUsername());
            model.addAttribute("totalAmountOfTweets",user.getMessageSet().size() + " Tweets");
            model.addAttribute("userBackgroundImage",user.getBackgroundFilename());
            model.addAttribute("userProfileDescription",user.getDescription());
            model.addAttribute("Following",user.getSubscriptions().size());
            model.addAttribute("Followers",user.getSubscribers().size());
            model.addAttribute("isCurrentUser",currentUser.equals(user));
            model.addAttribute("isAuthUserSubscribe",user.getSubscribers().contains(currentUser));
        } catch (Exception e) {
            model.addAttribute("currentUserId",false);
        }

        return "profile";
    }

    @DeleteMapping("/message-delete/{message}")
    public String deleteMessage(@PathVariable Message message,
                                @AuthenticationPrincipal User currentUser) {

        messageService.deleteMessage(message);

        return "redirect:/user-messages/" + currentUser.getId();
    }
}
