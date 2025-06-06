import { Component } from '@angular/core';
import { AuthService } from './services/auth-service.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'TailyFrontApp';
    username = '123';
    password = '123';
    errorMessage = '123';

  constructor() {}

  // login() {
  //   this.authService.login(this.username, this.password).subscribe({
  //     next: () => {},
  //     error: () => {
  //       this.errorMessage = 'Invalid credentials';
  //     }
  //   });
  // }
}
