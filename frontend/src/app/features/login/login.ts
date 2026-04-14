import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { AuthService, AuthResponse } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  isSignUp = signal(false);
  username = '';
  password = '';
  mobile = '';
  error = signal('');
  loading = signal(false);

  private baseUrl: string;

  constructor(
    private auth: AuthService,
    private http: HttpClient,
    private router: Router
  ) {
    if (auth.isLoggedIn()) {
      this.router.navigate(['/']);
    }
    if (typeof window !== 'undefined' && window.location.hostname !== 'localhost') {
      this.baseUrl = 'https://bansiwala-bhojnalay.onrender.com/api/auth';
    } else {
      this.baseUrl = 'http://localhost:8080/api/auth';
    }
  }

  toggleMode(): void {
    this.isSignUp.set(!this.isSignUp());
    this.error.set('');
  }

  onSubmit(): void {
    this.error.set('');
    if (!this.username.trim() || !this.password.trim()) {
      this.error.set('Enter username and password');
      return;
    }
    this.loading.set(true);

    if (this.isSignUp()) {
      this.doSignUp();
    } else {
      this.doLogin();
    }
  }

  private doLogin(): void {
    this.auth.login(this.username, this.password).subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(err.error?.message || 'Login failed');
      },
    });
  }

  private doSignUp(): void {
    if (!this.mobile.trim()) {
      this.error.set('Enter your registered mobile number');
      this.loading.set(false);
      return;
    }

    this.http
      .post<AuthResponse>(`${this.baseUrl}/signup`, {
        username: this.username,
        password: this.password,
        mobile: this.mobile,
      })
      .subscribe({
        next: (res) => {
          this.loading.set(false);
          localStorage.setItem('token', res.token);
          localStorage.setItem('username', res.username);
          localStorage.setItem('role', res.role);
          if (res.studentId) localStorage.setItem('studentId', String(res.studentId));
          window.location.href = '/';
        },
        error: (err) => {
          this.loading.set(false);
          this.error.set(err.error?.message || 'Sign up failed');
        },
      });
  }
}
