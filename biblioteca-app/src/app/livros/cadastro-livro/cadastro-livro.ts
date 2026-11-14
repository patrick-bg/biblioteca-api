import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { StatusLivro } from '../dados-livros';
import { ToastrService } from 'ngx-toastr';
import { LivroService } from '../livro-service';
import { DetalhesAutor } from '../../autores/dados-autor';
import { forkJoin, Observable } from 'rxjs';
import { Header } from '../../common/components/header/header';
import { RouterLink } from '@angular/router';

interface CadastroLivroForm{
  titulo: FormControl<string>;
  isbn: FormControl<string>;
  genero: FormControl<string>;
  preco: FormControl<string>;
  autorId: FormControl<string>;
  idioma: FormControl<string>;
  formato: FormControl<string>;
  editora: FormControl<string>;
  dataPublicacao: FormControl<string>;
  paginasTotais: FormControl<number>;
  status: FormControl<StatusLivro | ''>;
  paginasLidas: FormControl<number | null>;
  nota: FormControl<string | null>;
  urlCapa: FormControl<string | null>;
}

@Component({
  selector: 'app-cadastro-livro',
  imports: [Header, ReactiveFormsModule],
  templateUrl: './cadastro-livro.html',
  styleUrl: './cadastro-livro.scss',
})
export class CadastroLivro implements OnInit {
  form!: FormGroup<CadastroLivroForm>;
  toast = inject(ToastrService);
  service = inject(LivroService);

  autoresDisponiveis: DetalhesAutor[] = [];

  ngOnInit(): void {
    this.form = new FormGroup<CadastroLivroForm>({
      titulo: new FormControl<string>('', {nonNullable: true, validators: Validators.required}),
      isbn: new FormControl<string>('', {nonNullable: true, validators: Validators.required}),
      genero: new FormControl<string>('', {nonNullable: true, validators: Validators.required}),
      preco: new FormControl<string>('', {nonNullable: true, validators: Validators.required}),
      autorId: new FormControl<string>('', {nonNullable: true, validators: Validators.required}),
      idioma: new FormControl<string>('', {nonNullable: true, validators: Validators.required}),
      formato: new FormControl(<string>'', {nonNullable: true, validators: Validators.required}),
      editora: new FormControl<string>('', {nonNullable: true, validators: Validators.required}),
      dataPublicacao: new FormControl<string>('', {nonNullable: true, validators: Validators.required}),
      paginasTotais: new FormControl<number>(0, {nonNullable: true, validators: Validators.required}),
      status: new FormControl<StatusLivro | ''>('', {nonNullable: true, validators: Validators.required}),
      paginasLidas: new FormControl<number>(0, {nonNullable: true}),
      nota: new FormControl<string>('', {nonNullable: true}),
      urlCapa: new FormControl<string>('', {nonNullable: true}),
    });

    this.inicializarDropDowns();

  }

  inicializarDropDowns(){
    forkJoin({
      autores: this.service.listarAutores()
    }).subscribe({
      next: (resultado) => {
        this.autoresDisponiveis = resultado.autores;
      },
      error: () => this.toast.error('Erro ao carregar autores.')
    });
  }

  isLeituraIniciada(): boolean {
  const statusIniciados = ['LENDO', 'ABANDONADO', 'RELEITURA'];
  const statusAtual = this.form.controls.status.value;
  
  return statusIniciados.includes(statusAtual);
  }

  isLeituraFinalizada(): boolean{
    return this.form.controls.status.value === 'LIDO';
  }

  handleSubmit(){
    console.log(this.form.value);
  }

}
