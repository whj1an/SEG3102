import { Component, inject, signal } from '@angular/core';
import { AuthenticationService } from '../authentication-service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  styleUrl: './login.css',
  imports: [FormsModule],
})
export class Login {
  username = '';
  password = '';
  message = signal('');
  private loginService: AuthenticationService = inject(AuthenticationService);

  get isLoggedIn(): boolean {
    return this.loginService.isLoggedIn();
  }

  get loggedUser(): string {
    return this.loginService.getUser();
  }

  checkLogin(): boolean {
    this.message.set('');
    if (!this.loginService.login(this.username, this.password)) {
      this.message.set('Invalid Login');
      setTimeout(() => this.message.set(''), 2500);
      return false;
    }
    return true;
  }

  logout(): boolean {
    this.loginService.logout();
    return true;
  }
}
