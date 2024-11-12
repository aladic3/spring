package com.example.demo.util

import com.example.demo.config.SecurityConfig
import com.example.demo.model.TokenType
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtUtil(private val secretKey: SecretKey) {

    fun generateRefreshToken(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .claim("type", TokenType.REFRESH_TOKEN.name)
            .setExpiration(Date(System.currentTimeMillis() + 1000 * SecurityConfig.EXPIRATION_REFRESH_TOKEN_SECOND))
            .signWith(secretKey)

            .compact()
    }

    fun extractClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body

    }
    fun extractType(token: String): TokenType {
        return extractClaims(token).get("type", String::class.java).let { TokenType.valueOf(it) }
    }
    fun generateAccessToken(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .claim("type", TokenType.ACCESS_TOKEN.name)
            .setExpiration(Date(System.currentTimeMillis() + 1000 * SecurityConfig.EXPIRATION_TOKEN_SECOND))
            .signWith(secretKey)
            .compact()
    }

    fun extractUsername(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }

    fun validateToken(token: String): Boolean {
        return try {
          !isTokenExpired(token) && extractClaims(token).subject.isNotEmpty()
        } catch (e: JwtException) {
            false
        }
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    fun extractExpiration(token: String): Date {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .expiration
    }

    fun extractClaim(token: String, claimResolver: (Claims) -> Any): Any {
        val claims = extractClaims(token)
        return claimResolver(claims)
    }
}