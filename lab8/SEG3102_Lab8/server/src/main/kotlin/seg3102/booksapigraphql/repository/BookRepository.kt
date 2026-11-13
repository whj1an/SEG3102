package seg3102.booksapigraphql.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import seg3102.booksapigraphql.entity.Book

@Repository
interface BookRepository: MongoRepository<Book, String>
