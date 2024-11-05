package com.example.demo


import org.springframework.context.annotation.*
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain


@Configuration
class SecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher("/**") // Указывает на URL-шаблоны
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/books/**").hasRole("USER")
                    .anyRequest().authenticated()
            }
            .httpBasic{httpBasicCustomizer ->
                httpBasicCustomizer.realmName("books")
            }
            .sessionManagement{sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            }
            .logout{logout ->
                logout.logoutUrl("/logout")
                    .invalidateHttpSession(true).clearAuthentication(true)
                    .logoutSuccessHandler { request, response, authentication ->
                        response.status = 200
                        response.writer.write("Logout successful")
                    } // Обработчик успешного выхода
            }
            .headers { headers ->
                headers.cacheControl { cache ->
                    cache.disable() // Отключить кэширование
                }
            }

        return http.build()
    }
    @Bean
    fun userDetailsService(): InMemoryUserDetailsManager {
        val user = User.withUsername("user")
            .password(passwordEncoder().encode("password"))
            .authorities("ROLE_USER")
            .build()
        return InMemoryUserDetailsManager(user)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}