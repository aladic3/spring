package com.example.demo



import org.springframework.context.annotation.*

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
class SecurityConfig(private val customUserDetailsService: CustomUserDetailsService,
                     @Lazy private val jwtRequestFilter: JwtRequestFilter)
     {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher("/**") // Указывает на URL-шаблоны
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/books/**").hasRole("USER")
                    .requestMatchers("/login", "/register").permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin { formLogin ->
                formLogin.defaultSuccessUrl("/", true)
                formLogin.loginPage("/login")
                formLogin.loginProcessingUrl("/login")
                formLogin.successForwardUrl("/")



            }
            .logout { logout ->
                logout.logoutSuccessUrl("/")
            }
            .sessionManagement { sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            }
            .csrf{csrf -> csrf.disable()} // Отключаем CSRF
            .addFilterBefore(jwtRequestFilter,
                UsernamePasswordAuthenticationFilter::class.java)


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


         @Bean
         fun secretKey(): SecretKey {
             val keyGen = KeyGenerator.getInstance("HmacSHA256")
             keyGen.init(256)
             return keyGen.generateKey()
         }


         // Используем AuthenticationConfiguration для получения AuthenticationManager
         /* fun configure(auth: AuthenticationManagerBuilder) {
              auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder())
          }
 */

}