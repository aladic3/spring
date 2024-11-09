package com.example.demo
/*import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import org.springframework.context.annotation.Bean
import org.springframework.web.filter.CommonsRequestLoggingFilter
import org.springframework.context.annotation.Configuration

@Component
class RequestLoggingFilter : Filter {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val params = httpRequest.parameterMap
            .map { "${it.key}=${it.value.joinToString()}" }
            .joinToString(", ")
        println("Request Params: $params")
        chain.doFilter(request, response)
    }

    override fun init(filterConfig: FilterConfig?) {}
    override fun destroy() {}
}



@Configuration
class RequestLoggingConfig {

    @Bean
    fun requestLoggingFilter(): CommonsRequestLoggingFilter {
        val filter = CommonsRequestLoggingFilter()
        filter.setIncludeQueryString(true)
        filter.setIncludePayload(true)
        filter.setIncludeHeaders(false) // Set to true if you want headers logged as well
        filter.setMaxPayloadLength(10000)
        return filter
    }
}*/