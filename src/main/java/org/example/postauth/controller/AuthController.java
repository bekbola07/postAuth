package org.example.postauth.controller;

import lombok.RequiredArgsConstructor;
import org.example.postauth.entity.Role;
import org.example.postauth.entity.User;
import org.example.postauth.repo.RoleRepo;
import org.example.postauth.repo.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    @GetMapping("/login")
    public String loginPage(){
        return "/auth/login-page";
    }

    @GetMapping("/logout")
    public String logoutPage(){
        return "/auth/logout";
    }
    @PostMapping("/logout")
    public String logout(){
        return "/auth/logout";
    }
    @GetMapping("/register")
    public String registerPage(){
        return "/auth/register";
    }
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String phone,
                           @RequestParam String password,
                           @RequestParam String fullName){
        List<Role> roles = roleRepo.findAll();

        userRepo.save(User.builder()
                .fullName(fullName)
                .password(passwordEncoder.encode(password))
                .username(username)
                .phoneNumber(phone)
                .roles(List.of(roles.get(1)))
                .build());
        return "redirect:/auth/login";

    }
}
