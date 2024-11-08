package com.example.demo

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(BookNotFoundException::class,
        BookNotFoundByAuthorException::class,
        BookNotFoundByTitleException::class)
    fun handleBookNotFoundException(e: BookNotFoundException): ResponseEntity<String> {
        return ResponseEntity(e.message, org.springframework.http.HttpStatus.NOT_FOUND)

    }
    @ExceptionHandler(BookAlreadyExistsException::class)
    fun handleBookAlreadyExistsException(e: BookAlreadyExistsException): ResponseEntity<String> {
        return ResponseEntity(e.message, org.springframework.http.HttpStatus.CONFLICT)
    }
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<String> {
        return ResponseEntity("An error occurred: ${e.message}" , org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)


    }
    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleUsernameNotFoundException(e: UsernameNotFoundException): ResponseEntity<String> {
        return ResponseEntity(e.message, org.springframework.http.HttpStatus.NOT_FOUND)
    }
}