package com.example.demo.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class RequestLoggingFilter: OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(RequestLoggingFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // Сохраняем время начала обработки запроса
        request.setAttribute("startTime", System.currentTimeMillis())
        // Логируем информацию о каждом запросе:
        // метод (GET, POST и т.д.), URL и IP-адрес клиента
        logger.info("Request: ${request.method} ${request.requestURI} from ${request.remoteAddr}")



        // Логируем информацию о каждом ответе:
        // статус ответа и время обработки запроса
        val startTime = request.getAttribute("startTime") as Long
        val endTime = System.currentTimeMillis()

        logger.info("Response: ${response.status}")
        logger.info("Request processed in ${endTime - startTime} ms")

        filterChain.doFilter(request, response)

    }
}