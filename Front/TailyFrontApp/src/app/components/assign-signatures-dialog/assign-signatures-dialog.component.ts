import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DocumentService } from '../../services/document.service';
import { SignatureRequest } from '../../DTOs/SignatureRequest';
import { SignatureDto } from 'src/app/DTOs/SignatureDto';
import { PageEvent } from '@angular/material/paginator';
import { SignatureService } from 'src/app/services/signature.service';
import { UserService } from 'src/app/services/user.service';
import { UserDto } from 'src/app/DTOs/UserDto';

interface DialogData {
  documentId: number;
}

@Component({
  selector: 'app-assign-signatures-dialog',
  templateUrl: './assign-signatures-dialog.component.html',
  styleUrls: ['./assign-signatures-dialog.component.scss']
})
export class AssignSignaturesDialogComponent implements OnInit {
  availableUsers: UserDto[] = [];
  selectedUserIds: number[] = [];

  userIdToNameMap = new Map<number, string>();

  existingPagedSignatures: SignatureDto[] = [];
  existingSignatures: SignatureDto[] = [];
  signaturesTotal = 0;
  signaturesPageIndex = 0;
  signaturesPageSize = 5;
  documentId: number = this.data.documentId;
  totalElements: number = 0;

  constructor(
    private dialogRef: MatDialogRef<AssignSignaturesDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private documentService: DocumentService,
    private signatureService: SignatureService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.fetchUsers();

  }

  isUserAlreadyAssigned(userId: number): boolean {
    return this.existingSignatures.some(s => s.userId === userId);
  }

  fetchUsers(): void {
    const sortBy = 'id';
    const sortDir: 'ASC' | 'DESC' = 'ASC';

    this.userService.getPagedUsers(
      0,
      1000,
      sortBy,
      sortDir)
      .subscribe((response) => {
        this.availableUsers = response.content;
        this.totalElements = response.totalElements;
        this.fetchExistingSignatures();
        this.fetchPagedExistingSignatures();
      });
  }

  fetchExistingSignatures(): void {
    this.signatureService
      .getSignaturesByDocumentId(this.documentId, 0, 1000, 'id', 'ASC')
      .subscribe((response) => {
        this.existingSignatures = response.content;
        this.signaturesTotal = response.totalElements;
      });
  }

  fetchPagedExistingSignatures(): void {
    this.signatureService
      .getSignaturesByDocumentId(
        this.documentId,
        this.signaturesPageIndex,
        this.signaturesPageSize,
        'id',
        'ASC'
      )
      .subscribe((response) => {
        this.existingPagedSignatures = response.content;
        this.signaturesTotal = response.totalElements;
        this.userIdToNameMap = new Map(
          this.availableUsers.map(user => [user.id, user.username])
        );
      });
  }

  toggleUser(userId: number, checked: boolean): void {
    if (checked) {
      this.selectedUserIds.push(userId);
    } else {
      this.selectedUserIds = this.selectedUserIds.filter(id => id !== userId);
    }
  }

  assignSignatures(): void {
    const request: SignatureRequest = {
      documentId: this.data.documentId,
      userIds: this.selectedUserIds,
    };

    this.documentService.assignSignatures(request).subscribe(() => {
      this.fetchPagedExistingSignatures(); // обновим список
      this.selectedUserIds = [];
    });
  }

  onSignaturesPageChange(event: PageEvent): void {
    this.signaturesPageIndex = event.pageIndex;
    this.signaturesPageSize = event.pageSize;
    this.fetchPagedExistingSignatures();
  }

  close(): void {
    this.dialogRef.close();
  }

  ngOnChanges(): void {
    // Обновление при изменении входных данных

  }
}

