import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { AuthorsService } from './authors-service';
import { AuthorDetails } from '../model/author-details';

const Url = 'http://localhost:8080/books-api/';

describe('AuthorsService', () => {
  let service: AuthorsService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    service = TestBed.inject(AuthorsService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('follows the books and bio links of the author', () => {
    let result: AuthorDetails | undefined;
    service.getAuthorDetails('3').subscribe((details) => (result = details));

    http.expectOne(Url + 'authors/3').flush({
      id: 3,
      firstName: 'Koredo',
      lastName: 'Tablanga',
      _links: {
        self: { href: Url + 'authors/3' },
        books: { href: Url + 'authors/3/books' },
        bio: { href: Url + 'authors/3/bio' },
      },
    });
    http.expectOne(Url + 'authors/3/books').flush({
      _embedded: { books: [{ id: 1, title: 'SpringBoot JPA and REST' }] },
    });
    http.expectOne(Url + 'authors/3/bio').flush({ id: 4, biodata: 'Writes about Spring.' });

    expect(result).toEqual({
      id: 3,
      firstName: 'Koredo',
      lastName: 'Tablanga',
      bio: 'Writes about Spring.',
      books: [{ id: 1, title: 'SpringBoot JPA and REST' }],
    });
  });

  it('reports a 404 when no author has the id', () => {
    let status = 0;
    service.getAuthorDetails('99').subscribe({ error: (err) => (status = err.status) });
    http.expectOne(Url + 'authors/99').flush(null, { status: 404, statusText: 'Not Found' });
    expect(status).toBe(404);
  });
});
