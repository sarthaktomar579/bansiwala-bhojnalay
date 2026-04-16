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
  memberThalis = signal<any[]>([]);
  loading = signal(true);
  paymentStudent = signal<Student | null>(null);
  paymentAmount = 0;
  searchQuery = '';

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.loadStudents();
    this.loadMemberThalis();
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

  loadMemberThalis(): void {
    this.api.getPaymentDueMembers().subscribe({
      next: (data) => this.memberThalis.set(data),
    });
  }

  get filteredStudents(): Student[] {
    const q = this.searchQuery.trim().toLowerCase();
    if (!q) return this.students();
    return this.students().filter(
      m => m.name.toLowerCase().includes(q) || m.mobile.includes(q)
    );
  }

  toggleActive(student: Student): void {
    const action = student.isActive
      ? this.api.deactivateStudent(student.id)
      : this.api.activateStudent(student.id);
    action.subscribe({ next: () => this.loadStudents() });
  }

  openPaymentById(studentId: number, name: string, amountPaid: number): void {
    this.paymentStudent.set({ id: studentId, name, amountPaid } as Student);
    this.paymentAmount = 0;
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
        this.loadMemberThalis();
      },
    });
  }
}
