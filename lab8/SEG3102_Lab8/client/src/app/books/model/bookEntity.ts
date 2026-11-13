export class BookEntity {
  constructor(
    public bookId: number,
    public bookNumber: number,
    public category: string,
    public title: string,
    public cost: number,
    public authors?: AuthorEntity[],
    public year?: string,
    public description?: string
  ) {}
}

export class AuthorEntity {
  constructor(
    public bookNumber: number,
    public firstName: string,
    public lastName: string
  ){}
}
