# SEG3102 Lab 8 – GraphQL Web Services

Haojian (Jace) Wang 300411829, github username: whj1an

This folder contains the two branches of the starter repository
`https://github.com/SEG3102-Fall2026/seg3102-lab-08`:

| Folder | Branch | Description |
|---|---|---|
| `server/` | `server` | Spring Boot (Kotlin) GraphQL API backed by MongoDB (Spring for GraphQL, Spring Data MongoDB) |
| `client/` | `client` | Angular Book Store (from Lab 4) using Apollo Angular to call the GraphQL API (provided, unchanged) |

## Exercise

The GraphQL server was completed so that it works with the provided Angular client: the
GraphQL schemas and the resolvers were added.

Files written for the exercise (in `server/src/main/`):

- `resources/graphql/book.graphqls` – `Query` (`books`, `bookById`, `bookByNumber`),
  type `Book`, input `CreateBookInput`, `Mutation` (`newBook`, `deleteBook`, `updateBook`)
- `resources/graphql/author.graphqls` – extends `Query` with `authors(bookNumber)`,
  type `Author`, input `CreateAuthorInput`, extends `Mutation` with `newAuthor`
- `kotlin/seg3102/booksapigraphql/resolvers/BooksController.kt` – book queries and mutations,
  and the `Book.authors` field (authors are found by `bookNumber`)
- `kotlin/seg3102/booksapigraphql/resolvers/AuthorsController.kt` – `authors` query and
  `newAuthor` mutation

The client uses `bookByNumber` (with `authors`), `newBook` and `newAuthor`. The other
operations of the schema are implemented as well.

Validation rules:

- `newBook` requires `bookNumber`, `category`, `title` and `cost`, and rejects a book number
  that is already used (the book number links a book to its authors).
- `newAuthor` requires a first and last name and an existing book number.
- `deleteBook` also deletes the book's authors, and returns `false` if the id is unknown.
- `updateBook` only changes the fields that are given; if the book number changes, the
  book's authors are moved to the new number.

Errors are reported by the provided `ExceptionHandler` (message `"Error"`).

## Running

1. Start MongoDB:

   ```bash
   docker run -d -p 27017:27017 --name mongodb mongo
   ```

2. Start the server (port 9000, GraphiQL at http://localhost:9000/graphiql):

   ```bash
   cd server && ./gradlew bootRun
   ```

3. Start the client (http://localhost:4200):

   ```bash
   cd client && npm install && npm start
   ```

   Log in with `admin` / `password`, add a book with authors in **Admin**, then look it up in
   **Books** by its number.

## Example GraphiQL requests

```graphql
mutation {
  newBook(createBookInput: {bookNumber: 1020, category: "Kids", title: "Tales of times",
                            cost: 100, year: "2010", description: "Great read for Kids"}) {
    bookId
  }
}

mutation {
  newAuthor(createAuthorInput: {bookNumber: 1020, firstName: "Xala", lastName: "Zandra"}) {
    firstName
  }
}

{
  books {
    bookNumber
    title
    authors { lastName }
  }
}
```
