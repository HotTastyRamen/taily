import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

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
  private userTestUrl = 'admin/user';

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
              this.http.get<{ username: string, userId: number }>('/users/myName', { withCredentials: true })
      .subscribe({
        next: res => {
          localStorage.setItem('userId', res.userId.toString());

          console.log('User ID stored:', res.userId);
        },
        error: () => {
          console.log('Error fetching user data');
        }
      });
        this.router.navigate(['/EDO']);
      })
    );
  }

  getVerToken(code: VerificationCode): Observable<VerJWT> {
    return this.http.post<VerJWT>(this.verUrl, code);
  }

  getUserTest(username: string): Observable<any> {
    return this.http.post<VerJWT>(this.userTestUrl, username);
  }

  logout(): void {
    this.http.post(`${this.authUrl}/logout`, {}, { withCredentials: true }).subscribe({
      next: () => this.router.navigate(['/auth']),
      error: () => this.router.navigate(['/auth']) // даже если logout не удался, просто перенаправляем
    });
  }

  isAuthenticated(): Observable<boolean> {
    return this.http.get<{ authenticated: boolean }>(`${this.authUrl}/check`).pipe(
      map(response => response.authenticated),
      catchError(() => of(false)) // В случае ошибки (например, 401) считаем, что не аутентифицирован
    );
  }
}
