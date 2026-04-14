export interface Student {
  id: number;
  name: string;
  mobile: string;
  email?: string;
  qrCodeUuid: string;
  hasFingerprintRegistered: boolean;
  isActive: boolean;
  createdAt: string;
}

export interface StudentRequest {
  name: string;
  mobile: string;
  email?: string;
  fingerprintTemplate?: string;
}
