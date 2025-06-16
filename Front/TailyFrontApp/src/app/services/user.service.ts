import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { Page } from '../DTOs/Page';
import { UserDto } from '../DTOs/UserDto';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {}

    getPagedUsers(
    page: number,
    size: number,
    sortBy: string,
    sortDir: 'ASC' | 'DESC'
  ): Observable<Page<UserDto>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDir', sortDir);

    return this.http.get<Page<UserDto>>('/users', { params });
  }
}
