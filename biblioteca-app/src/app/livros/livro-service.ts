import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { DadosLivroForm, DetalhesLivros, FiltroLivro } from './dados-livros';
import { Observable, Observer } from 'rxjs';
import { DetalhesAutor } from '../autores/dados-autor';
import { PageResult } from '../common/pagination/page-result';

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
    return this.http.get<DetalhesAutor[]>(`${this.baseUrl}/autores-listagem`)
  }

  deletar(id: string) : Observable<void>{
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  atualizar(id: string) : Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  obterPorId(id: string) : Observable<DetalhesLivros>{
    console.log('Obter por ID com sucesso!');
    return this.http.get<DetalhesLivros>(`${this.baseUrl}/${id}`);
  }

  listrar(filtros: FiltroLivro): Observable<PageResult<DetalhesLivros>>{
    let params = new HttpParams();

    if(filtros.ano){
      params = params.set('ano', filtros.ano);
    }

    if(filtros.status){
      params = params.set('status', filtros.status);
    }

    if(filtros.autorId){
      params = params.set('autor-id', filtros.autorId);
    }

    if(filtros.genero){
      params = params.set('genero', filtros.genero);
    }

    if(filtros.idioma){
      params = params.set('idioma', filtros.idioma);
    }

    if(filtros.titulo){
      params = params.set('titulo', filtros.titulo);
    }
    
    if(filtros.autorGenero){
      params = params.set('autor-genero', filtros.autorGenero);
    }

    if(filtros.paisOrigemAutor){
      params = params.set('pais-origem', filtros.paisOrigemAutor);
    }    

    if(filtros.editora){
      params = params.set('editora', filtros.editora);
    }

    let page = filtros.page ?? 0;
    let size = filtros.size ?? 10;

    params = params.set('page', page).set('size',size);

    return this.http.get<PageResult<DetalhesLivros>>(this.baseUrl, {params});
  }
}
