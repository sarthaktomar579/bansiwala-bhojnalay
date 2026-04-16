import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/services/api.service';
import { AuthService } from '../../core/services/auth.service';
import { CheckInResponse } from '../../core/models/meal.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-scan-checkin',
  imports: [CommonModule, FormsModule],
  templateUrl: './scan-checkin.html',
  styleUrl: './scan-checkin.scss',
})
export class ScanCheckin implements OnInit {
  result = signal<CheckInResponse | null>(null);
  error = signal<string>('');
  processing = signal(false);
  thaliCount = 1;
  studentName = '';

  constructor(
    private api: ApiService,
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (!this.auth.isStudent() || !this.auth.studentId()) {
      this.router.navigate(['/']);
      return;
    }
    this.studentName = this.auth.username();
    this.warmUpSpeech();
  }

  private warmUpSpeech(): void {
    if (typeof window === 'undefined' || !('speechSynthesis' in window)) return;
    const warmup = new SpeechSynthesisUtterance('');
    warmup.volume = 0;
    window.speechSynthesis.speak(warmup);
    window.speechSynthesis.getVoices();
  }

  checkIn(): void {
    const sid = this.auth.studentId();
    if (!sid) return;

    this.processing.set(true);
    this.result.set(null);
    this.error.set('');

    this.api.checkInByScan(sid, this.thaliCount).subscribe({
      next: (res) => {
        this.result.set(res);
        this.processing.set(false);
        this.announceCheckIn(res);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Check-in failed');
        this.processing.set(false);
      },
    });
  }

  reset(): void {
    this.result.set(null);
    this.error.set('');
    this.thaliCount = 1;
  }

  private announceCheckIn(res: CheckInResponse): void {
    if (typeof window === 'undefined' || !('speechSynthesis' in window)) return;

    window.speechSynthesis.cancel();

    const count = this.thaliCount;
    const text = `Check-in done for ${count} thali by ${res.studentName}. Jai Shree Krishna.`;

    const utterance = new SpeechSynthesisUtterance(text);
    utterance.lang = 'en-IN';
    utterance.rate = 1.1;
    utterance.volume = 1.0;
    utterance.pitch = 1.0;

    const voices = window.speechSynthesis.getVoices();
    const indianVoice = voices.find(v => v.lang.startsWith('en') && v.localService);
    if (indianVoice) utterance.voice = indianVoice;

    window.speechSynthesis.speak(utterance);
  }
}
