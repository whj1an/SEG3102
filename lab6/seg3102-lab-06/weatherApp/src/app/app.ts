import {Component, inject, OnInit} from '@angular/core';
import {Weather} from './model/weather.model';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule
} from '@angular/forms';
import {OpenWeatherService} from './service/open-weather-service';
import {Country, CountrySelectComponent} from '@wlucha/ng-country-select';
import {DatePipe, DecimalPipe} from '@angular/common';

function parseResponse(response: any): Weather {
  return new Weather({mainCondition: response.weather[0].main,
      temperature: response.main.temp - 273.15,
      pressure: response.main.pressure,
      humidity: response.main.humidity,
      windspeed: response.wind.speed,
      city: response.name,
      country: response.sys.country
    }
  );
}

@Component({
  selector: 'app-root',
  imports: [ReactiveFormsModule, DecimalPipe, CountrySelectComponent, DatePipe],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App  implements OnInit {
  protected title = 'weather-app';
  weatherForm!: FormGroup;
  condition: Weather | null = null;
  message: string | null = null;
  currentDate!: number;
  private formBuilder: FormBuilder = inject(FormBuilder);
  private weatherService: OpenWeatherService = inject(OpenWeatherService);
  // @ts-ignore
  protected country: (FormControl<Country | null> & FormControl);

  ngOnInit(): void {
    this.weatherForm = this.formBuilder.group({
      country: [],
      city: []
    });
    this.country = this.weatherForm.get('country') as FormControl;
  }

  submit(): void {
    const selectedCountry: Country = this.weatherForm.get('country')!.value;
    const selectedCity: string = this.weatherForm.get('city')!.value;
    this.weatherService.getWeatherAtCity(selectedCity, selectedCountry.alpha2).subscribe({
      next: (response: any) => {
        this.message = null;
        this.currentDate = Date.now();
        this.condition = parseResponse(response);
      },
      error: (error: any) => {this.condition = null; this.message = error; }
    });
  }
}

