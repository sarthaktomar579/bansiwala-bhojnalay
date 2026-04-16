import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../core/services/api.service';
import { Student } from '../../core/models/student.model';

@Component({
  selector: 'app-students',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './students.html',
  styleUrl: './students.scss',
})
export class Students implements OnInit {
  students = signal<Student[]>([]);
  dueIds = signal<Set<number>>(new Set());
  loading = signal(true);
  paymentStudent = signal<Student | null>(null);
  paymentAmount = 0;
  searchQuery = '';
  showDueOnly = false;

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.loadStudents();
    this.loadDueMembers();
  }

  loadStudents(): void {
    this.loading.set(true);
    this.api.getStudents().subscribe({
      next: (data) => {
        this.students.set(data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  loadDueMembers(): void {
    this.api.getPaymentDueMembers().subscribe({
      next: (data) => {
        const ids = new Set<number>();
        data.filter((m: any) => m.paymentDue).forEach((m: any) => ids.add(m.studentId));
        this.dueIds.set(ids);
      },
    });
  }

  get dueCount(): number {
    return this.dueIds().size;
  }

  isDue(id: number): boolean {
    return this.dueIds().has(id);
  }

  toggleDueFilter(): void {
    this.showDueOnly = !this.showDueOnly;
  }

  get displayedMembers(): Student[] {
    let list = this.students();
    const q = this.searchQuery.trim().toLowerCase();
    if (q) {
      list = list.filter(m => m.name.toLowerCase().includes(q) || m.mobile.includes(q));
    }
    if (this.showDueOnly) {
      list = list.filter(m => this.isDue(m.id));
    }
    return list;
  }

  toggleActive(student: Student): void {
    const action = student.isActive
      ? this.api.deactivateStudent(student.id)
      : this.api.activateStudent(student.id);
    action.subscribe({ next: () => this.loadStudents() });
  }

  clearDue(studentId: number): void {
    this.api.clearPaymentDue(studentId).subscribe({
      next: () => {
        this.loadStudents();
        this.loadDueMembers();
      },
    });
  }

  openPayment(student: Student): void {
    this.paymentStudent.set(student);
    this.paymentAmount = 0;
  }

  closePayment(): void {
    this.paymentStudent.set(null);
    this.paymentAmount = 0;
  }

  submitPayment(): void {
    const s = this.paymentStudent();
    if (!s || this.paymentAmount <= 0) return;
    this.api.recordPayment(s.id, this.paymentAmount).subscribe({
      next: () => {
        this.closePayment();
        this.loadStudents();
        this.loadDueMembers();
      },
    });
  }
}
