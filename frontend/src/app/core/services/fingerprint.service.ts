import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

export interface FingerprintCaptureResult {
  success: boolean;
  template: string;
  error?: string;
}

@Injectable({ providedIn: 'root' })
export class FingerprintService {
  private useDummyMode = true;

  isDummyMode(): boolean {
    return this.useDummyMode;
  }

  setDummyMode(enabled: boolean): void {
    this.useDummyMode = enabled;
  }

  /**
   * Generate a unique fingerprint template for registration.
   * In real mode: device captures real thumb → returns ISO template.
   * In dummy mode: generates a unique string that acts like a real template.
   */
  captureForRegistration(studentName: string): Observable<FingerprintCaptureResult> {
    if (this.useDummyMode) {
      const slug = studentName.toUpperCase().replace(/\s+/g, '_');
      const unique = Date.now().toString(36) + Math.random().toString(36).substring(2, 6);
      const template = `FP_${slug}_${unique}`;
      return of({ success: true, template });
    }
    return this.captureFromDevice();
  }

  /**
   * Capture fingerprint for check-in.
   * In real mode: device captures thumb → returns template → backend matches.
   * In dummy mode: directly send the known template of a student to simulate match.
   */
  captureForCheckIn(knownTemplate: string): Observable<FingerprintCaptureResult> {
    if (this.useDummyMode) {
      return of({ success: true, template: knownTemplate });
    }
    return this.captureFromDevice();
  }

  /**
   * REAL DEVICE INTEGRATION
   * When you buy a fingerprint scanner (e.g., Mantra MFS100):
   * 1. Install the device driver on your laptop
   * 2. The driver runs a local HTTPS service (e.g., https://localhost:11100)
   * 3. This method calls that service to capture the thumb
   * 4. Returns the ISO fingerprint template string
   *
   * To activate: set useDummyMode = false, and uncomment the fetch code below.
   */
  private captureFromDevice(): Observable<FingerprintCaptureResult> {
    return new Observable((observer) => {
      // ──────────────────────────────────────────────────────
      // UNCOMMENT THIS WHEN YOU HAVE A REAL FINGERPRINT DEVICE:
      //
      // const deviceUrl = 'https://localhost:11100/capture';
      // fetch(deviceUrl, {
      //   method: 'POST',
      //   body: JSON.stringify({ timeout: 10000 }),
      //   headers: { 'Content-Type': 'application/json' }
      // })
      //   .then(res => res.json())
      //   .then(data => {
      //     if (data.errorCode === 0) {
      //       observer.next({ success: true, template: data.isoTemplate });
      //     } else {
      //       observer.next({ success: false, template: '', error: data.errorMessage });
      //     }
      //     observer.complete();
      //   })
      //   .catch(err => {
      //     observer.next({
      //       success: false,
      //       template: '',
      //       error: 'Fingerprint device not connected: ' + err.message
      //     });
      //     observer.complete();
      //   });
      // ──────────────────────────────────────────────────────

      observer.next({
        success: false,
        template: '',
        error: 'No fingerprint device connected. Turn on Dummy Mode for testing.',
      });
      observer.complete();
    });
  }
}
