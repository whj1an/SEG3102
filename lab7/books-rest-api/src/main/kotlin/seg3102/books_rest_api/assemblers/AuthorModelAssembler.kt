package seg3102.books_rest_api.assemblers

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component
import seg3102.books_rest_api.controller.ApiController
import seg3102.books_rest_api.entities.Author
import seg3102.books_rest_api.entities.Book
import seg3102.books_rest_api.representation.AuthorRepresentation
import seg3102.books_rest_api.representation.BookTitleRepresentation

@Component
class AuthorModelAssembler : RepresentationModelAssemblerSupport<Author,
        AuthorRepresentation>(ApiController::class.java, AuthorRepresentation::class.java) {

    // An author with links to itself, to its books and to its bio
    override fun toModel(entity: Author): AuthorRepresentation {
        val authorRepresentation = instantiateModel(entity)
        authorRepresentation.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ApiController::class.java)
                        .getAuthorById(entity.id))
                .withSelfRel())
        authorRepresentation.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ApiController::class.java)
                        .getAuthorBooksById(entity.id))
                .withRel("books"))
        authorRepresentation.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ApiController::class.java)
                        .getAuthorBioById(entity.id))
                .withRel("bio"))
        authorRepresentation.id = entity.id
        authorRepresentation.firstName = entity.firstName
        authorRepresentation.lastName = entity.lastName
        return authorRepresentation
    }

    // The titles of the books of an author, each with a link to the book
    fun toBookTitlesModel(authorId: Long, books: List<Book>): CollectionModel<BookTitleRepresentation> {
        val titles = books.map { bookTitleRepresentation(it) }
        return CollectionModel.of(titles,
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(ApiController::class.java)
                                .getAuthorBooksById(authorId))
                        .withSelfRel())
    }

    private fun bookTitleRepresentation(book: Book): BookTitleRepresentation {
        val representation = BookTitleRepresentation()
        representation.id = book.id
        representation.title = book.title
        return representation.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ApiController::class.java)
                        .getBookById(book.id))
                .withSelfRel())
    }
}
