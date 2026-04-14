import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Student, StudentRequest } from '../models/student.model';
import {
  CheckInRequest,
  CheckInResponse,
  DailyReport,
  MealType,
  StudentMealHistory,
} from '../models/meal.model';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  // ─── Students ──────────────────────────────────────
  getStudents(activeOnly = false): Observable<Student[]> {
    const params = new HttpParams().set('activeOnly', activeOnly);
    return this.http.get<Student[]>(`${this.baseUrl}/students`, { params });
  }

  getStudent(id: number): Observable<Student> {
    return this.http.get<Student>(`${this.baseUrl}/students/${id}`);
  }

  registerStudent(req: StudentRequest): Observable<Student> {
    return this.http.post<Student>(`${this.baseUrl}/students`, req);
  }

  updateStudent(id: number, req: StudentRequest): Observable<Student> {
    return this.http.put<Student>(`${this.baseUrl}/students/${id}`, req);
  }

  getStudentQr(id: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/students/${id}/qr`, {
      responseType: 'blob',
    });
  }

  registerFingerprint(id: number, template: string): Observable<Student> {
    return this.http.patch<Student>(
      `${this.baseUrl}/students/${id}/fingerprint`,
      { fingerprintTemplate: template }
    );
  }

  deactivateStudent(id: number): Observable<void> {
    return this.http.patch<void>(
      `${this.baseUrl}/students/${id}/deactivate`,
      {}
    );
  }

  activateStudent(id: number): Observable<void> {
    return this.http.patch<void>(
      `${this.baseUrl}/students/${id}/activate`,
      {}
    );
  }

  // ─── Check-In ──────────────────────────────────────
  checkInByQr(req: CheckInRequest): Observable<CheckInResponse> {
    return this.http.post<CheckInResponse>(
      `${this.baseUrl}/meals/check-in/qr`,
      req
    );
  }

  checkInByFingerprint(req: CheckInRequest): Observable<CheckInResponse> {
    return this.http.post<CheckInResponse>(
      `${this.baseUrl}/meals/check-in/fingerprint`,
      req
    );
  }

  checkInManual(studentId: number, mealType?: MealType): Observable<CheckInResponse> {
    let params = new HttpParams();
    if (mealType) params = params.set('mealType', mealType);
    return this.http.post<CheckInResponse>(
      `${this.baseUrl}/meals/check-in/manual/${studentId}`,
      null,
      { params }
    );
  }

  getTodayRecords(mealType?: MealType): Observable<CheckInResponse[]> {
    let params = new HttpParams();
    if (mealType) params = params.set('mealType', mealType);
    return this.http.get<CheckInResponse[]>(`${this.baseUrl}/meals/today`, {
      params,
    });
  }

  // ─── Reports ──────────────────────────────────────
  getDailyReport(date?: string): Observable<DailyReport> {
    let params = new HttpParams();
    if (date) params = params.set('date', date);
    return this.http.get<DailyReport>(`${this.baseUrl}/reports/daily`, {
      params,
    });
  }

  getStudentMonthlyReport(
    studentId: number,
    year: number,
    month: number
  ): Observable<StudentMealHistory> {
    const params = new HttpParams()
      .set('year', year)
      .set('month', month);
    return this.http.get<StudentMealHistory>(
      `${this.baseUrl}/reports/student/${studentId}/monthly`,
      { params }
    );
  }
}
