import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../../core/services/api.service';
import { FingerprintService } from '../../core/services/fingerprint.service';
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
  capturing = signal(false);
  error = signal('');
  success = signal('');

  form: StudentRequest = {
    name: '',
    mobile: '',
    email: '',
    fingerprintTemplate: '',
  };

  constructor(
    private api: ApiService,
    public fpService: FingerprintService,
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
          if (s.hasFingerprintRegistered) {
            this.success.set('Fingerprint already registered');
          }
        },
      });
    }
  }

  captureFingerprint(): void {
    if (!this.form.name.trim()) {
      this.error.set('Enter student name first');
      return;
    }
    this.capturing.set(true);
    this.error.set('');
    this.success.set('');

    this.fpService.captureForRegistration(this.form.name).subscribe({
      next: (result) => {
        this.capturing.set(false);
        if (result.success) {
          this.form.fingerprintTemplate = result.template;
          this.success.set('Fingerprint captured successfully!');
        } else {
          this.error.set(result.error || 'Fingerprint capture failed');
        }
      },
    });
  }

  onSubmit(): void {
    this.error.set('');
    this.success.set('');

    if (!this.form.name.trim() || !this.form.mobile.trim()) {
      this.error.set('Name and mobile number are required');
      return;
    }

    if (!this.form.fingerprintTemplate && !this.isEdit()) {
      this.error.set('Please capture fingerprint before registering');
      return;
    }

    this.saving.set(true);

    const request: StudentRequest = {
      name: this.form.name.trim(),
      mobile: this.form.mobile.trim(),
      email: this.form.email?.trim() || undefined,
      fingerprintTemplate: this.form.fingerprintTemplate?.trim() || undefined,
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
        const msg = err.error?.message || err.error?.details || 'Failed to save student';
        this.error.set(typeof msg === 'string' ? msg : JSON.stringify(msg));
      },
    });
  }
}
