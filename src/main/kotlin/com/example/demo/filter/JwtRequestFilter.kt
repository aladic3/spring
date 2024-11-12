package com.example.demo.filter

import com.example.demo.model.TokenType
import com.example.demo.util.JwtUtil
import io.jsonwebtoken.Jwts
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtRequestFilter(
    private val secretKey: SecretKey,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {


    private fun validateToken(token: String, username: String): Boolean {
        val claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body

        return claims.subject == username && !claims.expiration.before(Date())
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")

        var username: String? = null
        var jwt: String? = null


        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7)
            username = extractUsername(jwt)
        } else {
            filterChain.doFilter(request, response)
            return
        }

        val jwtUtil = JwtUtil(secretKey)
        val tokenType = jwtUtil.extractType(jwt)
        if (tokenType == TokenType.ACCESS_TOKEN && !request.requestURI.contains("/refresh")) {


            if (SecurityContextHolder.getContext().authentication == null) {
                val userDetails = userDetailsService.loadUserByUsername(username)

                if (validateToken(jwt, userDetails.username)) {
                    val authenticationToken =
                        UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authenticationToken
                }
            }
        }
        if (tokenType == TokenType.REFRESH_TOKEN && request.requestURI.contains("/refresh")) { // access to refresh token
            if (SecurityContextHolder.getContext().authentication == null) {
                val userDetails = userDetailsService.loadUserByUsername(username)

                if (validateToken(jwt, userDetails.username)) {
                    true
                } else {
                    response.status = 401
                    filterChain.doFilter(request, response)
                    return
                }
            }


        }

        filterChain.doFilter(request, response)
    }

    private fun extractUsername(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }
}
