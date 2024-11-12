package com.example.demo.controller

import com.example.demo.service.TokenService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/token")
class TokenController (private val tokenService: TokenService) {
    @GetMapping("/all")
    fun getAllToken(): String {
        return tokenService.getAllTokens().toString()
    }

    @GetMapping("/delete")
    fun deleteAllToken(): String {
        tokenService.deleteAllTokens()
        return "All tokens deleted"
    }

}