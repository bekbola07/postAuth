package org.example.postauth.controller;

import lombok.RequiredArgsConstructor;
import org.example.postauth.entity.Role;
import org.example.postauth.entity.User;
import org.example.postauth.repo.RoleRepo;
import org.example.postauth.repo.UserRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/super")

public class SuperAdminController {
   private final UserRepo userRepo;
   private final RoleRepo roleRepo;
    @GetMapping
    public String superPage(Model model){
        model.addAttribute("users", userRepo.findAll());
        return "/super/users";
    }
    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Integer id, Model model){

        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("user no"));
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleRepo.findAll());

        return "/super/edit";
    }
    @PostMapping("/edit/{id}")
    public String updateUserRoles(@PathVariable("id") Integer id, @RequestParam List<UUID> roleIds) {
        User user = userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        List<Role> roles = roleRepo.findAllById(roleIds);
        user.setRoles(roles);
        userRepo.save(user);
        return "redirect:/super";  // Assuming there's a list page to redirect to after saving
    }
}
