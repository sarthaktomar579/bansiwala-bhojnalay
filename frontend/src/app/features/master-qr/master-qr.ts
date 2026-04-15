import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-master-qr',
  imports: [CommonModule],
  templateUrl: './master-qr.html',
  styleUrl: './master-qr.scss',
})
export class MasterQr implements OnInit {
  qrImageUrl = signal('');
  scanUrl = '';

  ngOnInit(): void {
    if (typeof window !== 'undefined') {
      this.scanUrl = `${window.location.origin}/scan`;
      const qrApiUrl = `https://api.qrserver.com/v1/create-qr-code/?size=400x400&data=${encodeURIComponent(this.scanUrl)}`;
      this.qrImageUrl.set(qrApiUrl);
    }
  }

  print(): void {
    window.print();
  }
}
