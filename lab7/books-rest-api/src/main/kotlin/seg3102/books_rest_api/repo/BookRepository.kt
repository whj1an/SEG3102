package seg3102.books_rest_api.repo

import org.springframework.data.repository.CrudRepository
import seg3102.books_rest_api.entities.Book

interface BookRepository: CrudRepository<Book, Long>