package org.example.postauth.component;

import lombok.RequiredArgsConstructor;
import org.example.postauth.entity.Role;
import org.example.postauth.entity.User;
import org.example.postauth.repo.RoleRepo;
import org.example.postauth.repo.UserRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor

public class Runner implements CommandLineRunner {
    @Value("${spring.jpa.hibernate.ddl-auto}")
    String ddl;

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        if (ddl.equals("create")) {


        Role rol1 = new Role("ROLE_SUPER_ADMIN");
        Role rol2 = new Role("ROLE_USER");
        Role rol3 =new Role("ROLE_ADMIN");
        Role rol4 = new Role("ROLE_OPERATOR");
        Role rol5 = new Role("ROLE_SELLER");


            roleRepo.save(rol2);
            roleRepo.save(rol1);
            roleRepo.save(rol3);
            roleRepo.save(rol4);
            roleRepo.save(rol5);


            for (int i = 1; i <= 5; i++) {
                userRepo.save(User.builder()
                        .id(i)
                        .fullName("Eshamt" + i)
                        .password(passwordEncoder.encode("123"))
                        .username("admin" + i)
                        .phoneNumber("+998123456"+i)
                        .roles(List.of(rol1,rol5,rol4, rol3))
                        .build());
            }
        }
    }
}
