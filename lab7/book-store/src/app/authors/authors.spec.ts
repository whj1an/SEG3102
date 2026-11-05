import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, Router } from '@angular/router';
import { Authors } from './authors';

describe('Authors', () => {
  let component: Authors;
  let fixture: ComponentFixture<Authors>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Authors],
      providers: [provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(Authors);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('does not navigate when the id is not a positive number', () => {
    const router = TestBed.inject(Router);
    const navigate = vi.spyOn(router, 'navigate');
    component.searchForm.setValue({ id: 'abc' });
    component.onSubmit();
    expect(navigate).not.toHaveBeenCalled();
  });

  it('navigates to the author route when a valid id is submitted', () => {
    const router = TestBed.inject(Router);
    const navigate = vi.spyOn(router, 'navigate').mockResolvedValue(true);
    component.searchForm.setValue({ id: '12' });
    component.onSubmit();
    expect(navigate).toHaveBeenCalledWith(['./', '12'], expect.anything());
  });
});
