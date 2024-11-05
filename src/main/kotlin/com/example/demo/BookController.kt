package com.example.demo
import org.springframework.web.bind.annotation.*



@RestController
@RequestMapping("/books")
class BookController (private val bookRepository: BookRepository) {



    @GetMapping
    fun getBooks() = bookRepository.findAll()
    @GetMapping("/{id}")
    fun getBookById(@PathVariable id: Long): Book {
        return bookRepository.findById(id).orElseThrow { BookNotFoundException(id) }
    }


    @PostMapping
    fun addBook(@RequestBody book: Book): String {

        if (bookRepository.existsById(book.id) ) {
            throw BookAlreadyExistsException(book.id)
        } else {
            bookRepository.save(book)
        }
        return "Book added successfully"
    }

    @PutMapping("/{id}")
    fun updateBook(@PathVariable id: Long, @RequestBody book: Book): Book {
        return bookRepository.findById(id).map { existingBook ->
            val newBook = existingBook.copy(
                title = book.title,
                author = book.author,
            )
        bookRepository.save(newBook)
        }.orElseThrow { BookNotFoundException(id) }


    }

    @DeleteMapping("/{id}")
    fun deleteBook(@PathVariable id:Long): String{

        if (!bookRepository.existsById(id)) {
            throw BookNotFoundException(id)
        }
        bookRepository.deleteById(id)
        return "Book deleted successfully"

    }

    @GetMapping("/search/title")
    fun getBookByTitle(@RequestParam title: String): List<Book> {
        val book = bookRepository.findByTitle(title)
        return if (book.isEmpty()) throw BookNotFoundByTitleException(title) else book
    }
    @GetMapping("/search/author")
    fun getBookByAuthor(@RequestParam author: String): List<Book> {
        val book = bookRepository.findByAuthor(author)
        return if (book.isEmpty()) throw BookNotFoundByAuthorException(author) else book
    }

}