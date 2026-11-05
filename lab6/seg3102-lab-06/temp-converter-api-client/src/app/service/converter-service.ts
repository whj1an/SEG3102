import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';

const baseUrl = 'http://localhost:8080/temperature-converter';

@Injectable({
  providedIn: 'root'
})
export class ConverterService {
  private httpClient: HttpClient = inject(HttpClient);

  public convertCelsius(value: string): Observable<number | any> {
    return this.httpClient.get(`${baseUrl}/celsius-fahrenheit/${value}`).pipe(
      catchError(this.handleError))
  }

  public convertFahrenheit(value: string): Observable<number | any> {
    return this.httpClient.get(`${baseUrl}/fahrenheit-celsius/${value}`).pipe(
      catchError(this.handleError))
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    return throwError(() =>
      `Backend returned code ${error.status}, body was: ${error.error}`);
  }
}
