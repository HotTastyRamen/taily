import { Component, Input } from '@angular/core';
import { SignatureDto } from 'src/app/DTOs/SignatureDto';
import { DocumentService } from 'src/app/services/document.service';
import { SignatureService } from 'src/app/services/signature.service';

@Component({
  selector: 'app-signature-card',
  templateUrl: './signature-card.component.html',
  styleUrls: ['./signature-card.component.scss']
})
export class SignatureCardComponent {
  @Input() signature!: SignatureDto;

  showVerificationField = false;
  smsCode: string = '';
  downloadUrl: string | null = null;
  message: string = '';

  constructor(
    private documentService: DocumentService,
    private signatureService: SignatureService
  ) {}

  downloadLatestVersion() {
    this.documentService.getLatestVersion(this.signature.documentId).subscribe({
      next: (version) => {
        this.documentService.getDownloadUrl(version.filePath).subscribe(url => {
          this.downloadUrl = url;
          window.open(this.downloadUrl, '_blank');
        });
      }
    });
  }

  sendVerificationCode() {
    // this.signatureService.verifySignatureCode(this.smsCode, this.signature.userId).subscribe({
    //   next: (token) => {
    //     this.signatureService.confirmSignature(token).subscribe(() => {
    //       this.message = 'Подпись подтверждена';
    //     });
    //   },
    //   error: () => {
    //     this.message = 'Неверный код';
    //   }
    // });
  }

  activateCodeInput() {
    this.showVerificationField = true;
  }
}
