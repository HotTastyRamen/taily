import { ChangeDetectorRef, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { SignatureDto } from 'src/app/DTOs/SignatureDto';
import { DocumentService } from 'src/app/services/document.service';
import { SignatureService } from 'src/app/services/signature.service';

@Component({
  selector: 'app-signature-card',
  templateUrl: './signature-card.component.html',
  styleUrls: ['./signature-card.component.scss']
})
export class SignatureCardComponent implements OnChanges {
  @Input() signature!: SignatureDto;

  localSignature!: SignatureDto;

  showVerificationField = false;
  smsCode: string = '';
  downloadUrl: string | null = null;
  message: string = '';

  constructor(
    private documentService: DocumentService,
    private signatureService: SignatureService,
    private cdRef: ChangeDetectorRef
  ) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['signature'] && this.signature) {
      this.localSignature = { ...this.signature };
    }
  }

  downloadLatestVersion() {
    this.documentService.getLatestVersion(this.localSignature.documentId).subscribe({
      next: (version) => {
        this.documentService.getDownloadUrl(version.filePath).subscribe(url => {
          this.downloadUrl = url;
          window.open(this.downloadUrl, '_blank');
        });
      }
    });
  }

  sendVerificationCode(): void {
    this.signatureService.confirmSignature(this.localSignature.id).subscribe({
      next: () => {
        this.message = 'Код отправлен на ваш номер телефона';
      },
      error: (err) => {
        this.message = 'Ошибка при отправке кода: ' + (err.error?.message || err.message);
      }
    });
  }

  activateCodeInput() {
    this.showVerificationField = true;
    this.sendVerificationCode();
  }

  verificateCode() {
    if (!this.smsCode) {
      this.message = 'Пожалуйста, введите код подтверждения';
      return;
    }

    this.signatureService.verifySignature(this.localSignature.id, this.smsCode).subscribe({
      next: () => {
        this.message = 'Подпись успешно подтверждена';
        this.cancelVerification();
        this.updateSignature();
      },
      error: (err) => {
        this.message = 'Ошибка при подтверждении подписи: ' + (err.error?.message || err.message);
      }
    });
  }

  updateSignature(): void {
    if (!this.localSignature?.id) {
      this.message = 'Некорректный ID подписи';
      return;
    }

    this.signatureService.getSignatureById(this.localSignature.id).subscribe({
      next: (updatedSignature) => {
        this.localSignature = updatedSignature;
        this.cdRef.detectChanges();
      },
      error: (err) => {
        this.message = 'Ошибка при обновлении подписи: ' + (err.error?.message || err.message);
      }
    });
  }

  cancelVerification(): void {
    this.showVerificationField = false;
    this.smsCode = '';
    this.message = '';
  }
}
