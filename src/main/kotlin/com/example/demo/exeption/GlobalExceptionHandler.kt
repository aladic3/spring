package com.example.demo.exeption

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(
        BookNotFoundException::class,
        BookNotFoundByAuthorException::class,
        BookNotFoundByTitleException::class)
    fun handleBookNotFoundException(e: BookNotFoundException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    }
    @ExceptionHandler(BookAlreadyExistsException::class)
    fun handleBookAlreadyExistsException(e: BookAlreadyExistsException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.CONFLICT)
    }
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<String> {
        e.printStackTrace()
        return ResponseEntity("An error occurred: ${e.message}" , HttpStatus.INTERNAL_SERVER_ERROR)


    }
    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleUsernameNotFoundException(e: UsernameNotFoundException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.NOT_FOUND)
    }
}