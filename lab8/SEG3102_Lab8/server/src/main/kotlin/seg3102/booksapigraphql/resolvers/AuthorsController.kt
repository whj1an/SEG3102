// seg3102-lab8 Haojian Wang, 300411829
package seg3102.booksapigraphql.resolvers

import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import seg3102.booksapigraphql.entity.Author
import seg3102.booksapigraphql.entity.Book
import seg3102.booksapigraphql.resolvers.types.CreateAuthorInput

@Controller
class AuthorsController(private val mongoOperations: MongoOperations) {

    /**
     * Resolves the `authors` query: returns the authors of the book
     * with the given book number.
     */
    @QueryMapping
    fun authors(@Argument bookNumber: Int): List<Author> {
        return mongoOperations.find(byBookNumber(bookNumber), Author::class.java)
    }

    /**
     * Resolves the `newAuthor` mutation (used by the Angular Admin form):
     * stores a new author for a book and returns it.
     * The first and last names must not be blank, and a book with the
     * given book number must already exist.
     */
    @MutationMapping
    fun newAuthor(@Argument("createAuthorInput") input: CreateAuthorInput): Author {
        if (input.firstName.isBlank() || input.lastName.isBlank()) {
            throw Exception("Invalid input")
        }
        // an author can only be attached to an existing book
        if (!mongoOperations.exists(byBookNumber(input.bookNumber), Book::class.java)) {
            throw Exception("No book with number ${input.bookNumber}")
        }
        val author = Author(input.bookNumber, input.firstName, input.lastName)
        return mongoOperations.insert(author)
    }

    /**
     * Builds a MongoDB query that matches documents with the given book number.
     */
    private fun byBookNumber(bookNumber: Int): Query {
        val query = Query()
        query.addCriteria(Criteria.where("bookNumber").`is`(bookNumber))
        return query
    }
}
