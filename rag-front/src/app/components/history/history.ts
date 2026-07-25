import { Component, OnInit } from '@angular/core';
import { DocumentService } from '../../services/document/documentservice';


interface Document {
  id: number;
  nombre: string;
  fechaSubida: string;
  tamanio: number;
}

@Component({
  selector: 'app-history',
  imports: [],
  templateUrl: './history.html',
  styleUrl: './history.css',
})
export class History implements OnInit {
  documentos: Document[] = [];
  cargando = false;

  constructor(private documentService: DocumentService) {}

  ngOnInit(): void {
    this.cargarDocumentos();
  }

  cargarDocumentos(): void {
    this.cargando = true;
    this.documentService.obtenerDocumentos().subscribe({
      next: (docs) => {
        this.documentos = docs;
        this.cargando = false;
      },
      error: () => {
        this.cargando = false;
      }
    });
  }

  eliminar(id: number): void {
    this.documentService.eliminarDocumento(id).subscribe({
      next: () => {
        this.documentos = this.documentos.filter(doc => doc.id !== id);
      }
    });
  }
}
