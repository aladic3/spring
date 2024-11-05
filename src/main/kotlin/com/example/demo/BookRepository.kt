package com.example.demo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository: JpaRepository<Book, Long> {
    fun findByTitle(title: String): List<Book>
    fun findByAuthor(author: String): List<Book>
}