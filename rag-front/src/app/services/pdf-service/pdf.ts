import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class Pdf {
  private urlBase = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  subirPdf(archivo: File): Observable<any> {
    const formData = new FormData();
    formData.append('archivo', archivo);
    return this.http.post(`${this.urlBase}/documents/subir`, formData);
  }
}
