package com.example.demo


import org.springframework.context.annotation.*

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

import org.springframework.security.web.SecurityFilterChain



@Configuration
@EnableWebSecurity
class SecurityConfig(private val customUserDetailsService: CustomUserDetailsService)
     {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher("/**") // Указывает на URL-шаблоны
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/books/**").hasRole("USER")
                    .anyRequest().authenticated()
            }
            .formLogin { formLogin ->
                formLogin.defaultSuccessUrl("/", true)


            }
            .logout { logout ->
                logout.logoutSuccessUrl("/")
            }
            .sessionManagement { sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            }

        http.csrf{csrf -> csrf.disable()} // Отключаем CSRF
        return http.build()
    }
         /*
    @Bean
    fun userDetailsService(): InMemoryUserDetailsManager {
        val user = User.withUsername("user")
            .password(passwordEncoder().encode("password"))
            .authorities("ROLE_USER")
            .build()
        return InMemoryUserDetailsManager(user)
    }
*/

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }


         // Используем AuthenticationConfiguration для получения AuthenticationManager
         // Метод configure для настройки AuthenticationManagerBuilder
        /* fun configure(auth: AuthenticationManagerBuilder) {
             auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder())
         }
*/

}