package com.example.demo

class BookNotFoundException (id: Long) : RuntimeException("Book with id $id not found")
class BookNotFoundByAuthorException (author: String) :  RuntimeException("Book with author $author not found")
class BookNotFoundByTitleException (title: String) :  RuntimeException("Book with title $title not found")