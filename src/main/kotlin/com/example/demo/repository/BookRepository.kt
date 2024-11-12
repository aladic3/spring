package com.example.demo.repository

import com.example.demo.model.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository: JpaRepository<Book, Long> {
    fun findByTitle(title: String): List<Book>
    fun findByAuthor(author: String): List<Book>
}