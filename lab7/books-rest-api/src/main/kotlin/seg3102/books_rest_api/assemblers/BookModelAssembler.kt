package seg3102.books_rest_api.assemblers

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component
import seg3102.books_rest_api.controller.ApiController
import seg3102.books_rest_api.entities.Author
import seg3102.books_rest_api.entities.Book
import seg3102.books_rest_api.representation.AuthorNameRepresentation
import seg3102.books_rest_api.representation.BookRepresentation
import java.util.*

@Component
class BookModelAssembler : RepresentationModelAssemblerSupport<Book,
        BookRepresentation>(ApiController::class.java, BookRepresentation::class.java) {
    override fun toModel(entity: Book): BookRepresentation {
        val bookRepresentation = instantiateModel(entity)
        bookRepresentation.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ApiController::class.java)
                        .getBookById(entity.id))
                .withSelfRel())
        bookRepresentation.authors = toAuthorsRepresentation(entity.authors)
        bookRepresentation.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ApiController::class.java)
                        .getBookOrdersById(entity.id))
                .withRel("orders"))
        bookRepresentation.id = entity.id
        bookRepresentation.isbn = entity.isbn
        bookRepresentation.category = entity.category
        bookRepresentation.title = entity.title
        bookRepresentation.cost = entity.cost
        bookRepresentation.year = entity.year
        bookRepresentation.description = entity.description
        return bookRepresentation
    }

    override fun toCollectionModel(entities: Iterable<Book>): CollectionModel<BookRepresentation> {
        val bookRepresentations = super.toCollectionModel(entities)
        bookRepresentations.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(
                        ApiController::class.java).allBooks()).withSelfRel())
        return bookRepresentations
    }

    fun toAuthorsRepresentation(authors: List<Author>): List<AuthorNameRepresentation> {
        return if (authors.isEmpty()) Collections.emptyList() else authors
                .map {
                    authorRepresentation(it)
                }
    }

    private fun authorRepresentation(author: Author): AuthorNameRepresentation {
        val representation = AuthorNameRepresentation()
        representation.firstName = author.firstName
        representation.lastName = author.lastName
        return representation.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ApiController::class.java)
                        .getAuthorById(author.id))
                .withSelfRel())
    }
}
