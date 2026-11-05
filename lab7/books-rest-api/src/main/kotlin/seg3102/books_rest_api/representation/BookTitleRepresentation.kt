package seg3102.books_rest_api.representation

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation

// Subset of a book (title only) used in the list of books of an author
@Relation(collectionRelation = "books")
@JsonInclude(JsonInclude.Include.NON_NULL)
class BookTitleRepresentation : RepresentationModel<BookTitleRepresentation>() {
    var id: Long = 0
    var title: String = ""
}
