package com.example.demo.service

import com.example.demo.model.Token
import com.example.demo.model.User
import com.example.demo.repository.TokenRepository
import com.example.demo.util.JwtUtil
import org.springframework.stereotype.Service

@Service
class TokenService(private val tokenRepository: TokenRepository, private val jwtUtil: JwtUtil) {
    fun addToken(token: Token) {
        tokenRepository.save(token)
    }
    fun getToken(token: String): Token? {
        return tokenRepository.findByRefreshToken(token)
    }
    fun deleteToken(refreshToken: String) {
        tokenRepository.deleteByRefreshToken(refreshToken)
    }
    fun getAllTokens(): List<Token> {
        return tokenRepository.findAll()
    }
    fun deleteAllTokens() {
        tokenRepository.deleteAll()
    }
    fun generateRefreshToken(user: User?): Token {
        if (user == null) {
            throw IllegalArgumentException("User is null")
        }
        val token = jwtUtil.generateRefreshToken(user.username)
        val expiration = jwtUtil.extractExpiration(token)
        val gToken = Token(user = user, refreshToken = token, expirationTime = expiration)

        return tokenRepository.save(gToken)
    }

}