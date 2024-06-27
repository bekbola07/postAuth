package org.example.postauth.controller;

import lombok.RequiredArgsConstructor;
import org.example.postauth.entity.Message;
import org.example.postauth.entity.User;
import org.example.postauth.repo.MessageRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {
    private final MessageRepo messageRepo;

    @GetMapping
    public String messagesPage(Model model){
        List<Message> messages = messageRepo.findAll()
                .stream()
                .sorted((m1, m2) -> m2.getSend_at().compareTo(m1.getSend_at()))
                .toList();

        model.addAttribute("messages", messages);
        return "/messages";

    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam String body, Authentication authentication){
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                User user = (User) principal;
                Message message = Message.builder()
                        .user(user)
                        .body(body)
                        .build();
                messageRepo.save(message);
                return "redirect:/";
            }
        }
        return "redirect:/auth/login";
    }
    @PostMapping("/changeReady")
    public String makeReady(@RequestParam UUID messageId){
        Message message = messageRepo.findById(messageId).orElseThrow(() -> new RuntimeException("no message to make ready"));
        message.setReady(!message.isReady());

        messageRepo.save(message);

        return "redirect:/message";


    }
}
