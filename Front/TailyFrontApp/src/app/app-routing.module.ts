import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { IsAuthGuard } from './guards/auth.guard';
import { AuthComponent } from './components/auth-component/auth-component.component';
import { DocumentListComponent } from './components/document-list/document-list.component';
import { DocumentDashboardComponent } from './document-dashboard/document-dashboard.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './auth.interceptor';

const routes: Routes = [
  { path: '', redirectTo: 'auth', pathMatch: 'full' },
  { path: 'auth', component: AuthComponent },
  { path: 'EDO', component: DocumentDashboardComponent, canActivate: [IsAuthGuard] },
  { path: '**', redirectTo: 'auth' }, // fallback на всякий случай
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
   providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ]
})
export class AppRoutingModule { }
