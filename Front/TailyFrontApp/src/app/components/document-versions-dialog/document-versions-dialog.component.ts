import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DocumentService } from '../../services/document.service';
import { DocumentVersionDto } from '../../DTOs/DocumentVersionDto';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-document-versions-dialog',
  templateUrl: './document-versions-dialog.component.html',
  styleUrls: ['./document-versions-dialog.component.scss']
})
export class DocumentVersionsDialogComponent implements OnInit {
  documentId!: number;
  latestVersion!: DocumentVersionDto;
  versions: DocumentVersionDto[] = [];
  totalVersions = 0;
  pageSize = 5;
  pageIndex = 0;
  selectedFileName: string | undefined;

  constructor(
    private documentService: DocumentService,
    private dialogRef: MatDialogRef<DocumentVersionsDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { documentId: number }
  ) {
    this.documentId = data.documentId;
  }

  ngOnInit() {
    this.loadLatestVersion();
    this.loadVersions();
  }

  loadLatestVersion() {
    this.documentService.getLatestVersion(this.documentId).subscribe(v => {
      this.latestVersion = v;

      this.documentService.getDownloadUrl(v.filePath).subscribe(url => {
        this.latestVersion['downloadUrl'] = url;
      });
    });
  }

  loadVersions() {
    this.documentService.getAllVersions(
      this.documentId, this.pageIndex, this.pageSize
    ).subscribe(page => {
      this.versions = page.content;
      console.log('Loaded versions:', this.versions);
      // Получаем presigned URL для каждой версии
      this.versions.forEach(v => {
        console.log('Processing version:', v);
        console.log('File path:', v.filePath);
        this.documentService.getDownloadUrl(v.filePath).subscribe(url => {
          v['downloadUrl'] = url;
        });
      });

      this.totalVersions = page.totalElements;
    });
  }

  onFileSelected(event: Event) {
    const file = (event.target as HTMLInputElement).files?.[0];
    this.selectedFileName = file?.name || '';
    if (file) {
      this.documentService.uploadNewVersion(this.documentId, file).subscribe(() => {
        this.loadLatestVersion();
        this.loadVersions();
      });
    }
  }

  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadVersions();
  }

  close() {
    this.dialogRef.close();
  }
}

