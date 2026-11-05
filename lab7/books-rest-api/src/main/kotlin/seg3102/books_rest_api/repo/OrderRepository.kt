package seg3102.books_rest_api.repo

import org.springframework.data.repository.CrudRepository
import seg3102.books_rest_api.entities.Order

interface OrderRepository : CrudRepository<Order, Long>