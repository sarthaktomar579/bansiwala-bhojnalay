export type MealType = 'BREAKFAST' | 'LUNCH' | 'DINNER';
export type CheckInMethod = 'QR_SCAN' | 'FINGERPRINT' | 'MANUAL';

export interface CheckInRequest {
  qrCodeUuid?: string;
  fingerprintTemplate?: string;
  mealType?: MealType;
  checkInMethod?: CheckInMethod;
}

export interface CheckInResponse {
  recordId: number;
  studentId: number;
  studentName: string;
  mealDate: string;
  mealType: MealType;
  checkInTime: string;
  checkInMethod: CheckInMethod;
  message?: string;
}

export interface DailyReport {
  date: string;
  breakfastCount: number;
  lunchCount: number;
  dinnerCount: number;
  totalCheckIns: number;
}

export interface MealEntry {
  date: string;
  mealType: MealType;
  checkInTime: string;
  checkInMethod: CheckInMethod;
}

export interface StudentMealHistory {
  studentId: number;
  studentName: string;
  year: number;
  month: number;
  meals: MealEntry[];
  totalBreakfast: number;
  totalLunch: number;
  totalDinner: number;
}
