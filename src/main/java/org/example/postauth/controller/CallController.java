package org.example.postauth.controller;

import lombok.RequiredArgsConstructor;
import org.example.postauth.entity.Message;
import org.example.postauth.repo.MessageRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/seller")
public class CallController {
    private final MessageRepo messageRepo;
    @GetMapping
    public String sellerPage(Model model){
        List<Message> messages = messageRepo.findAll().stream()
                .filter(Message::isReady)
                .collect(Collectors.toList());

        model.addAttribute("messages",messages);
        return "seller";
    }
}
