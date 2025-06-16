import { Component } from '@angular/core';

@Component({
  selector: 'app-document-dashboard',
  templateUrl: './document-dashboard.component.html',
  styleUrls: ['./document-dashboard.component.scss']
})
export class DocumentDashboardComponent {
  currentView: 'documents' | 'signatures' = 'documents';
  showForm = false;

  setView(view: 'documents' | 'signatures') {
    this.currentView = view;
  }

  toggleForm() {
    this.showForm = !this.showForm;
  }
}
