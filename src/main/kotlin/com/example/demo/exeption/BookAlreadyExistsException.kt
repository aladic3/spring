package com.example.demo.exeption

class BookAlreadyExistsException(id: Long) : RuntimeException("Book with id $id already exists")