import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],

})
export class HeaderComponent implements OnInit {
  username: string = '...';
  selectedSection: 'documents' | 'projects' = 'documents';

  constructor(private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.http.get<{ username: string, userId: number }>('/users/myName', { withCredentials: true })
      .subscribe({
        next: res => {
          this.username = res.username;
          localStorage.setItem('userId', res.userId.toString());

          console.log('Username:', this.username);
          console.log('User ID stored:', res.userId);
        },
        error: () => {
          this.username = 'Неизвестно';
          console.log('Error fetching user data');
        }
      });
  }

    logout() {
    this.http.post('/auth/logout', {}, { withCredentials: true }).subscribe({
      next: () => this.router.navigate(['/auth']),
      error: (err) => console.error('Logout failed', err)
    });
  }
}
