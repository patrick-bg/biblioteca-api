import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { DetalhesLivros, StatusLivro } from '../dados-livros';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterModule } from '@angular/router';
import { Header } from '../../common/components/header/header';
import { LivroService } from '../livro-service';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { PageResult } from '../../common/pagination/page-result';
import { DetalhesAutor } from '../../autores/dados-autor';
import { EnumLabelPipe } from '../../common/components/enum-label.pipe';

interface FiltrosForm {
  ano: FormControl<string>;
  genero: FormControl<string>;
  autorId: FormControl<string>;
  idioma: FormControl<string>;
  formato: FormControl<string>;
  titulo: FormControl<string>;
  editora: FormControl<string>;
  status: FormControl<StatusLivro | ''>;
  nota: FormControl<number | null>;
  autorNome: FormControl<string>;
  autorGenero: FormControl<string>;
  paisOrigemAutor: FormControl<string>;
}

@Component({
  selector: 'app-listagem-livros',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, Header, RouterModule, EnumLabelPipe],
  templateUrl: './listagem-livros.html',
  styleUrl: './listagem-livros.scss',
})
export class ListagemLivros implements OnInit {

  service = inject(LivroService);
  toast = inject(ToastrService);
  router = inject(Router)

  listagem$!: Observable<PageResult<DetalhesLivros>>;
  autores: DetalhesAutor[] = [];
  livros: DetalhesLivros [] = [];
  paginaAtual = 0;
  tamanhoPagina = 10;

  form!: FormGroup;

  ngOnInit(): void {
    this.form = new FormGroup<FiltrosForm>({
      ano: new FormControl<string>('', {nonNullable: true}),
      genero: new FormControl<string>('', {nonNullable: true}),
      autorId: new FormControl<string>('', {nonNullable: true}),
      idioma: new FormControl<string>('', {nonNullable: true}),
      formato: new FormControl<string>('', {nonNullable: true}),
      titulo: new FormControl<string>('', {nonNullable: true}),
      editora: new FormControl<string>('', {nonNullable: true}),
      status: new FormControl<StatusLivro | ''>('', {nonNullable: true}),
      nota: new FormControl<number | null>(null, {nonNullable: true}),
      autorNome: new FormControl<string>('', {nonNullable: true}),
      autorGenero: new FormControl<string>('', {nonNullable: true}),
      paisOrigemAutor: new FormControl<string>('', {nonNullable: true})
    }); 

    this.listarAutores();
    this.listarLivros();
  }

  deletar(id: string) : void {
    const confirmacao = confirm('Tem certeza que deseja excluir o livro?');

    if(confirmacao){
      this.service.deletar(id).subscribe({
        next: () => {
          this.toast.success('Livro excluído com sucesso.');
          this.listarLivros();
        },
        error: () => this.toast.error('Erro ao excluir o livro.')
      });
    }
  }

  listarAutores(){
    this.service.listarAutores().subscribe({
      next: (resultado: DetalhesAutor[]) => {
        this.autores = resultado;
      },
      error: () => this.toast.error('Erro ao carregar autores para filtro.')
    });
  }

  listarLivros(){
    const ano = this.form.get('ano')?.value;
    const genero = this.form.get('genero')?.value;
    const autorId = this.form.get('autorId')?.value;
    const idioma = this.form.get('idioma')?.value;
    const formato = this.form.get('formato')?.value;
    const titulo = this.form.get('titulo')?.value;
    const editora = this.form.get('editora')?.value;
    const status = this.form.get('status')?.value;
    const nota = this.form.get('nota')?.value;
    const autorNome = this.form.get('autorNome')?.value;
    const autorGenero = this.form.get('autorGenero')?.value;
    const paisOrigemAutor = this.form.get('paisOrigemAutor')?.value;

    this.listagem$ = this.service.listrar({
      page: this.paginaAtual,
      size: this.tamanhoPagina,
      ano: ano || undefined,
      genero: genero || undefined,
      autorId: autorId || undefined,
      idioma: idioma || undefined,
      formato: formato || undefined,
      titulo: titulo || undefined,
      editora: editora || undefined,
      status: status || undefined,
      nota: nota || undefined,
      autorNome: autorNome || undefined,
      autorGenero: autorGenero || undefined,
      paisOrigemAutor: paisOrigemAutor || undefined      
    });
  }

  aplicarFiltros(){
    console.log(this.form.value);
    this.paginaAtual = 0;
    this.listarLivros();
  }

  limparFiltros(){
    this.form.reset();
    this.aplicarFiltros();
  }

  prepararEdicao(idLivro: string){
    this.router.navigate(['/paginas/cadastro-livros'], {
      queryParams: {
        id: idLivro
      }
    });
  }

  navegar(pagina: number) : void {
    this.paginaAtual = pagina;
    this.listarLivros();
  }

  navegarProximo(listagem: PageResult<DetalhesLivros>): void {
    if(!listagem.last){
      this.navegar(listagem.number + 1);
    }
  }

  navegarAnterior(listagem: PageResult<DetalhesLivros>): void{
    if(!listagem.first){
      this.navegar(listagem.number - 1);
    }
  }

  paginas(totalPages: number): number[]{
    return Array.from( { length: totalPages } , (_value, index) => index);
  }

  registroInicial(listagem: PageResult<DetalhesLivros>): number {
    if(listagem.totalElements === 0){
      return 0;
    }

    return listagem.number * listagem.size +1;
  }

  registroFinal(listagem: PageResult<DetalhesLivros>): number {
    if(listagem.totalElements === 0){
      return 0;
    }

    return Math.min( (listagem.number + 1) * listagem.size, listagem.totalElements);
  }

}
