import { Component, inject, OnInit } from "@angular/core";
import { FormBuilder, FormControl, ReactiveFormsModule } from "@angular/forms";
import { BuscaLivro, BuscaLivroService } from "./busca-livro-service";
import { catchError, debounceTime, distinctUntilChanged, filter, of, switchMap } from "rxjs";

@Component({
  selector: 'app-cadastro-livro',
  standalone: true,
  imports: [ReactiveFormsModule],
  template: `
    <div>
      <label>Pesquisar livro (Título ou ISBN):</label>
      <input [formControl]="campoPesquisa" placeholder="Ex: Dom Casmurro ou 9788535914849" />

      @if (resultados.length > 0) {
        <ul>
          @for (livro of resultados; track livro.isbn) {
            <li (click)="selecionarLivro(livro)" style="cursor: pointer;">
              <img [src]="livro.urlCapa || 'assets/sem-capa.png'" width="40" />
              <strong>{{ livro.titulo }}</strong> - {{ livro.autores.join(', ') }} (ISBN: {{ livro.isbn }})
            </li>
          }
        </ul>
      }
    </div>

    <form [formGroup]="formCadastro">
      <input formControlName="titulo" placeholder="Título" />
      <input formControlName="autor" placeholder="Autor" />
      <input formControlName="isbn" placeholder="ISBN" />
      <input formControlName="capaUrl" placeholder="URL da Capa" />
    </form>
  `
})
export class CadastroLivroComponent implements OnInit {
  private fb = inject(FormBuilder);
  private buscaService = inject(BuscaLivroService);

  campoPesquisa = new FormControl('');
  resultados: BuscaLivro[] = [];

  formCadastro = this.fb.group({
    titulo: [''],
    autor: [''],
    isbn: [''],
    capaUrl: ['']
  });

  ngOnInit() {
    this.campoPesquisa.valueChanges.pipe(
      debounceTime(400),
      distinctUntilChanged(),
      filter(termo => !!termo && termo.trim().length >= 3),
      switchMap(termo => this.buscaService.buscar(termo!).pipe(
        catchError(() => of([]))
      ))
    ).subscribe(dados => {
      this.resultados = dados;
    });
  }

  selecionarLivro(livro: BuscaLivro) {
    this.formCadastro.patchValue({
      titulo: livro.titulo,
      autor: livro.autores.join(', '),
      isbn: livro.isbn,
      capaUrl: livro.urlCapa
    });
    this.resultados = []; 
  }
}