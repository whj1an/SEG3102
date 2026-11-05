// HAL (hal+json) shapes returned by the Books REST API

export interface Link {
  href: string;
}

// GET /books-api/authors/{id}
export interface AuthorResource {
  id: number;
  firstName: string;
  lastName: string;
  _links: {
    self: Link;
    books?: Link;
    bio?: Link;
  };
}

// An entry of GET /books-api/authors/{id}/books
export interface BookTitle {
  id: number;
  title: string;
}

// GET /books-api/authors/{id}/bio
export interface BioResource {
  id: number;
  biodata: string;
}

// What the Author component displays
export interface AuthorDetails {
  id: number;
  firstName: string;
  lastName: string;
  bio: string;
  books: BookTitle[];
}
