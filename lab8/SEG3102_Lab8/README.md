# SEG3102 Lab 8

Haojian  Wang 300411829, github username: whj1an



| Folder | Branch | Description |
|---|---|---|
| `server/` | `server` | Spring Boot (Kotlin) GraphQL API backed by MongoDB (Spring for GraphQL, Spring Data MongoDB) |
| `client/` | `client` | Angular Book Store (from Lab 4) using Apollo Angular to call the GraphQL API (provided, unchanged) |



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
