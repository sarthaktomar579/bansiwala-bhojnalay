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

  private readonly productionUrl = 'https://bansiwala-bhojnalay.vercel.app';

  ngOnInit(): void {
    this.scanUrl = `${this.productionUrl}/scan`;
    const qrApiUrl = `https://api.qrserver.com/v1/create-qr-code/?size=400x400&data=${encodeURIComponent(this.scanUrl)}`;
    this.qrImageUrl.set(qrApiUrl);
  }

  print(): void {
    window.print();
  }
}
