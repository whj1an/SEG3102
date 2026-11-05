import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-authors',
  templateUrl: './authors.html',
  styleUrl: './authors.css',
  imports: [ReactiveFormsModule, RouterOutlet],
})
export class Authors {
  private builder: FormBuilder = inject(FormBuilder);
  private router: Router = inject(Router);
  private route: ActivatedRoute = inject(ActivatedRoute);

  // An author id is a positive whole number
  searchForm = this.builder.group({
    id: ['', [Validators.required, Validators.pattern('^[1-9]\\d*$')]],
  });

  get id() {
    return this.searchForm.controls.id;
  }

  onSubmit(): void {
    if (this.searchForm.invalid) {
      this.searchForm.markAllAsTouched();
      return;
    }
    const id = String(this.id.value).trim();
    // Renders the Author component in the nested outlet: /authors/{id}
    this.router.navigate(['./', id], { relativeTo: this.route }).then(() => {});
  }
}
