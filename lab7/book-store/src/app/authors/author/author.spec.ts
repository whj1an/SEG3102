import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { BehaviorSubject } from 'rxjs';
import { Author } from './author';

const Url = 'http://localhost:8080/books-api/';

describe('Author', () => {
  let fixture: ComponentFixture<Author>;
  let http: HttpTestingController;
  const params = new BehaviorSubject<{ id: string }>({ id: '3' });

  beforeEach(async () => {
    params.next({ id: '3' });
    await TestBed.configureTestingModule({
      imports: [Author],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: ActivatedRoute, useValue: { params } },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Author);
    http = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  it('displays the author information when found', async () => {
    http.expectOne(Url + 'authors/3').flush({
      id: 3,
      firstName: 'Koredo',
      lastName: 'Tablanga',
      _links: { self: { href: Url + 'authors/3' }, books: { href: Url + 'authors/3/books' } },
    });
    http.expectOne(Url + 'authors/3/books').flush({ _embedded: { books: [{ id: 1, title: 'SpringBoot JPA and REST' }] } });
    await fixture.whenStable();

    const text = (fixture.nativeElement as HTMLElement).textContent ?? '';
    expect(text).toContain('Koredo');
    expect(text).toContain('Tablanga');
    expect(text).toContain('SpringBoot JPA and REST');
    expect(text).toContain('No biography available.');
  });

  it('displays a message when the author does not exist', async () => {
    http.expectOne(Url + 'authors/3').flush(null, { status: 404, statusText: 'Not Found' });
    await fixture.whenStable();
    expect((fixture.nativeElement as HTMLElement).textContent).toContain('Sorry, no author was found with id 3');
  });

  it('displays a message when the server cannot be reached', async () => {
    http.expectOne(Url + 'authors/3').error(new ProgressEvent('error'), { status: 0 });
    await fixture.whenStable();
    expect((fixture.nativeElement as HTMLElement).textContent).toContain('could not be reached');
  });

  it('looks up the new id when another author is searched', async () => {
    http.expectOne(Url + 'authors/3').flush(null, { status: 404, statusText: 'Not Found' });
    params.next({ id: '7' });
    http.expectOne(Url + 'authors/7').flush({ id: 7, firstName: 'Jane', lastName: 'C', _links: { self: { href: '' } } });
    await fixture.whenStable();
    expect((fixture.nativeElement as HTMLElement).textContent).toContain('Jane');
  });
});
