import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TempConverterComponent } from './temp-converter.component';

describe('TempConverterComponent', () => {
  let component: TempConverterComponent;
  let fixture: ComponentFixture<TempConverterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TempConverterComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TempConverterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
