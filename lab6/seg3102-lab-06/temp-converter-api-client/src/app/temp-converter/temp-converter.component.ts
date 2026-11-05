import {Component, inject} from '@angular/core';
import { DecimalPipe } from '@angular/common';
import {ConverterService} from '../service/converter-service';

@Component({
  selector: 'app-temp-converter',
  templateUrl: './temp-converter.component.html',
  styleUrls: ['./temp-converter.component.css'],
  standalone: true,
  imports: [DecimalPipe]
})
export class TempConverterComponent {
  private readonly converterService = inject(ConverterService);
  celsiusValue = 0;
  fahrenheitValue = 0;

  convertCelsius(value: string): void {
    this.converterService.convertCelsius(value).subscribe({
      next: (response) => { this.fahrenheitValue = response; },
      error: (_error) => window.alert(`Error: Unable to convert ${value}`)
    });
  }

  convertFahrenheit(value: string): void {
    this.converterService.convertFahrenheit(value).subscribe({
      next: (response) => { this.celsiusValue = response; },
      error: (_error) => window.alert(`Error: Unable to convert ${value}`)
    });
  }
}
