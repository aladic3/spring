package com.example.demo

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class BookService(private val bookRepository: BookRepository) {
    fun addBook(book: Book): ResponseEntity<String> {
        if (bookRepository.existsById(book.id)) {
            throw BookAlreadyExistsException(book.id)
        } else {
            bookRepository.save(book)
        }
        return ResponseEntity.ok("Book added successfully")
    }
    fun addBooks(books: List<Book>): ResponseEntity<List<Book>> {
        books.forEach {
            if (bookRepository.existsById(it.id)) {
                throw BookAlreadyExistsException(it.id)
            } else {
                bookRepository.save(it)
            }
        }
        return ResponseEntity.ok(books)
    }
    fun getBooks() = bookRepository.findAll()

    fun getBookById( id: Long): Book {
        return bookRepository.findById(id).orElseThrow { BookNotFoundException(id) }
    }

    fun updateBook(id: Long,  book: Book): ResponseEntity<String> {
        return bookRepository.findById(id).map { existingBook ->
            val newBook = existingBook.copy(
                title = book.title,
                author = book.author,
            )
            bookRepository.save(newBook)
        }.orElseThrow { BookNotFoundException(id) }.let { ResponseEntity.ok("Book updated successfully") }
    }

    fun deleteBook( id:Long): ResponseEntity<String> {

        if (!bookRepository.existsById(id)) {
            throw BookNotFoundException(id)
        }
        bookRepository.deleteById(id)
        return ResponseEntity.ok("Book deleted successfully")
    }

    fun getBookByTitle( title: String): List<Book> {
        val book = bookRepository.findByTitle(title)
        return if (book.isEmpty()) throw BookNotFoundByTitleException(title) else book
    }

    fun getBookByAuthor( author: String): List<Book> {
        val book = bookRepository.findByAuthor(author)
        return if (book.isEmpty()) throw BookNotFoundByAuthorException(author) else book
    }

}