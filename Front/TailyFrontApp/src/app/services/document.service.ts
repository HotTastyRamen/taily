import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DocumentDto } from '../DTOs/DocumentDto'; // Предполагается, что этот DTO уже создан
import { Page } from '../DTOs/Page';
import { SignatureRequest } from '../DTOs/SignatureRequest';
import { DocumentVersionDto } from '../DTOs/DocumentVersionDto';

@Injectable({
  providedIn: 'root'
})
export class DocumentService {
  constructor(private http: HttpClient) {}

  getCreatedDocuments(
    title: string,
    page: number,
    size: number,
    sortBy: string,
    sortDir: 'ASC' | 'DESC'
  ): Observable<Page<DocumentDto>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDir', sortDir);

    if (title) {
      params = params.set('title', title);
    }

    return this.http.get<Page<DocumentDto>>('documents/UserDocs', {
      params,
      withCredentials: true
    });
  }

  assignSignatures(request: SignatureRequest): Observable<void> {
    return this.http.post<void>('/signatures/assign', request, {
      withCredentials: true
    });
  }

  getLatestVersion(documentId: number): Observable<DocumentVersionDto> {
    return this.http.get<DocumentVersionDto>(`/document-versions/document/${documentId}/latest`);
  }

  getAllVersions(documentId: number, page: number, size: number): Observable<Page<DocumentVersionDto>> {
    return this.http.get<Page<DocumentVersionDto>>(`/document-versions/document/${documentId}`, {
      params: { page, size }
    });
  }

  uploadNewVersion(documentId: number, file: File): Observable<string> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post(`/documents/${documentId}/upload-version`, formData, {
      responseType: 'text',
      withCredentials: true
    });
  }

  getDownloadUrl(objectKey: string): Observable<string> {
    return this.http.get(`/document-versions/getVersion`, {
      params: { objectKey },
      responseType: 'text', // потому что сервер возвращает строку (URL)
      withCredentials: true
    });
  }
}
