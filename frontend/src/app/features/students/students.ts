import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../core/services/api.service';
import { Student } from '../../core/models/student.model';

@Component({
  selector: 'app-students',
  imports: [CommonModule, RouterLink],
  templateUrl: './students.html',
  styleUrl: './students.scss',
})
export class Students implements OnInit {
  students = signal<Student[]>([]);
  loading = signal(true);

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.loadStudents();
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

  toggleActive(student: Student): void {
    const action = student.isActive
      ? this.api.deactivateStudent(student.id)
      : this.api.activateStudent(student.id);

    action.subscribe({ next: () => this.loadStudents() });
  }
}
