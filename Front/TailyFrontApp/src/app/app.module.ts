import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from  '@angular/common/http';

import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatPaginatorModule } from '@angular/material/paginator';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input'; // для текста "Items per page"
import { MatSelectModule } from '@angular/material/select'
import { MatDialogModule } from '@angular/material/dialog';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthComponent } from './components/auth-component/auth-component.component';
import { AuthService } from './services/auth-service.service';
import { DocumentCardComponent } from './components/document-card/document-card.component';
import { DocumentListComponent } from './components/document-list/document-list.component';
import { HeaderComponent } from './components/header/header.component';
import { DocumentDashboardComponent } from './document-dashboard/document-dashboard.component';
import { AssignSignaturesDialogComponent } from './components/assign-signatures-dialog/assign-signatures-dialog.component';
import { DocumentVersionsDialogComponent } from './components/document-versions-dialog/document-versions-dialog.component';
import { SignatureCardComponent } from './components/signature-card/signature-card.component';
import { SignatureListComponent } from './components/signature-list/signature-list.component';
import { DocumentFormComponent } from './components/document-form/document-form.component';
import {ReactiveFormsModule} from '@angular/forms';

@NgModule({
  declarations: [
    AppComponent,
    AuthComponent,
    DocumentCardComponent,
    DocumentListComponent,
    HeaderComponent,
    DocumentDashboardComponent,
    AssignSignaturesDialogComponent,
    DocumentVersionsDialogComponent,
    SignatureCardComponent,
    SignatureListComponent,
    DocumentFormComponent
  ],
  imports: [
    MatPaginatorModule,
    BrowserModule,
    ReactiveFormsModule,
    AppRoutingModule,
    FormsModule,
    CommonModule,
    HttpClientModule,
    MatCardModule,
    MatIconModule,
    MatChipsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatProgressSpinnerModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatInputModule,
    MatDialogModule,
    MatSelectModule
  ],
  providers: [AuthService],
  bootstrap: [AppComponent]
})
export class AppModule { }
