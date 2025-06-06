import { ApplicationConfig, NgModule } from '@angular/core';
import { provideRouter, RouterModule, Routes } from '@angular/router';
import { IsAuthGuard } from './guards/auth.guard';
import { AuthComponent } from './components/auth-component/auth-component.component';
import { AppComponent } from './app.component';

const routes: Routes = [
  { path: 'auth', component: AuthComponent },
  { path: '', redirectTo: '/auth', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
