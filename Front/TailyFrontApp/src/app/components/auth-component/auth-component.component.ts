import { AuthService } from 'src/app/services/auth-service.service';
import { Component } from '@angular/core';
import { trigger, transition, style, animate } from '@angular/animations';
import { VerificationCode } from 'src/app/DTOs/VerificationCode';

@Component({
  selector: 'app-auth-component',
  templateUrl: './auth-component.component.html',
  styleUrls: ['./auth-component.component.scss'],
    animations: [
    trigger('fadeIn', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(-10px)' }),
        animate('300ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ])
    ])
  ]
})
export class AuthComponent {
  username = '';
  password = '';
  errorMessage = '';
  loginSuccess = false;
  smsCode = '';
  userId: number | null = null;
  verToken: string | null = null;

  constructor(private authService: AuthService) {}

  login() {
    this.authService.login(this.username, this.password).subscribe({
      next: () => { this.loginSuccess = true; },
      error: () => {
        this.errorMessage = 'Invalid credentials';
      }
    });
  }

  auth(){
    this.authService.auth(this.username, this.password).subscribe({
      next: (response) => {
        this.userId = response.userId;
        this.loginSuccess = true;
      },
      error: () => {
        this.errorMessage = 'Неверные логин или пароль';
      }
    });
  }

  verificate(){
    const verificationCode: VerificationCode = {
      userId: this.userId,
      code: this.smsCode
    };

    this.authService.getVerToken(verificationCode).subscribe({
      next: (response) => {
        console.log('Verification token:', response.verJwt);
        this.authService.setVerJWT(response.verJwt);
        this.login();
      },
      error: () => {
        this.errorMessage = 'Неверный код подтверждения';
      }
    });
    }
  }

