package com.example.demo

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String) = userRepository.findByUsername(username)
        ?: throw UsernameNotFoundException("User with username $username not found")
}