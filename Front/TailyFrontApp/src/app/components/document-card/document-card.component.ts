import { Component, Input, OnInit } from '@angular/core';
import { DocumentDto } from 'src/app/DTOs/DocumentDto';
import { SignatureRequest } from 'src/app/DTOs/SignatureRequest';
import { DocumentService } from 'src/app/services/document.service';
import { AssignSignaturesDialogComponent } from '../assign-signatures-dialog/assign-signatures-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { DocumentVersionsDialogComponent } from '../document-versions-dialog/document-versions-dialog.component';

@Component({
  selector: 'app-document-card',
  templateUrl: './document-card.component.html',
  styleUrls: ['./document-card.component.scss']
})
export class DocumentCardComponent implements OnInit {
  @Input() document!: DocumentDto;
  selectedUserIds: number[] = [];


  constructor(
    private documentService: DocumentService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {

  }

  viewVersions(): void {
    this.dialog.open(DocumentVersionsDialogComponent, {
      width: '60vw',
      data: { documentId: this.document.id, documentTitle: this.document.title }
    });
  }

  openAssignDialog(): void {
    this.dialog.open(AssignSignaturesDialogComponent, {
      width: '500px',
      data: { documentId: this.document.id, documentTitle: this.document.title }
    });
  }
}
