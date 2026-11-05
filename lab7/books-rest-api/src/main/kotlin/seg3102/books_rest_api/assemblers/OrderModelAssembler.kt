package seg3102.books_rest_api.assemblers

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component
import seg3102.books_rest_api.controller.ApiController
import seg3102.books_rest_api.entities.Order
import seg3102.books_rest_api.representation.OrderRepresentation

@Component
class OrderModelAssembler : RepresentationModelAssemblerSupport<Order,
        OrderRepresentation>(ApiController::class.java, OrderRepresentation::class.java) {

    override fun toModel(entity: Order): OrderRepresentation {
        val orderRepresentation = instantiateModel(entity)
        orderRepresentation.id = entity.id
        orderRepresentation.quantity = entity.quantity
        return orderRepresentation
    }

    // An order is reached through its book: /books/{id}/orders/{orderId}
    fun toModel(bookId: Long, entity: Order): OrderRepresentation {
        val orderRepresentation = toModel(entity)
        orderRepresentation.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ApiController::class.java)
                        .getBookOrderById(bookId, entity.id))
                .withSelfRel())
        orderRepresentation.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ApiController::class.java)
                        .getBookById(bookId))
                .withRel("book"))
        return orderRepresentation
    }

    fun toOrdersModel(bookId: Long, orders: List<Order>): CollectionModel<OrderRepresentation> {
        return CollectionModel.of(orders.map { toModel(bookId, it) },
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(ApiController::class.java)
                                .getBookOrdersById(bookId))
                        .withSelfRel())
    }
}
