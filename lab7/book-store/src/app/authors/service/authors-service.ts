import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { forkJoin, Observable, of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';
import { AuthorDetails, AuthorResource, BioResource, BookTitle } from '../model/author-details';

const Url = 'http://localhost:8080/books-api/';

@Injectable({
  providedIn: 'root',
})
export class AuthorsService {
  private http: HttpClient = inject(HttpClient);

  // GET /authors/{id}: fails with a 404 HttpErrorResponse when no author has this id
  public getAuthor(id: string): Observable<AuthorResource> {
    return this.http.get<AuthorResource>(Url + 'authors/' + id);
  }

  // Gets the author, then follows the "books" and "bio" hyperlinks of the
  // representation (HATEOAS) to complete the author's information.
  public getAuthorDetails(id: string): Observable<AuthorDetails> {
    return this.getAuthor(id).pipe(
      switchMap((author) =>
        forkJoin({
          books: this.getBookTitles(author._links.books?.href),
          bio: this.getBio(author._links.bio?.href),
        }).pipe(
          map(({ books, bio }) => ({
            id: author.id,
            firstName: author.firstName,
            lastName: author.lastName,
            books,
            bio,
          })),
        ),
      ),
    );
  }

  private getBookTitles(href: string | undefined): Observable<BookTitle[]> {
    if (!href) return of([]);
    return this.http.get<any>(href).pipe(
      map((response) => (response._embedded?.books ?? []) as BookTitle[]),
      catchError(() => of([])),
    );
  }

  private getBio(href: string | undefined): Observable<string> {
    if (!href) return of('');
    return this.http.get<BioResource>(href).pipe(
      map((bio) => bio.biodata ?? ''),
      catchError(() => of('')),
    );
  }
}
