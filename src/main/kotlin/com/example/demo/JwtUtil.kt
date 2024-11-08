package com.example.demo

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtUtil (private val secretKey: SecretKey) {
    fun generateToken(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
            .signWith(secretKey)
            .compact()
    }
    fun generateToken(user: User): String {
        return Jwts.builder()
            .setSubject(user.username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
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
    fun extractExpiration(token: String): Date {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .expiration
    }
    fun extractClaim(token: String, claimResolver: (claims: Map<String, Any>) -> String): String {
        val claims = extractAllClaims(token)
        return claimResolver(claims)
    }
    fun extractAllClaims(token: String): Map<String, Any> {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun validateToken(token: String, username: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .body.subject == username && !isTokenExpired(token)


        } catch (e: JwtException) {
            false
        }
    }

    private fun isTokenExpired(token: kotlin.String): kotlin.Boolean {
        return extractExpiration(token).before(Date())
    }


}