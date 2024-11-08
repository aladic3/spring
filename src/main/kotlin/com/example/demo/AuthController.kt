package com.example.demo

import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(private val jwtUtil: JwtUtil, private val customUserDetailsService: CustomUserDetailsService,
                      private val passwordEncoder: PasswordEncoder,
                      private val userRepository: UserRepository) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        val user = customUserDetailsService.loadUserByUsername(loginRequest.username)
        if (passwordEncoder.matches(loginRequest.password, user.password)) {
            val token = jwtUtil.generateToken(user)
            return ResponseEntity.ok(mapOf("token" to token))
        }
        return ResponseEntity.status(401).build()
    }
    @RequestMapping("/register")
    fun register(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        val user = User(
            id = userRepository.count() + 1,
            username = loginRequest.username,
            password = passwordEncoder.encode(loginRequest.password),
            roles = mutableSetOf("USER")
        )
        userRepository.save(user)
        return ResponseEntity.ok("register")


    }
}