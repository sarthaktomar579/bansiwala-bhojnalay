import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/services/api.service';
import { FingerprintService } from '../../core/services/fingerprint.service';
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
  isDummyMode = signal(true);

  students = signal<Student[]>([]);
  selectedStudentId: number | null = null;

  constructor(
    private api: ApiService,
    private fpService: FingerprintService
  ) {}

  ngOnInit(): void {
    this.isDummyMode.set(this.fpService.isDummyMode());
    this.loadStudents();
  }

  loadStudents(): void {
    this.api.getStudents(true).subscribe({
      next: (s) => {
        this.students.set(s.filter((st) => st.hasFingerprintRegistered));
      },
    });
  }

  clearResult(): void {
    this.result.set(null);
    this.error.set('');
    this.selectedStudentId = null;
  }

  toggleDummyMode(): void {
    const newMode = !this.isDummyMode();
    this.isDummyMode.set(newMode);
    this.fpService.setDummyMode(newMode);
  }

  scanAndCheckIn(): void {
    this.processing.set(true);
    this.result.set(null);
    this.error.set('');

    if (this.isDummyMode()) {
      this.checkInDummy();
    } else {
      this.checkInReal();
    }
  }

  /**
   * DUMMY MODE:
   * Simulates fingerprint recognition by selecting a registered student.
   * In real life, the sensor would identify the student automatically.
   * Here, you pick the student (simulating the sensor identifying them),
   * then the system records the meal — same backend logic either way.
   */
  private checkInDummy(): void {
    if (!this.selectedStudentId) {
      this.error.set('Select a student (simulating fingerprint recognition)');
      this.processing.set(false);
      return;
    }

    this.api.checkInManual(+this.selectedStudentId).subscribe({
      next: (res) => {
        this.result.set(res);
        this.processing.set(false);
        this.loadStudents();
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Check-in failed');
        this.processing.set(false);
      },
    });
  }

  /**
   * REAL MODE:
   * Calls the fingerprint device SDK → captures thumb template →
   * sends to backend → backend finds matching student → records meal.
   */
  private checkInReal(): void {
    this.fpService.captureForCheckIn('').subscribe({
      next: (captureResult) => {
        if (!captureResult.success) {
          this.error.set(captureResult.error || 'Fingerprint capture failed');
          this.processing.set(false);
          return;
        }
        this.api
          .checkInByFingerprint({ fingerprintTemplate: captureResult.template })
          .subscribe({
            next: (res) => {
              this.result.set(res);
              this.processing.set(false);
            },
            error: (err) => {
              this.error.set(err.error?.message || 'Fingerprint not recognized');
              this.processing.set(false);
            },
          });
      },
    });
  }
}
