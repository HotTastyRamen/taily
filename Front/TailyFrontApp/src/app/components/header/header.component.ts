import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],

})
export class HeaderComponent implements OnInit {
  username: string = '...';
  selectedSection: 'documents' | 'projects' = 'documents';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<{ username: string }>('/api/user', { withCredentials: true })
      .subscribe({
        next: res => this.username = res.username,
        error: () => this.username = 'Неизвестно'
      });
  }
}
