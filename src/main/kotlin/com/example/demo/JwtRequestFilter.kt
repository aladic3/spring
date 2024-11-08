package com.example.demo
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter

@Service
class JwtRequestFilter(private val jwtUtil: JwtUtil,
    private val customUserDetailsService: CustomUserDetailsService) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: jakarta.servlet.http.HttpServletRequest,
        response: HttpServletResponse,
        filterChain: jakarta.servlet.FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            val token = authorizationHeader.substring(7)
            val username =  jwtUtil.extractUsername(token)
            if (SecurityContextHolder.getContext().authentication == null) {
                val userDetails = customUserDetailsService.loadUserByUsername(username)
                if (jwtUtil.validateToken(token, userDetails.username)) {
                    val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        }
        filterChain.doFilter(request, response)
    }
}
