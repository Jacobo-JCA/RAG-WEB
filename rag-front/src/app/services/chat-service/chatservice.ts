import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';


interface RespuestaChat {
  respuesta: string;
}

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  private urlBase = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {
  }

  preguntar(question: string): Observable<RespuestaChat> {
    return this.http.post<RespuestaChat>(`${this.urlBase}/chat/ask`, { question });
    
  }
}
