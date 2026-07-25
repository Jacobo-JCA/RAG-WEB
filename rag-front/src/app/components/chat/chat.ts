import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ChatService } from '../../services/chat-service/chatservice';

interface Message {
  id: number;
  rol: 'usuario' | 'bot';
  texto: string;
}

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './chat.html',
  styleUrl: './chat.css'
})
export class Chat {
  pregunta = '';
  cargando = false;
  mensajes: Message[] = [];
  private contadorId = 0;

  constructor(private chatService: ChatService) {}

  enviarPregunta(): void {
    if (!this.pregunta.trim() || this.cargando) return;

    const textoPregunta = this.pregunta;
    this.pregunta = '';

    this.mensajes.push({
      id: ++this.contadorId,
      rol: 'usuario',
      texto: textoPregunta
    });

    this.cargando = true;

    this.chatService.preguntar(textoPregunta).subscribe({
      next: (respuesta) => {
        this.mensajes.push({
          id: ++this.contadorId,
          rol: 'bot',
          texto: respuesta.respuesta
        });
        this.cargando = false;
      },
      error: () => {
        this.mensajes.push({
          id: ++this.contadorId,
          rol: 'bot',
          texto: '❌ Error al consultar el documento'
        });
        this.cargando = false;
      }
    });
  }
}