import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../../core/services/api.service';
import { StudentRequest } from '../../core/models/student.model';

@Component({
  selector: 'app-student-form',
  imports: [CommonModule, FormsModule],
  templateUrl: './student-form.html',
  styleUrl: './student-form.scss',
})
export class StudentForm implements OnInit {
  isEdit = signal(false);
  studentId = 0;
  saving = signal(false);
  error = signal('');
  success = signal('');

  form: StudentRequest = {
    name: '',
    mobile: '',
    email: '',
  };

  constructor(
    private api: ApiService,
    private route: ActivatedRoute,
    public router: Router
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit.set(true);
      this.studentId = +id;
      this.api.getStudent(this.studentId).subscribe({
        next: (s) => {
          this.form.name = s.name;
          this.form.mobile = s.mobile;
          this.form.email = s.email || '';
        },
      });
    }
  }

  onSubmit(): void {
    this.error.set('');
    this.success.set('');

    if (!this.form.name.trim() || !this.form.mobile.trim()) {
      this.error.set('Name and mobile number are required');
      return;
    }

    this.saving.set(true);

    const request: StudentRequest = {
      name: this.form.name.trim(),
      mobile: this.form.mobile.trim(),
      email: this.form.email?.trim() || undefined,
    };

    const obs = this.isEdit()
      ? this.api.updateStudent(this.studentId, request)
      : this.api.registerStudent(request);

    obs.subscribe({
      next: () => {
        this.saving.set(false);
        this.router.navigate(['/students']);
      },
      error: (err) => {
        this.saving.set(false);
        const msg = err.error?.message || err.error?.details || 'Failed to save member';
        this.error.set(typeof msg === 'string' ? msg : JSON.stringify(msg));
      },
    });
  }
}
