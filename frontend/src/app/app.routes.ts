import { Routes } from '@angular/router';
import { authGuard, adminGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./features/login/login').then(m => m.Login) },
  { path: '', loadComponent: () => import('./features/dashboard/dashboard').then(m => m.Dashboard), canActivate: [authGuard] },
  { path: 'check-in', loadComponent: () => import('./features/check-in/check-in').then(m => m.CheckIn), canActivate: [authGuard, adminGuard] },
  { path: 'students', loadComponent: () => import('./features/students/students').then(m => m.Students), canActivate: [authGuard, adminGuard] },
  { path: 'students/new', loadComponent: () => import('./features/student-form/student-form').then(m => m.StudentForm), canActivate: [authGuard, adminGuard] },
  { path: 'students/:id/edit', loadComponent: () => import('./features/student-form/student-form').then(m => m.StudentForm), canActivate: [authGuard, adminGuard] },
  { path: 'students/:id/report', loadComponent: () => import('./features/student-report/student-report').then(m => m.StudentReport), canActivate: [authGuard] },
  { path: 'my-report', loadComponent: () => import('./features/student-report/student-report').then(m => m.StudentReport), canActivate: [authGuard] },
  { path: '**', redirectTo: '' },
];
