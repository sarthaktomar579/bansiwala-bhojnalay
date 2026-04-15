import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/services/api.service';
import { AuthService } from '../../core/services/auth.service';
import { CheckInResponse } from '../../core/models/meal.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-scan-checkin',
  imports: [CommonModule, FormsModule],
  templateUrl: './scan-checkin.html',
  styleUrl: './scan-checkin.scss',
})
export class ScanCheckin implements OnInit {
  result = signal<CheckInResponse | null>(null);
  error = signal<string>('');
  processing = signal(false);
  thaliCount = 1;
  studentName = '';

  constructor(
    private api: ApiService,
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (!this.auth.isStudent() || !this.auth.studentId()) {
      this.router.navigate(['/']);
      return;
    }
    this.studentName = this.auth.username();
  }

  checkIn(): void {
    const sid = this.auth.studentId();
    if (!sid) return;

    this.processing.set(true);
    this.result.set(null);
    this.error.set('');

    this.api.checkInByScan(sid, this.thaliCount).subscribe({
      next: (res) => {
        this.result.set(res);
        this.processing.set(false);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Check-in failed');
        this.processing.set(false);
      },
    });
  }

  reset(): void {
    this.result.set(null);
    this.error.set('');
    this.thaliCount = 1;
  }
}
