import { Component, OnInit, signal } from '@angular/core';
import { DocumentService } from '../../services/document/documentservice';


interface Document {
  idDocument: number;
  name: string;
  dateUploaded: string;
  totalChunks: number;
}

@Component({
  selector: 'app-history',
  imports: [],
  templateUrl: './history.html',
  styleUrl: './history.css',
})
export class History implements OnInit {
  documentos = signal<Document[]>([]);
  cargando = signal<boolean>(false);

  constructor(private documentService: DocumentService) {}

  ngOnInit(): void {
    this.cargarDocumentos();
  }

  cargarDocumentos(): void {
    this.cargando.set(true);
    this.documentService.obtenerDocumentos().subscribe({
      next: (docs) => {
        this.documentos.set(docs);
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
      }
    });
  }

  eliminar(id: number): void {
    this.documentService.eliminarDocumento(id).subscribe({
      next: () => {
        this.documentos.update(currentDocs => currentDocs.filter(doc => doc.idDocument !== id));
      }
    });
  }
}
