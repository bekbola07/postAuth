package org.example.postauth.controller;

import lombok.RequiredArgsConstructor;
import org.example.postauth.entity.Comment;
import org.example.postauth.entity.Post;
import org.example.postauth.entity.User;
import org.example.postauth.repo.CommentRepo;
import org.example.postauth.repo.PostRepo;
import org.example.postauth.repo.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentRepo commentRepo;
    private final PostRepo postRepo;
    private final UserRepo userRepo;


    @GetMapping("/")
    public String postComments(@RequestParam UUID postId, Model model){
        List<Comment> comments = commentRepo.findAllByPostId(postId);
        Post post = postRepo.findById(postId).orElseThrow(() -> new RuntimeException("post no me"));
        model.addAttribute("comments",comments);
        model.addAttribute("post",post);
        return "/comments";
    }
    @PostMapping
    public String comment(@RequestParam String body, @RequestParam UUID postId, Authentication authentication){
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                User currentUser;
                currentUser = (User) principal;
                User user = currentUser;
                Post post = postRepo.findById(postId).orElseThrow(() -> new RuntimeException("no post"));

                Comment comment = Comment.builder()
                        .user(user)
                        .body(body)
                        .post(post)
                        .build();

                commentRepo.save(comment);
                return "redirect:/comment/"+postId;
            }
        }
        return "redirect:/auth/login";


    }
}
