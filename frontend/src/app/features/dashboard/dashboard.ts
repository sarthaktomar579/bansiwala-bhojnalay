import { Component, OnInit, OnDestroy, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../../core/services/api.service';
import { AuthService } from '../../core/services/auth.service';
import { DailyReport, CheckInResponse } from '../../core/models/meal.model';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard implements OnInit, OnDestroy {
  report = signal<DailyReport | null>(null);
  recentCheckIns = signal<CheckInResponse[]>([]);
  loading = signal(true);
  today = new Date().toLocaleDateString('en-IN', {
    weekday: 'long', year: 'numeric', month: 'long', day: 'numeric',
  });
  private refreshTimer: any = null;

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
    this.refreshTimer = setInterval(() => this.refreshData(), 15000);
  }

  ngOnDestroy(): void {
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer);
      this.refreshTimer = null;
    }
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
  }

  private refreshData(): void {
    this.api.getDailyReport().subscribe({
      next: (data) => this.report.set(data),
    });
    this.api.getTodayRecords().subscribe({
      next: (data) => this.recentCheckIns.set(data.slice(0, 10)),
    });
  }

  formatTime(dateTime: string): string {
    return new Date(dateTime).toLocaleTimeString('en-IN', {
      hour: '2-digit',
      minute: '2-digit',
    });
  }
}
