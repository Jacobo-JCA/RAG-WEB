import { Component, ElementRef, ViewChild } from '@angular/core';
import { Pdf } from '../../services/pdf-service/pdf';

@Component({
  selector: 'app-upload',
  imports: [],
  templateUrl: './upload.html',
  styleUrl: './upload.css',
})
export class Upload {
  archivo: File | null = null;
  subiendo = false;
  mensaje = '';

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
    this.subiendo = true;
    this.pdf.subirPdf(this.archivo).subscribe({
      next: () => {
        this.mensaje = '✅ PDF subido correctamente';
        this.archivo = null;
        this.subiendo = false;
      },
      error: () => {
        this.mensaje = '❌ Error al subir el PDF';
        this.subiendo = false;
      }
    });
  }
}
