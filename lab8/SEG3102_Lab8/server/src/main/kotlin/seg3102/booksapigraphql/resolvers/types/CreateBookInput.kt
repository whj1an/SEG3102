package seg3102.booksapigraphql.resolvers.types

class CreateBookInput(
    val bookNumber: Int? = null,
    val description: String? = null,
    val year: String? = null,
    val cost: Float? = null,
    val title: String? = null,
    val category: String? = null
)
