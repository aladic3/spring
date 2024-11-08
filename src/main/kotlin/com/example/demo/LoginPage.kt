package com.example.demo

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginPage {
    @GetMapping("/login")
    fun login(): String {
        val loginPage = "login"
        return loginPage
    }
}