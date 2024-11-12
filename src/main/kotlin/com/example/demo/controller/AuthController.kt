package com.example.demo.controller

import com.example.demo.model.LoginRequest
import com.example.demo.model.RefreshToken
import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import com.example.demo.service.CustomUserDetailsService
import com.example.demo.service.TokenService
import com.example.demo.util.JwtUtil
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(private val jwtUtil: JwtUtil, private val customUserDetailsService: CustomUserDetailsService,
                     private val passwordEncoder: PasswordEncoder,
                     private val userRepository: UserRepository,
    private val tokenService: TokenService
) {
    @PostMapping("/refresh")
    fun refresh(@RequestBody refreshToken: RefreshToken): ResponseEntity<Any> {
        val refreshToken = refreshToken.refreshToken
        if (jwtUtil.validateToken(refreshToken)) {

            val newToken = jwtUtil.generateAccessToken(jwtUtil.extractUsername(refreshToken))
            return ResponseEntity.ok(mapOf("access_token" to newToken))
        }
        return ResponseEntity.status(401).build()
    }
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        val user = customUserDetailsService.loadUserByUsername(loginRequest.username)
        if (passwordEncoder.matches(loginRequest.password, user.password)) {
            val token = jwtUtil.generateAccessToken(user.username)
            val refreshToken = tokenService.generateRefreshToken(user)
            return ResponseEntity.ok(mapOf("access_token" to token, "refresh_token" to refreshToken.getRefreshToken()))
        }
        return ResponseEntity.status(401).build()
    }
    @PostMapping("/register")
    fun register(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        val user = User(
            username = loginRequest.username,
            password = passwordEncoder.encode(loginRequest.password),
            roles = mutableSetOf("USER")
        )
        userRepository.save(user)
        return ResponseEntity.ok("register")


    }
}