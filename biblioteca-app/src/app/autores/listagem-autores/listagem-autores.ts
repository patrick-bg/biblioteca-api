import { Component, inject, OnInit } from '@angular/core';
import { AutorService } from '../autor-service';
import { Observable } from 'rxjs';
import { PageResult } from '../../common/pagination/page-result';
import { DetalhesAutor } from '../dados-autor';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterModule } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-listagem-autores',
  imports: [CommonModule, RouterLink, RouterModule],
  templateUrl: './listagem-autores.html',
  styleUrl: './listagem-autores.scss',
})
export class ListagemAutores implements OnInit{

  service = inject(AutorService);
  router = inject(Router);
  toast = inject(ToastrService);
  listagem$! : Observable<PageResult<DetalhesAutor>>; 
  paginaAtual = 0;
  tamanhoPagina = 10;

  ngOnInit(): void {
    this.listarAutores();
  }

  listarAutores(){
    this.listagem$ = this.service.listar(this.paginaAtual, this.tamanhoPagina);
  }

  deletar(id:string) : void{
    this.service.deletar(id).subscribe({
      next: () => {
        this.toast.success('Autor excluído com sucesso.');
        this.listarAutores();
      },
      error: () => this.toast.error('Erro ao excluir o autor.')
    });
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

  calcularIdade(dataNascimento: string|Date, dataFalecimento?: string | Date ) : number {
    if(!dataNascimento){
      return 0;
    }

    const nascimento = dataNascimento instanceof Date ? dataNascimento : new Date(dataNascimento);
    let dataFim = new Date();

    if(dataFalecimento){
      dataFim = dataFalecimento instanceof Date ? dataFalecimento: new Date(dataFalecimento);
    }

    let idade = dataFim.getFullYear() - nascimento.getFullYear();
    const mes = dataFim.getMonth() - nascimento.getMonth();

    if(mes < 0 || (mes === 0 && dataFim.getDate() < nascimento.getDate())){
      idade--;
    }
    return idade;
  }

  prepararEdicao(idAutor : string){
    this.router.navigate(['/paginas/cadastro-autores'], {
      queryParams:{
        id: idAutor
      }
    });
  }


}
