import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/services/api.service';
import { Student } from '../../core/models/student.model';
import { CheckInResponse } from '../../core/models/meal.model';

@Component({
  selector: 'app-check-in',
  imports: [CommonModule, FormsModule],
  templateUrl: './check-in.html',
  styleUrl: './check-in.scss',
})
export class CheckIn implements OnInit {
  result = signal<CheckInResponse | null>(null);
  error = signal<string>('');
  processing = signal(false);

  students = signal<Student[]>([]);
  selectedStudentId: number | null = null;
  thaliCount = 1;
  thaliOptions = Array.from({ length: 30 }, (_, i) => i + 1);

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.loadStudents();
  }

  loadStudents(): void {
    this.api.getStudents(true).subscribe({
      next: (s) => this.students.set(s),
    });
  }

  clearResult(): void {
    this.result.set(null);
    this.error.set('');
    this.selectedStudentId = null;
    this.thaliCount = 1;
  }

  checkIn(): void {
    if (!this.selectedStudentId) {
      this.error.set('Please select a member');
      return;
    }

    this.processing.set(true);
    this.result.set(null);
    this.error.set('');

    this.api.checkInManual(+this.selectedStudentId, undefined, this.thaliCount).subscribe({
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
}
