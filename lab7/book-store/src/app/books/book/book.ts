import { Component, inject, OnDestroy, OnInit, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { BookEntity } from '../model/book-entity';
import { BooksService } from '../service/books-service';
import { AuthornamesPipe } from '../../pipes/authornames-pipe';

@Component({
  selector: 'app-book',
  templateUrl: './book.html',
  styleUrl: './book.css',
  imports: [AuthornamesPipe],
})
export class Book implements OnInit, OnDestroy {
  // undefined: request in progress, null: not found
  selectedBook = signal<BookEntity | null | undefined>(undefined);
  private subscription?: Subscription;
  private route: ActivatedRoute = inject(ActivatedRoute);
  private booksService: BooksService = inject(BooksService);

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      const id = params['id'];
      this.subscription?.unsubscribe();
      this.selectedBook.set(undefined);
      this.subscription = this.booksService.getBook(id).subscribe({
        next: (data: BookEntity) => this.selectedBook.set(data),
        error: () => this.selectedBook.set(null),
      });
    });
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }
}
