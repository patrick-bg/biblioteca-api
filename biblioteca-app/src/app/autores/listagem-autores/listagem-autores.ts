import { Component, inject, OnInit } from '@angular/core';
import { AutorService } from '../autor-service';
import { Observable } from 'rxjs';
import { PageResult } from '../../common/pagination/page-result';
import { DetalhesAutor } from '../dados-autor';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-listagem-autores',
  imports: [CommonModule],
  templateUrl: './listagem-autores.html',
  styleUrl: './listagem-autores.scss',
})
export class ListagemAutores implements OnInit{

  service = inject(AutorService);
  listagem$! : Observable<PageResult<DetalhesAutor>>; 
  paginaAtual = 0;
  tamanhoPagina = 10;

  ngOnInit(): void {
    this.listarAutores();
  }

  listarAutores(){
    this.listagem$ = this.service.listar(this.paginaAtual, this.tamanhoPagina);
  }

  navegar(pagina : number) {
    this.paginaAtual = pagina;
    this.listarAutores();
  }

  navegarProximo(listagem : PageResult<DetalhesAutor>){
    if(!listagem.last){
      this.navegar(listagem.number + 1);
    }
  }

  navegarAnterior(listagem : PageResult<DetalhesAutor>){
    if(!listagem.first){
      this.navegar(listagem.number - 1);
    }
  }

  paginas(totalPages : number) : number[]{
    return Array.from( { length: totalPages }, (valor, index) => index );
  }

  registroInicial(listagem: PageResult<DetalhesAutor>){
    if(listagem.totalElements === 0){
      return 0;
    }

    return(listagem.number * listagem.size) + 1;
  }

  registroFinal(listagem: PageResult<DetalhesAutor>){
    if(listagem.totalElements === 0){
      return 0;
    }

    return Math.min( (listagem.number + 1) * listagem.size, listagem.totalElements);
  }

  calcularIdade(dataNascimento: Date) : number {
    if(!dataNascimento){
      return 0;
    }

    const hoje = new Date();
    const nascimento = new Date(dataNascimento);

    let idade = hoje.getFullYear() - nascimento.getFullYear();
    const mes = hoje.getMonth() - nascimento.getMonth();

    if(mes < 0 || (mes === 0 && hoje.getDate() < nascimento.getDate())){
      idade--;
    }
    return idade;
  }
}
