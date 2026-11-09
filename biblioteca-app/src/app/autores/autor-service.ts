import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DadosAutorForm, DetalhesAutor } from './dados-autor';
import { Observable } from 'rxjs';
import { PageResult } from '../common/pagination/page-result';

@Injectable({
  providedIn: 'root',
})
export class AutorService {
  http = inject(HttpClient);
  baseUrl = `http://localhost:8080/autores`;

  criar(dados: DadosAutorForm) : Observable<DetalhesAutor>{
    return this.http.post<DetalhesAutor>(this.baseUrl, dados);
  }

  listar(page: number=  0, size: number = 10) : Observable<PageResult<DetalhesAutor>>{
    const url = `${this.baseUrl}?page=${page}&size=${size}`;
    return this.http.get<PageResult<DetalhesAutor>>(url);
  }

}
