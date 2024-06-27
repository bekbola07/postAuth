package org.example.postauth.controller;

import lombok.RequiredArgsConstructor;
import org.example.postauth.entity.Like;
import org.example.postauth.entity.Post;
import org.example.postauth.entity.User;
import org.example.postauth.repo.LikeRepo;
import org.example.postauth.repo.PostRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";


    private  final PostRepo postRepository;
    private final LikeRepo likeRepo;

    @GetMapping("")
    public String listPosts(Model model) {
        List<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "/admin/posts";
    }

    @GetMapping("/new")
    public String newPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "/admin/new_post";
    }

    @PostMapping("/save")
    public String savePost(@ModelAttribute Post post, @RequestParam("photoFile") MultipartFile photoFile) throws IOException {
        if (!photoFile.isEmpty()) {
            String fileName = UUID.randomUUID()+ "_" + photoFile.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIRECTORY, fileName);
            Files.createDirectories(filePath.getParent());
            photoFile.transferTo(filePath.toFile());
            post.setPhotoPath(fileName);
        }
        postRepository.save(post);
        return "redirect:/post";
    }

    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable("id") UUID id, Model model) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
        model.addAttribute("post", post);
        return "/admin/edit_post";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute Post post,
                             @RequestParam UUID pId,
                             @RequestParam("photoFile") MultipartFile photoFile) throws IOException {
        if (!photoFile.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + photoFile.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIRECTORY, fileName);
            Files.createDirectories(filePath.getParent());
            photoFile.transferTo(filePath.toFile());
            post.setPhotoPath(fileName);
        }else {
            Post postNo = postRepository.findById(pId).orElseThrow(() -> new RuntimeException("post no"));
            post.setPhotoPath(postNo.getPhotoPath());
        }
        postRepository.save(post);
        return "redirect:/post";
    }

    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") UUID id) {
        postRepository.deleteById(id);
        return "redirect:/post";
    }
    @PostMapping("/like")
    public String like(@RequestParam UUID postId, Authentication authentication){
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                User currentUser;
                currentUser = (User) principal;
                User user = currentUser;
                Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("post not me"));

                Like like = Like.builder()
                        .post(post)
                        .user(user)
                        .build();
                likeRepo.save(like);
                return "redirect:/";
            }
        }
        return "redirect:/auth/login";

    }

    @PostMapping("/dislike")
    public String dislike(@RequestParam UUID postId, Authentication authentication){
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                User currentUser;
                currentUser = (User) principal;

                User user = currentUser;
                Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("post not me"));

                List<Like> likes = likeRepo.findAllByPostIdAndUserId(postId, user.getId());

                likeRepo.delete(likes.get(0));

                return "redirect:/";
            }
        }
        return "redirect:/auth/login";

    }



}
