import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DadosAutorForm, DetalhesAutor } from './dados-autor';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AutorService {
  http = inject(HttpClient);

  criar(dados: DadosAutorForm) : Observable<DetalhesAutor>{
    const url = 'http://localhost:8080/autores'
    return this.http.post<DetalhesAutor>(url, dados);
  }
}
