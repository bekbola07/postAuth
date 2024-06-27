package org.example.postauth.controller;

import lombok.RequiredArgsConstructor;
import org.example.postauth.entity.Role;
import org.example.postauth.entity.User;
import org.example.postauth.repo.LikeRepo;
import org.example.postauth.repo.PostRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final PostRepo postRepo;
    private final LikeRepo likeRepo;

    @GetMapping
    public String mainPage(Model model, Authentication authentication){
        model.addAttribute("posts",postRepo.findAll());
        User currentUser;
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                 currentUser = (User) principal;
                model.addAttribute("currentUser",currentUser);


                model.addAttribute("roles",currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
                List<UUID> postIds = likeRepo.findAllByUserId(currentUser.getId()).stream().map(like -> like.getPost().getId())
                        .collect(Collectors.toList());
                model.addAttribute("likes", postIds);
            }
        }else {
            model.addAttribute("currentUser", null);
            model.addAttribute("roles", null);
        }

        return "/index";
    }


}
