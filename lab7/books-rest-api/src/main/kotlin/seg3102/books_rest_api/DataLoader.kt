package seg3102.books_rest_api

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import seg3102.books_rest_api.entities.Author
import seg3102.books_rest_api.entities.Bio
import seg3102.books_rest_api.entities.Book
import seg3102.books_rest_api.repo.AuthorRepository
import seg3102.books_rest_api.repo.BookRepository

// Adds a few books and authors when the database is empty, so that there is
// something to look up on the Books and Authors pages. Delete this class to
// start with an empty database instead.
@Component
class DataLoader(val bookRepository: BookRepository,
                 val authorRepository: AuthorRepository) : CommandLineRunner {

    @Transactional
    override fun run(vararg args: String) {
        if (bookRepository.count() > 0 || authorRepository.count() > 0) return

        val springBook = book("Tech", "SpringBoot JPA and REST", 25.45, 2017,
                "This is by far the best resource for SpringBoot, JPA and REST")
        val angularBook = book("Tech", "Introduction to Angular", 50.25, 2019,
                "Components, routing and forms")
        val kidsBook = book("Kids", "A Fantastic Story", 12.25, 2009, "")
        bookRepository.saveAll(listOf(springBook, angularBook, kidsBook))

        author("Koredo", "Tablanga", "Koredo writes about server side development with Spring.",
                springBook, angularBook)
        author("Jane", "C", "Jane writes stories for young readers.", kidsBook)
    }

    private fun book(category: String, title: String, cost: Double, year: Int, description: String): Book {
        val book = Book()
        book.category = category
        book.title = title
        book.cost = cost
        book.year = year
        book.description = description
        return book
    }

    private fun author(firstName: String, lastName: String, biodata: String, vararg books: Book) {
        val author = Author()
        author.firstName = firstName
        author.lastName = lastName
        author.bio = Bio()
        author.bio.biodata = biodata
        books.forEach { book ->
            author.books.add(book)
            book.authors.add(author)
        }
        authorRepository.save(author)
    }
}
