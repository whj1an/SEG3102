package seg3102.books_rest_api.representation

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation

@Relation(collectionRelation = "authors")
@JsonInclude(JsonInclude.Include.NON_NULL)
class AuthorRepresentation : RepresentationModel<AuthorRepresentation>() {
    var id: Long = 0
    var firstName: String = ""
    var lastName: String = ""
}
