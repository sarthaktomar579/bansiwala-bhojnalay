import { Routes } from '@angular/router';
import { Dashboard } from './features/dashboard/dashboard';
import { CheckIn } from './features/check-in/check-in';
import { Students } from './features/students/students';
import { StudentForm } from './features/student-form/student-form';
import { StudentReport } from './features/student-report/student-report';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: Dashboard },
  { path: 'check-in', component: CheckIn },
  { path: 'students', component: Students },
  { path: 'students/new', component: StudentForm },
  { path: 'students/:id/edit', component: StudentForm },
  { path: 'students/:id/report', component: StudentReport },
];
