import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Page } from '../DTOs/Page';
import { SignatureDto } from '../DTOs/SignatureDto';

@Injectable({
  providedIn: 'root'
})
export class SignatureService {

  verifySignatureCode(smsCode: string, userId: number) {
    throw new Error('Method not implemented.');
  }
  constructor(private http: HttpClient) {}

getSignaturesByDocumentId(
  documentId: number,
  page: number,
  size: number,
  sortBy: string = 'id',
  sortDir: 'ASC' | 'DESC' = 'ASC'
): Observable<Page<SignatureDto>> {
  const params = new HttpParams()
    .set('page', page)
    .set('size', size)
    .set('sort', `${sortBy},${sortDir}`);

  return this.http.get<Page<SignatureDto>>(`/signatures/document/${documentId}`, {
    params,
    withCredentials: true // ⬅️ Передаёт cookie (включая httpOnly JWT)
  });
}

getAllSignatures(
  page: number,
  size: number,
  sortBy: string = 'signedAt',
  sortDir: 'DESC' | 'ASC' = 'DESC'
): Observable<Page<SignatureDto>> {
  const params = new HttpParams()
    .set('page', page)
    .set('size', size)
    .set('sort', `${sortBy},${sortDir}`);

  return this.http.get<Page<SignatureDto>>(`/signatures/user`, {
    params,
    withCredentials: true // ⬅️ Передаёт cookie (включая httpOnly JWT)
  });
}
}
