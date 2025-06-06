import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

import { JwtRequest } from '../DTOs/JwtRequest';
import { JwtResponse } from '../DTOs/JwtResponse';
import { AuthResponse } from '../DTOs/AuthResponse';
import { VerificationCode } from '../DTOs/VerificationCode';
import { VerJWT } from '../DTOs/VerJWT';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private loginUrl = 'loginCookie';
  private authUrl = 'auth';
  private verUrl = 'getVerToken';

  private verjwt = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ5MTIxODg1LCJleHAiOjE3NDkyMzkxMjB9.HNBReybwhI_Way5Oi0lGIrbW9NtaYCxUKJGDhrARYrA';

  constructor(private http: HttpClient, private router: Router) {}

  setVerJWT(jwt: string): void {
    this.verjwt = jwt;
  }
  getVerJWT(): string {
    return this.verjwt;
  }

  auth(username: string, password: string): Observable<AuthResponse> {
    const body = { username, password };

    return this.http.post<AuthResponse>(this.authUrl, body);
  }

  login(username: string, password: string): Observable<void> {
    const body: JwtRequest = { username, password };
    const headers = new HttpHeaders({
      authorization: 'Bearer ' + this.verjwt
    });
    return this.http.post<void>(this.loginUrl, body, {
      headers: headers,
      withCredentials: true // 🔑 отправит/примет cookie
    }).pipe(
      tap(() => {
        this.router.navigate(['/dashboard']);
      })
    );
  }

  getVerToken(code: VerificationCode): Observable<VerJWT> {
    return this.http.post<VerJWT>(this.verUrl, code);
  }

  logout(): void {
    localStorage.removeItem('jwt');
    this.router.navigate(['/auth']);
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('jwt');
  }
}
