import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../../core/services/api.service';
import { AuthService } from '../../core/services/auth.service';
import { DailyReport, CheckInResponse } from '../../core/models/meal.model';
import { Student } from '../../core/models/student.model';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard implements OnInit {
  report = signal<DailyReport | null>(null);
  recentCheckIns = signal<CheckInResponse[]>([]);
  paymentDue = signal<any[]>([]);
  allMembers = signal<Student[]>([]);
  loading = signal(true);
  searchQuery = '';
  today = new Date().toLocaleDateString('en-IN', {
    weekday: 'long', year: 'numeric', month: 'long', day: 'numeric',
  });

  constructor(
    private api: ApiService,
    public auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (this.auth.isStudent()) {
      this.router.navigate(['/scan']);
      return;
    }
    this.loadDashboard();
  }

  loadDashboard(): void {
    this.loading.set(true);
    this.api.getDailyReport().subscribe({
      next: (data) => {
        this.report.set(data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });

    this.api.getTodayRecords().subscribe({
      next: (data) => this.recentCheckIns.set(data.slice(0, 10)),
    });

    this.api.getPaymentDueMembers().subscribe({
      next: (data) => this.paymentDue.set(data),
    });

    this.api.getStudents().subscribe({
      next: (data) => this.allMembers.set(data),
    });
  }

  get filteredMembers(): Student[] {
    const q = this.searchQuery.trim().toLowerCase();
    if (!q) return [];
    return this.allMembers().filter(
      m => m.name.toLowerCase().includes(q) || m.mobile.includes(q)
    );
  }

  formatTime(dateTime: string): string {
    return new Date(dateTime).toLocaleTimeString('en-IN', {
      hour: '2-digit',
      minute: '2-digit',
    });
  }
}
