package seg3102.books_rest_api.assemblers

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component
import seg3102.books_rest_api.controller.ApiController
import seg3102.books_rest_api.entities.Bio
import seg3102.books_rest_api.representation.BioRepresentation

@Component
class BioModelAssembler : RepresentationModelAssemblerSupport<Bio,
        BioRepresentation>(ApiController::class.java, BioRepresentation::class.java) {

    override fun toModel(entity: Bio): BioRepresentation {
        val bioRepresentation = instantiateModel(entity)
        bioRepresentation.id = entity.id
        bioRepresentation.biodata = entity.biodata
        return bioRepresentation
    }

    // A bio is reached through its author: /authors/{id}/bio
    fun toModel(authorId: Long, entity: Bio): BioRepresentation {
        val bioRepresentation = toModel(entity)
        bioRepresentation.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ApiController::class.java)
                        .getAuthorBioById(authorId))
                .withSelfRel())
        bioRepresentation.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ApiController::class.java)
                        .getAuthorById(authorId))
                .withRel("author"))
        return bioRepresentation
    }
}
