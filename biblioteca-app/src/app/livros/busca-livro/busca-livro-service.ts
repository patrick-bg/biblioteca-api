import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface BuscaLivro {
  titulo: string;
  autores: string[];
  isbn: string;
  urlCapa: string | null;
}

@Injectable({
  providedIn: 'root',
})
export class BuscaLivroService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/livros/buscar'

  buscar(termo: string) : Observable<BuscaLivro[]>{
    const params = new HttpParams().set('q', termo);
    return this.http.get<BuscaLivro[]>(this.baseUrl, {params});
  }
}
