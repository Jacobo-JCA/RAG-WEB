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

  // preguntar(question: string): Observable<RespuestaChat> {
  //   console.log('preguntar llamado con:', question);
  // console.log('url:', `${this.urlBase}/chat/ask`);
  //   return this.http.post<RespuestaChat>(`${this.urlBase}/chat/ask`, { question });
    
  // }
  preguntar(question: string): Observable<RespuestaChat> {
  console.log('preguntar llamado con:', question);
  console.log('url:', `${this.urlBase}/chat/ask`);

  const obs = this.http.post<RespuestaChat>(`${this.urlBase}/chat/ask`, { question });
  console.log('observable creado:', obs);
  return obs;  // ← ahora retorna la variable, no directamente el http.post
}
}
