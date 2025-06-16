import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-document-form',
  templateUrl: './document-form.component.html',
  styleUrls: ['./document-form.component.scss']
})
export class DocumentFormComponent {
  form: FormGroup;
  fileToUpload: File | null = null;
  isSubmitting = false;

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.form = this.fb.group({
      name: ['', Validators.required],
      description: [''],
    });
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.fileToUpload = input.files[0];
    }
  }

  onSubmit() {
    if (this.form.invalid || !this.fileToUpload) return;

    const formData = new FormData();
    formData.append('file', this.fileToUpload);
    formData.append('name', this.form.value.name);
    formData.append('description', this.form.value.description);

    this.isSubmitting = true;
    this.http.post('/documents/uploadDoc', formData, { withCredentials: true }).subscribe({
      next: () => {
        this.form.reset();
        this.fileToUpload = null;
        this.isSubmitting = false;
        alert('Документ успешно создан!');
      },
      error: (err) => {
        this.isSubmitting = false;
        alert('Ошибка: ' + err.message);
      }
    });
  }
}
