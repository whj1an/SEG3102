import { Component } from '@angular/core';
import {TempConverterComponent} from './temp-converter/temp-converter.component';

@Component({
  selector: 'app-root',
  imports: [
    TempConverterComponent
  ],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  title = 'temp-converter-api-client';
}
