import { Component, inject, OnDestroy, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { AuthorDetails } from '../model/author-details';
import { AuthorsService } from '../service/authors-service';

type Status = 'loading' | 'found' | 'notFound' | 'error';

@Component({
  selector: 'app-author',
  templateUrl: './author.html',
  styleUrl: './author.css',
  imports: [RouterLink],
})
export class Author implements OnInit, OnDestroy {
  status = signal<Status>('loading');
  authorId = signal('');
  author = signal<AuthorDetails | null>(null);
  errorMessage = signal('');

  private route: ActivatedRoute = inject(ActivatedRoute);
  private authorsService: AuthorsService = inject(AuthorsService);
  private paramsSubscription?: Subscription;
  private subscription?: Subscription;

  ngOnInit(): void {
    // The component is reused when a second id is searched, so react to every change of the parameter
    this.paramsSubscription = this.route.params.subscribe((params) => this.load(params['id']));
  }

  private load(id: string): void {
    this.subscription?.unsubscribe();
    this.authorId.set(id);
    this.author.set(null);
    this.status.set('loading');
    this.subscription = this.authorsService.getAuthorDetails(id).subscribe({
      next: (details) => {
        this.author.set(details);
        this.status.set('found');
      },
      error: (err: HttpErrorResponse) => {
        if (err.status === 404 || err.status === 400) {
          this.status.set('notFound');
        } else {
          this.errorMessage.set(
            err.status === 0
              ? 'The Books API could not be reached. Make sure the server is running on http://localhost:8080.'
              : `The Books API returned an error (HTTP ${err.status}).`,
          );
          this.status.set('error');
        }
      },
    });
  }

  ngOnDestroy(): void {
    this.paramsSubscription?.unsubscribe();
    this.subscription?.unsubscribe();
  }
}
