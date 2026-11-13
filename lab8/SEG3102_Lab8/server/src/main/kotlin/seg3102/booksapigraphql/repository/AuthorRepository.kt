package seg3102.booksapigraphql.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import seg3102.booksapigraphql.entity.Author

@Repository
interface AuthorRepository: MongoRepository<Author, Int>
