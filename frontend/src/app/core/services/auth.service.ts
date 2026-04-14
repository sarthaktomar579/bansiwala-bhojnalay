import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { ApiService } from './api.service';

export interface AuthResponse {
  token: string;
  username: string;
  role: string;
  studentId: number | null;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  isLoggedIn = signal(false);
  username = signal('');
  role = signal('');
  studentId = signal<number | null>(null);

  private baseUrl: string;

  constructor(private http: HttpClient, private router: Router) {
    if (typeof window !== 'undefined' && window.location.hostname !== 'localhost') {
      this.baseUrl = 'https://bansiwala-bhojnalay.onrender.com/api/auth';
    } else {
      this.baseUrl = 'http://localhost:8080/api/auth';
    }
    this.loadFromStorage();
  }

  private loadFromStorage(): void {
    if (typeof localStorage === 'undefined') return;
    const token = localStorage.getItem('token');
    if (token) {
      this.isLoggedIn.set(true);
      this.username.set(localStorage.getItem('username') || '');
      this.role.set(localStorage.getItem('role') || '');
      const sid = localStorage.getItem('studentId');
      this.studentId.set(sid ? +sid : null);
    }
  }

  login(username: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, { username, password })
      .pipe(tap(res => this.saveAuth(res)));
  }

  private saveAuth(res: AuthResponse): void {
    localStorage.setItem('token', res.token);
    localStorage.setItem('username', res.username);
    localStorage.setItem('role', res.role);
    if (res.studentId) localStorage.setItem('studentId', String(res.studentId));
    this.isLoggedIn.set(true);
    this.username.set(res.username);
    this.role.set(res.role);
    this.studentId.set(res.studentId);
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('role');
    localStorage.removeItem('studentId');
    this.isLoggedIn.set(false);
    this.username.set('');
    this.role.set('');
    this.studentId.set(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    if (typeof localStorage === 'undefined') return null;
    return localStorage.getItem('token');
  }

  isAdmin(): boolean {
    return this.role() === 'ADMIN';
  }

  isStudent(): boolean {
    return this.role() === 'STUDENT';
  }
}
