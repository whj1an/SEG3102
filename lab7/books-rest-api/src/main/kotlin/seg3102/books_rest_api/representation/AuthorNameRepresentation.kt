package seg3102.books_rest_api.representation

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation

// Subset of an author (name only) embedded in a book representation
@Relation(collectionRelation = "authors")
@JsonInclude(JsonInclude.Include.NON_NULL)
class AuthorNameRepresentation : RepresentationModel<AuthorNameRepresentation>() {
    var firstName: String = ""
    var lastName: String = ""
}
