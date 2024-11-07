package com.example.demo

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
@Controller
class LoginController {
    @GetMapping("/login")
    fun login(@AuthenticationPrincipal oauthUser: OAuth2User?, model: Model): String {
        if (oauthUser != null) {
            model.addAttribute("name", oauthUser.getAttribute("name"))
            return "home"
        }
        return "login"
    }

    @GetMapping("/")
    fun home(@AuthenticationPrincipal oauthUser: OAuth2User?, model: Model): String {
        model.addAttribute("name", oauthUser?.getAttribute("name") ?: "Anonymous")
        return "home"
    }
}