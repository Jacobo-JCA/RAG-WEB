import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';


interface Document {
  idDocument: number;
  name: string;
  dateUploaded: string;
  totalChunks: number;
}

@Injectable({
  providedIn: 'root',
})
export class DocumentService {
  private urlBase = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  obtenerDocumentos(): Observable<Document[]> {
    return this.http.get<Document[]>(`${this.urlBase}/documents`);
  }

  eliminarDocumento(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlBase}/documents/${id}`);
  }
}
