export type MealType = 'LUNCH' | 'DINNER';
export type CheckInMethod = 'QR_SCAN' | 'FINGERPRINT' | 'MANUAL';

export interface CheckInRequest {
  qrCodeUuid?: string;
  fingerprintTemplate?: string;
  mealType?: MealType;
  checkInMethod?: CheckInMethod;
  thaliCount?: number;
}

export interface CheckInResponse {
  recordId: number;
  studentId: number;
  studentName: string;
  mealDate: string;
  mealType: MealType;
  checkInTime: string;
  checkInMethod: CheckInMethod;
  thaliCount: number;
  message?: string;
}

export interface DailyReport {
  date: string;
  lunchCount: number;
  dinnerCount: number;
  totalCheckIns: number;
  lunchThalis: number;
  dinnerThalis: number;
  totalThalis: number;
}

export interface MealEntry {
  recordId: number;
  date: string;
  mealType: MealType;
  checkInTime: string;
  checkInMethod: CheckInMethod;
  thaliCount: number;
}

export interface StudentMealHistory {
  studentId: number;
  studentName: string;
  year: number;
  month: number;
  meals: MealEntry[];
  totalLunch: number;
  totalDinner: number;
}
