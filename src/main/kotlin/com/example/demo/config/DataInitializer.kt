package com.example.demo.config

import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import jakarta.annotation.PostConstruct
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DataInitializer(private val userRepository: UserRepository,
                      private val passwordEncoder: PasswordEncoder) {
    @PostConstruct
    fun init()  {
       val user = User(
                id = 1,
              username = "user",
              password = passwordEncoder.encode("password"),
              roles = mutableSetOf("USER")
       )

         val admin = User(
                    id = 2,
                  username = "admin",
                  password = passwordEncoder.encode("password"),
                  roles = mutableSetOf("ADMIN", "USER")
         )

        userRepository.save(user)
        userRepository.save(admin)
    }

}