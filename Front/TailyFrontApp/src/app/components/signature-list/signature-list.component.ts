import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { SignatureService } from '../../services/signature.service';
import { SignatureDto } from 'src/app/DTOs/SignatureDto';

@Component({
  selector: 'app-signature-list',
  templateUrl: './signature-list.component.html',
  styleUrls: ['./signature-list.component.scss']
})
export class SignatureListComponent implements OnInit {
  signatures: SignatureDto[] = [];

  totalElements = 0;
  page = 0;
  size = 5;

  loading = false;
  error = '';

  constructor(private signatureService: SignatureService) {}

  ngOnInit(): void {
    this.loadSignatures();
  }

  loadSignatures(): void {
    this.loading = true;
    this.signatureService.getAllSignatures(this.page, this.size).subscribe({
      next: (response) => {
        this.signatures = response.content;
        this.totalElements = response.totalElements;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Ошибка при загрузке подписей';
        this.loading = false;
      }
    });
  }

  onPageChange(event: PageEvent): void {
    this.page = event.pageIndex;
    this.size = event.pageSize;
    this.loadSignatures();
  }
}
