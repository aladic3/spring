package com.example.demo

class BookAlreadyExistsException(id: Long) : RuntimeException("Book with id $id already exists") {
}