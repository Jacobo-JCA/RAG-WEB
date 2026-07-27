import { Component, ElementRef, signal, ViewChild } from '@angular/core';
import { Pdf } from '../../services/pdf-service/pdf';

@Component({
  selector: 'app-upload',
  imports: [],
  templateUrl: './upload.html',
  styleUrl: './upload.css',
})
export class Upload {
  archivo: File | null = null;
  subiendo = signal<boolean>(false);
  mensaje = signal<string>('');

  @ViewChild('inputArchivo')
  inputArchivo!: ElementRef<HTMLInputElement>;

  constructor(private pdf: Pdf) {}

  abrirSelector(): void {
    this.inputArchivo.nativeElement.click();
  }

  archivoSeleccionado(evento: Event): void {
    const input = evento.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.archivo = input.files[0];
    }
  }

  subirArchivo(): void {
    if (!this.archivo) return;
    this.subiendo.set(true);
    this.pdf.subirPdf(this.archivo).subscribe({
      next: () => {
        this.mensaje.set('✅ PDF subido correctamente');
        this.archivo = null;
        this.subiendo.set(false);
      },
      error: () => {
        this.mensaje.set('❌ Error al subir el PDF');
        this.subiendo.set(false);
      }
    });
  }
}
