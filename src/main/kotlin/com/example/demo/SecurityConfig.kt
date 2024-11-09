package com.example.demo

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity, customUserDetService: CustomUserDetailsService): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/books/**").hasRole("USER")
                    .requestMatchers("/login", "/register").permitAll()
                    .anyRequest().authenticated()
            }
            .logout { logout ->
                logout.logoutSuccessUrl("/")
            }
            .sessionManagement { sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(JwtRequestFilter(secretKey(),customUserDetService), UsernamePasswordAuthenticationFilter::class.java)
            .csrf { csrf -> csrf.disable() }
        // No need to add JwtRequestFilter here as it's a component

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun secretKey(): SecretKey {
        val keyGen = KeyGenerator.getInstance("HmacSHA256")
        keyGen.init(256)
        return keyGen.generateKey()
    }
}
