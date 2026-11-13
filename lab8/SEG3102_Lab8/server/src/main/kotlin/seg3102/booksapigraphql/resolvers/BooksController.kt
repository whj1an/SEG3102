// seg3102-lab8 Haojian Wang, 300411829
package seg3102.booksapigraphql.resolvers

import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import seg3102.booksapigraphql.entity.Author
import seg3102.booksapigraphql.entity.Book
import seg3102.booksapigraphql.repository.BookRepository
import seg3102.booksapigraphql.resolvers.types.CreateBookInput
import java.util.UUID

@Controller
class BooksController(private val bookRepository: BookRepository,
                      private val mongoOperations: MongoOperations
) {
    /**
     * Resolves the `books` query: returns every book in the data store.
     */
    @QueryMapping
    fun books(): List<Book> {
        return bookRepository.findAll()
    }

    /**
     * Resolves the `bookById` query: returns the book with the given id,
     * or null if there is no such book.
     */
    @QueryMapping
    fun bookById(@Argument bookId: String): Book? {
        return bookRepository.findById(bookId).orElse(null)
    }

    /**
     * Resolves the `bookByNumber` query (used by the Angular Book page):
     * returns the book with the given book number, or null if there is no such book.
     */
    @QueryMapping
    fun bookByNumber(@Argument bookNumber: Int): Book? {
        return findByNumber(bookNumber)
    }

    /**
     * Resolves the `authors` field of type `Book`: returns the authors stored
     * with the same book number as the book.
     */
    @SchemaMapping(typeName = "Book", field = "authors")
    fun authors(book: Book): List<Author> {
        return mongoOperations.find(byBookNumber(book.bookNumber), Author::class.java)
    }

    /**
     * Resolves the `newBook` mutation (used by the Angular Admin form):
     * creates a book with a new random id and returns it.
     * The book number, category, title and cost are required, and the book
     * number must not already be used by another book.
     */
    @MutationMapping
    fun newBook(@Argument("createBookInput") input: CreateBookInput): Book {
        if (input.bookNumber != null &&
            input.category != null &&
            input.title != null &&
            input.cost != null) {
            if (findByNumber(input.bookNumber) != null) {
                throw Exception("A book with number ${input.bookNumber} already exists")
            }
            val book = Book(input.bookNumber, input.category, input.title, input.cost,
                input.year, input.description)
            book.bookId = UUID.randomUUID().toString()
            bookRepository.save(book)
            return book
        } else {
            throw Exception("Invalid input")
        }
    }

    /**
     * Resolves the `deleteBook` mutation: deletes the book with the given id
     * together with its authors. Returns true if the book was deleted,
     * false if there is no book with this id.
     */
    @MutationMapping
    fun deleteBook(@Argument bookId: String): Boolean {
        val book = bookRepository.findById(bookId).orElse(null) ?: return false
        // remove the authors attached to the book as well
        mongoOperations.remove(byBookNumber(book.bookNumber), Author::class.java)
        bookRepository.delete(book)
        return true
    }

    /**
     * Resolves the `updateBook` mutation: changes only the fields given in the
     * input for the book with the given id, and returns the updated book.
     * If the book number changes, the new number must be free and the book's
     * authors are moved to the new number.
     */
    @MutationMapping
    fun updateBook(@Argument bookId: String,
                   @Argument("createBookInput") input: CreateBookInput): Book {
        val book = bookRepository.findById(bookId).orElseThrow { Exception("Book not found") }
        val oldNumber = book.bookNumber
        if (input.bookNumber != null && input.bookNumber != oldNumber) {
            if (findByNumber(input.bookNumber) != null) {
                throw Exception("A book with number ${input.bookNumber} already exists")
            }
            book.bookNumber = input.bookNumber
            // keep the authors attached to the book under its new number
            mongoOperations.updateMulti(byBookNumber(oldNumber),
                Update().set("bookNumber", input.bookNumber), Author::class.java)
        }
        input.category?.let { book.category = it }
        input.title?.let { book.title = it }
        input.cost?.let { book.cost = it }
        input.year?.let { book.year = it }
        input.description?.let { book.description = it }
        return bookRepository.save(book)
    }

    /**
     * Returns the book with the given book number, or null if there is none.
     */
    private fun findByNumber(bookNumber: Int): Book? {
        return mongoOperations.findOne(byBookNumber(bookNumber), Book::class.java)
    }

    /**
     * Builds a MongoDB query that matches documents with the given book number
     * (used for both the book and the author collections).
     */
    private fun byBookNumber(bookNumber: Int): Query {
        val query = Query()
        query.addCriteria(Criteria.where("bookNumber").`is`(bookNumber))
        return query
    }
}
