package com.example.demo.config

import com.example.demo.filter.JwtRequestFilter
import com.example.demo.filter.RequestLoggingFilter
import com.example.demo.service.CustomUserDetailsService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Configuration
@EnableWebSecurity
class SecurityConfig {
    companion object {
        const val EXPIRATION_TOKEN_SECOND =  60 // 1 minute
        const val EXPIRATION_REFRESH_TOKEN_SECOND = 60 * 60 * 24 * 7 // 7 days
        }

    @Value("\${jwt.secret}")
    private lateinit var secretKey: String

    @Bean
    fun securityFilterChain(http: HttpSecurity, customUserDetService: CustomUserDetailsService): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/books/**").hasRole("USER")
                    .requestMatchers("/token/**").hasRole("ADMIN")
                    .requestMatchers("/login", "/register", "/refresh").permitAll()
                    .anyRequest().authenticated()
            }
            .logout { logout ->
                logout.logoutSuccessUrl("/")
            }
            .sessionManagement { sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(JwtRequestFilter(secretKey(), customUserDetService), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(RequestLoggingFilter(), JwtRequestFilter::class.java)
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
        val keyGen = SecretKeySpec(secretKey.toByteArray(), "HmacSHA256")
        return keyGen
    }

   /* fun secretKey(): SecretKey {
        val keyGen = KeyGenerator.getInstance("HmacSHA256")
        keyGen.init(256)
        return keyGen.generateKey()
    }

    */
}
