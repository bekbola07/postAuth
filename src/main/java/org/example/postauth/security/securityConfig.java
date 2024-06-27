package org.example.postauth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class securityConfig {
    private final CustomUserDetailService userDetailService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable);


        http.authorizeHttpRequests(manager->{
            manager.requestMatchers("/","/css/**","/uploads/**","/auth/**","/post/dislike","/post/like","/message/send").permitAll()
                    .requestMatchers("/message/**").hasRole("OPERATOR")
                    .requestMatchers("/post/**").hasRole("ADMIN")
                    .requestMatchers("/seller").hasRole("SELLER")
                    .requestMatchers("/super/**").hasRole("SUPER_ADMIN")
                    .anyRequest().authenticated();
        });
        http.formLogin(m->{
            m.loginPage("/auth/login")
                    .defaultSuccessUrl("/");
        });

        http.logout(m->{
           m.logoutUrl("/auth/logout").logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"));
        });

        http.userDetailsService(userDetailService);
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
