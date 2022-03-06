package com.jaemin.springdatajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing //Auditing사용하기 위함
@SpringBootApplication
//@EnableJpaRepositories(basePackages = "com.jaemin.springdatajpa")
public class SpringDataJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataJpaApplication.class, args);
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        // 시큐리티 사용 시 로그인한 사람의 정보로 저장
        /*return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if( authentication == null || !authentication.isAuthenticated()) {
                return null;
            }
            Member member = authentication.getPrincipal();
            return Optional.of(member.getId);
        }*/

        return () -> Optional.of(UUID.randomUUID().toString());
    }
}
