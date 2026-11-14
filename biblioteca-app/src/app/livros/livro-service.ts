import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { DadosLivroForm, DetalhesLivros } from './dados-livros';
import { Observable } from 'rxjs';
import { DetalhesAutor } from '../autores/dados-autor';

@Injectable({
  providedIn: 'root',
})
export class LivroService {
  http = inject(HttpClient);
  baseUrl = 'http://localhost:8080/livros'

  criar(dados: DadosLivroForm) : Observable<DetalhesLivros>{
    return this.http.post<DetalhesLivros>(this.baseUrl,dados);
  }

  listarAutores(): Observable<DetalhesAutor[]>{
    return this.http.get<DetalhesAutor[]>(`${this.baseUrl}/autores-cadastro-livro`)
  }

  deletar(id: string) : Observable<void>{
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
