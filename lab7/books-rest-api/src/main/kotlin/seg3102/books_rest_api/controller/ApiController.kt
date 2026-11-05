package seg3102.books_rest_api.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.repository.findByIdOrNull
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import seg3102.books_rest_api.assemblers.AuthorModelAssembler
import seg3102.books_rest_api.assemblers.BioModelAssembler
import seg3102.books_rest_api.assemblers.BookModelAssembler
import seg3102.books_rest_api.assemblers.OrderModelAssembler
import seg3102.books_rest_api.entities.Author
import seg3102.books_rest_api.entities.Bio
import seg3102.books_rest_api.entities.Book
import seg3102.books_rest_api.entities.Order
import seg3102.books_rest_api.repo.AuthorRepository
import seg3102.books_rest_api.repo.BioRepository
import seg3102.books_rest_api.repo.BookRepository
import seg3102.books_rest_api.repo.OrderRepository
import seg3102.books_rest_api.representation.AuthorRepresentation
import seg3102.books_rest_api.representation.BioRepresentation
import seg3102.books_rest_api.representation.BookRepresentation
import seg3102.books_rest_api.representation.BookTitleRepresentation
import seg3102.books_rest_api.representation.OrderRepresentation
import java.net.URI

@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("books-api", produces = ["application/hal+json"])
class ApiController(val authorRepository: AuthorRepository,
                    val bookRepository: BookRepository,
                    val bioRepository: BioRepository,
                    val orderRepository: OrderRepository,
                    val authorAssembler: AuthorModelAssembler,
                    val bookAssembler: BookModelAssembler,
                    val orderAssembler: OrderModelAssembler,
                    val bioAssembler: BioModelAssembler) {

    // ------------------------------------------------------------------ Books

    @Operation(summary = "Get all books")
    @GetMapping("/books")
    fun allBooks(): ResponseEntity<CollectionModel<BookRepresentation>> {
        val books = bookRepository.findAll()
        return ResponseEntity(
                bookAssembler.toCollectionModel(books),
                HttpStatus.OK)
    }

    @Operation(summary = "Get a book by id")
    @GetMapping("/books/{id}")
    fun getBookById(@PathVariable("id") id: Long): ResponseEntity<BookRepresentation> {
        return bookRepository.findById(id)
                .map { entity: Book -> bookAssembler.toModel(entity) }
                .map { body: BookRepresentation -> ResponseEntity.ok(body) }
                .orElse(ResponseEntity.notFound().build())
    }

    @Operation(summary = "Add a new book")
    @PostMapping("/books")
    fun addBook(@RequestBody book: Book): ResponseEntity<Any> {
        return try {
            val newBook = this.bookRepository.save(book)
            val location: URI = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newBook.id)
                    .toUri()
            ResponseEntity.created(location).body(bookAssembler.toModel(newBook))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @Operation(summary = "Update the information of a book")
    @PutMapping("/books/{id}")
    fun updateBook(@PathVariable("id") id: Long, @RequestBody book: Book): ResponseEntity<Any> {
        return try {
            val currBook = bookRepository.findById(id).get()
            currBook.title = book.title
            currBook.isbn = book.isbn
            currBook.cost = book.cost
            currBook.category = book.category
            currBook.description = book.description
            currBook.year = book.year
            bookRepository.save(currBook)
            ResponseEntity.noContent().build<Any>()
        } catch (e: NoSuchElementException) {
            ResponseEntity.badRequest().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @Operation(summary = "Remove a book")
    @DeleteMapping("/books/{id}")
    fun deleteBook(@PathVariable("id") id: Long): ResponseEntity<Any> {
        return try {
            // Author owns the many-to-many relation: detach the book from its authors first
            val book = bookRepository.findByIdOrNull(id)
            if (book != null) {
                book.authors.forEach { author ->
                    author.books.removeIf { it.id == id }
                    authorRepository.save(author)
                }
                book.authors.clear()
                bookRepository.delete(book)
            }
            ResponseEntity.noContent().build<Any>()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @Operation(summary = "Get the authors of a book")
    @GetMapping("/books/{id}/authors")
    fun getBookAuthorsById(@PathVariable("id") id: Long): ResponseEntity<CollectionModel<AuthorRepresentation>> {
        val book = bookRepository.findByIdOrNull(id) ?: return ResponseEntity.notFound().build()
        val authors = authorAssembler.toCollectionModel(book.authors)
        authors.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ApiController::class.java)
                        .getBookAuthorsById(id))
                .withSelfRel())
        return ResponseEntity.ok(authors)
    }

    @Operation(summary = "Add a new author to a book")
    @PostMapping("/books/{id}/authors")
    fun addBookAuthor(@PathVariable("id") id: Long, @RequestBody author: Author): ResponseEntity<Any> {
        val book = bookRepository.findByIdOrNull(id) ?: return ResponseEntity.notFound().build()
        return try {
            author.books.add(book)
            book.authors.add(author)
            val newAuthor = authorRepository.save(author)
            val location = WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(ApiController::class.java)
                            .getAuthorById(newAuthor.id))
                    .toUri()
            ResponseEntity.created(location).body(authorAssembler.toModel(newAuthor))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @Operation(summary = "Add an existing author to a book")
    @PatchMapping("/books/{bookId}/authors/{authorId}")
    fun updateBookAuthors(@PathVariable("bookId") bookId: Long,
                          @PathVariable("authorId") authorId: Long): ResponseEntity<Any> {
        val book = bookRepository.findByIdOrNull(bookId) ?: return ResponseEntity.notFound().build()
        val author = authorRepository.findByIdOrNull(authorId) ?: return ResponseEntity.notFound().build()
        if (author.books.none { it.id == bookId }) {
            author.books.add(book)
            book.authors.add(author)
            authorRepository.save(author)
        }
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Remove an author from a book")
    @DeleteMapping("/books/{bookId}/authors/{authorId}")
    fun removeBookAuthor(@PathVariable("bookId") bookId: Long,
                         @PathVariable("authorId") authorId: Long): ResponseEntity<Any> {
        val author = authorRepository.findByIdOrNull(authorId) ?: return ResponseEntity.notFound().build()
        author.books.removeIf { it.id == bookId }
        authorRepository.save(author)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Get the orders of a book")
    @GetMapping("/books/{id}/orders")
    fun getBookOrdersById(@PathVariable("id") id: Long): ResponseEntity<CollectionModel<OrderRepresentation>> {
        val book = bookRepository.findByIdOrNull(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(orderAssembler.toOrdersModel(id, book.orders))
    }

    @Operation(summary = "Get an order of a book")
    @GetMapping("/books/{bookId}/orders/{orderId}")
    fun getBookOrderById(@PathVariable("bookId") bookId: Long,
                         @PathVariable("orderId") orderId: Long): ResponseEntity<OrderRepresentation> {
        val book = bookRepository.findByIdOrNull(bookId) ?: return ResponseEntity.notFound().build()
        val order = book.orders.find { it.id == orderId } ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(orderAssembler.toModel(bookId, order))
    }

    @Operation(summary = "Add an order to a book")
    @PostMapping("/books/{id}/orders")
    fun addBookOrder(@PathVariable("id") id: Long, @RequestBody order: Order): ResponseEntity<Any> {
        val book = bookRepository.findByIdOrNull(id) ?: return ResponseEntity.notFound().build()
        return try {
            val newOrder = orderRepository.save(order)
            book.orders.add(newOrder)
            bookRepository.save(book)
            val location = WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(ApiController::class.java)
                            .getBookOrderById(id, newOrder.id))
                    .toUri()
            ResponseEntity.created(location).body(orderAssembler.toModel(id, newOrder))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @Operation(summary = "Remove an order of a book")
    @DeleteMapping("/books/{bookId}/orders/{orderId}")
    fun deleteBookOrder(@PathVariable("bookId") bookId: Long,
                        @PathVariable("orderId") orderId: Long): ResponseEntity<Any> {
        val book = bookRepository.findByIdOrNull(bookId) ?: return ResponseEntity.notFound().build()
        if (book.orders.removeIf { it.id == orderId }) {
            bookRepository.save(book)
            orderRepository.deleteById(orderId)
        }
        return ResponseEntity.noContent().build()
    }

    // ---------------------------------------------------------------- Authors

    @Operation(summary = "Get all authors")
    @GetMapping("/authors")
    fun allAuthors(): ResponseEntity<CollectionModel<AuthorRepresentation>> {
        val authors = authorAssembler.toCollectionModel(authorRepository.findAll())
        authors.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ApiController::class.java)
                        .allAuthors())
                .withSelfRel())
        return ResponseEntity.ok(authors)
    }

    @Operation(summary = "Get the authors having a given first name and last name")
    @GetMapping("/authors", params = ["firstName", "lastName"])
    fun getAuthorsByName(@RequestParam("firstName") firstName: String,
                         @RequestParam("lastName") lastName: String):
            ResponseEntity<CollectionModel<AuthorRepresentation>> {
        val authors = authorRepository.findAuthorsByName(firstName, lastName)
        return ResponseEntity.ok(authorAssembler.toCollectionModel(authors))
    }

    @Operation(summary = "Get an author by id")
    @GetMapping("/authors/{id}")
    fun getAuthorById(@PathVariable("id") id: Long): ResponseEntity<AuthorRepresentation> {
        return authorRepository.findById(id)
                .map { entity: Author -> authorAssembler.toModel(entity) }
                .map { body: AuthorRepresentation -> ResponseEntity.ok(body) }
                .orElse(ResponseEntity.notFound().build())
    }

    @Operation(summary = "Update the name of an author")
    @PutMapping("/authors/{id}")
    fun updateAuthor(@PathVariable("id") id: Long, @RequestBody author: Author): ResponseEntity<Any> {
        val currAuthor = authorRepository.findByIdOrNull(id) ?: return ResponseEntity.notFound().build()
        currAuthor.firstName = author.firstName
        currAuthor.lastName = author.lastName
        authorRepository.save(currAuthor)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Remove an author")
    @DeleteMapping("/authors/{id}")
    fun deleteAuthor(@PathVariable("id") id: Long): ResponseEntity<Any> {
        return try {
            authorRepository.deleteById(id)
            ResponseEntity.noContent().build<Any>()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @Operation(summary = "Get the books of an author")
    @GetMapping("/authors/{id}/books")
    fun getAuthorBooksById(@PathVariable("id") id: Long): ResponseEntity<CollectionModel<BookTitleRepresentation>> {
        val author = authorRepository.findByIdOrNull(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(authorAssembler.toBookTitlesModel(id, author.books))
    }

    @Operation(summary = "Get the bio of an author")
    @GetMapping("/authors/{id}/bio")
    fun getAuthorBioById(@PathVariable("id") id: Long): ResponseEntity<BioRepresentation> {
        val author = authorRepository.findByIdOrNull(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(bioAssembler.toModel(id, author.bio))
    }

    @Operation(summary = "Update the bio of an author")
    @PutMapping("/authors/{id}/bio")
    fun updateAuthorBio(@PathVariable("id") id: Long, @RequestBody bio: Bio): ResponseEntity<Any> {
        val author = authorRepository.findByIdOrNull(id) ?: return ResponseEntity.notFound().build()
        author.bio.biodata = bio.biodata
        bioRepository.save(author.bio)
        return ResponseEntity.noContent().build()
    }
}
