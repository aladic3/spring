package com.example.demo.repository

import com.example.demo.model.Token
import org.springframework.data.jpa.repository.JpaRepository

interface TokenRepository : JpaRepository<Token, Long> {
    fun findByRefreshToken(refreshToken: String): Token?
    fun deleteByRefreshToken(refreshToken: String)

}