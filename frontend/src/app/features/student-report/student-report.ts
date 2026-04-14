import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ApiService } from '../../core/services/api.service';
import { AuthService } from '../../core/services/auth.service';
import { StudentMealHistory, MealEntry } from '../../core/models/meal.model';

@Component({
  selector: 'app-student-report',
  imports: [CommonModule, RouterLink],
  templateUrl: './student-report.html',
  styleUrl: './student-report.scss',
})
export class StudentReport implements OnInit {
  studentId = 0;
  year: number;
  month: number;
  history = signal<StudentMealHistory | null>(null);
  loading = signal(true);
  calendarDays: CalendarDay[] = [];

  monthNames = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December',
  ];

  constructor(
    private api: ApiService,
    private auth: AuthService,
    private route: ActivatedRoute
  ) {
    const now = new Date();
    this.year = now.getFullYear();
    this.month = now.getMonth() + 1;
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.studentId = +idParam;
    } else if (this.auth.isStudent() && this.auth.studentId()) {
      this.studentId = this.auth.studentId()!;
    }
    this.loadReport();
  }

  loadReport(): void {
    this.loading.set(true);
    this.api.getStudentMonthlyReport(this.studentId, this.year, this.month).subscribe({
      next: (data) => {
        this.history.set(data);
        this.buildCalendar(data.meals);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  prevMonth(): void {
    this.month--;
    if (this.month < 1) { this.month = 12; this.year--; }
    this.loadReport();
  }

  nextMonth(): void {
    this.month++;
    if (this.month > 12) { this.month = 1; this.year++; }
    this.loadReport();
  }

  get monthLabel(): string {
    return `${this.monthNames[this.month - 1]} ${this.year}`;
  }

  private buildCalendar(meals: MealEntry[]): void {
    const daysInMonth = new Date(this.year, this.month, 0).getDate();
    const mealsByDate: Record<string, Set<string>> = {};

    for (const m of meals) {
      const day = m.date;
      if (!mealsByDate[day]) mealsByDate[day] = new Set();
      mealsByDate[day].add(m.mealType);
    }

    this.calendarDays = [];
    for (let d = 1; d <= daysInMonth; d++) {
      const dateStr = `${this.year}-${String(this.month).padStart(2, '0')}-${String(d).padStart(2, '0')}`;
      const dayMeals = mealsByDate[dateStr] || new Set();
      this.calendarDays.push({
        day: d,
        date: dateStr,
        breakfast: dayMeals.has('BREAKFAST'),
        lunch: dayMeals.has('LUNCH'),
        dinner: dayMeals.has('DINNER'),
      });
    }
  }
}

interface CalendarDay {
  day: number;
  date: string;
  breakfast: boolean;
  lunch: boolean;
  dinner: boolean;
}
