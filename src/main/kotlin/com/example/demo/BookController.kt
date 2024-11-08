package com.example.demo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*



@RestController
@RequestMapping("/books")
class BookController (private val bookService: BookService) {



    @GetMapping
    fun getBooks() = bookService.getBooks()
    @GetMapping("/{id}")
    fun getBookById(@PathVariable id: Long): Book {
        return bookService.getBookById(id)
    }

    @PostMapping
    fun addBook(@RequestBody book: Book): ResponseEntity<String> {
        return bookService.addBook(book)
    }


    @PostMapping("/addBooks")
    fun addBooks(@RequestBody books: List<Book>): ResponseEntity<String>  {

        return ResponseEntity.ok(bookService.addBooks(books).toString())
    }

    @PutMapping("/{id}")
    fun updateBook(@PathVariable id: Long, @RequestBody book: Book): ResponseEntity<String> {
        return bookService.updateBook(id, book)


    }

    @DeleteMapping("/{id}")
    fun deleteBook(@PathVariable id:Long): ResponseEntity<String> {


        return bookService.deleteBook(id)

    }

    @GetMapping("/search/title")
    fun getBookByTitle(@RequestParam title: String): List<Book> {
        return bookService.getBookByTitle(title)
    }
    @GetMapping("/search/author")
    fun getBookByAuthor(@RequestParam author: String): List<Book> {
        return bookService.getBookByAuthor(author)
    }

}