import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators, ReactiveFormsModule, ValidatorFn, AbstractControl, ValidationErrors, FormBuilder } from '@angular/forms';
import { DadosLivroForm, DetalhesLivros, LivroBuscaExterna, StatusLivro } from '../dados-livros';
import { ToastrService } from 'ngx-toastr';
import { LivroService } from '../livro-service';
import { DetalhesAutor } from '../../autores/dados-autor';
import { forkJoin, Observable } from 'rxjs';
import { Header } from '../../common/components/header/header';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NgxMaskDirective } from 'ngx-mask';
import { ValidationErrorResponse } from '../../common/validation/validation-error-model';
import { RouterLink } from '@angular/router';
import { BuscaLivro } from '../busca-livro/busca-livro-service';

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
  nota: FormControl<number | null>;
  lidoEm: FormControl<string>;
  urlCapa: FormControl<string | null>;
}

@Component({
  selector: 'app-cadastro-livro',
  imports: [Header, ReactiveFormsModule, CommonModule, NgxMaskDirective, RouterLink, RouterModule],
  templateUrl: './cadastro-livro.html',
  styleUrl: './cadastro-livro.scss',
})

export class CadastroLivro implements OnInit {
  form!: FormGroup<CadastroLivroForm>;
  toast = inject(ToastrService);
  rotaAtiva = inject (ActivatedRoute);
  service = inject(LivroService);
  idLivroEdicao?: string | null;
  autoresDisponiveis: DetalhesAutor[] = [];
  mesAtualMax: string;
  campoPesquisa = new FormControl('');
  resultados: any[] = [];
  resultadosBuscaExterna: LivroBuscaExterna[] = [];
  buscandoExterno = false;
  

  constructor(private fb: FormBuilder){
    const hoje = new Date();
    const ano = hoje.getFullYear();
    const mes = String(hoje.getMonth() + 1).padStart(2, '0');
    this.mesAtualMax = `${ano}-${mes}`;
  }

  ngOnInit(): void {
    this.form = new FormGroup<CadastroLivroForm>({
      titulo: new FormControl<string>('', {nonNullable: true, validators: Validators.required}),
      isbn: new FormControl<string>('', {nonNullable: true, validators: Validators.required}),
      genero: new FormControl<string>('', {nonNullable: true, validators: Validators.required}),
      preco: new FormControl<string>('', {nonNullable: true, validators: [Validators.required, Validators.min(1)]}),
      autorId: new FormControl<string>('', {nonNullable: true, validators: Validators.required}),
      idioma: new FormControl<string>('', {nonNullable: true, validators: Validators.required}),
      formato: new FormControl<string>('', {nonNullable: true, validators: Validators.required}),
      editora: new FormControl<string>('', {nonNullable: true, validators: Validators.required}),
      dataPublicacao: new FormControl<string>('', {nonNullable: true, validators: [Validators.required, dataPublicacaoValidator({permitirFutura:false, anoMinimo: 1})]}),
      paginasTotais: new FormControl<number>(0, {nonNullable: true, validators: [Validators.required, Validators.min(1)]}),
      status: new FormControl<StatusLivro | ''>('', {nonNullable: true, validators: Validators.required}),
      paginasLidas: new FormControl<number>(0, {nonNullable: true, validators: [Validators.min(0)]} ),
      nota: new FormControl<number | null>(null, { validators: [Validators.min(0), Validators.max(5), Validators.pattern(/^\d+(\.\d{1})?$/) ]}),
      lidoEm: new FormControl<string>('', {nonNullable: true, validators: [dataLeituraValidator()]}), 
      urlCapa: new FormControl<string>('', {nonNullable: true}),
    },
    {validators: [paginasValidator, ordemDatasLivroValidator('dataPublicacao', 'lidoEm')] });

    this.inicializarDropDowns();
    this.carregarDadosParaEdicao();
  }

  selecionarLivro(livro: BuscaLivro) {
      this.form.patchValue({
        titulo: livro.titulo,
        isbn: livro.isbn,
        urlCapa: livro.urlCapa
      });
      this.resultados = []; 
    }

  get mesMinimoPublicacao(): string | null {
    const pub = this.form.get('dataPublicacao')?.value;
    return pub && pub.length >= 7 ? pub.substring(0,7) : null;
  }

  carregarDadosParaEdicao(){
    this.idLivroEdicao = this.rotaAtiva.snapshot.queryParamMap.get('id');

    if(!this.idLivroEdicao){
      return;
    }

    this.service.obterPorId(this.idLivroEdicao).subscribe({
      next: (livro) => {
        this.form.patchValue({
          titulo: livro.titulo,
          isbn: livro.isbn,
          genero: livro.genero,
          preco: livro.preco,
          autorId: livro.autorId,
          idioma: livro.idioma,
          formato: livro.formato,
          editora: livro.editora,
          dataPublicacao: livro.dataPublicacao ? new Date(livro.dataPublicacao).toISOString().split('T')[0] : '',
          paginasTotais: livro.paginasTotais,
          status: livro.status as "" | StatusLivro,
          paginasLidas: livro.paginasLidas,
          nota: livro.nota,
          lidoEm: livro.lidoEm,
          urlCapa: livro.urlCapa
        });
        this.atualizarValidadoresPorStatus();
      },
      error: () => this.toast.error('Erro ao carregar os dados do livro.')
    });
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

  isLivroBacklog(): boolean{
    return this.form.controls.status.value === 'BACKLOG'
  }

  handleStatusChange(): void{
    const { paginasLidas, paginasTotais, nota } = this.form.controls;

  if (this.isLeituraFinalizada()) {
    paginasLidas.setValue(paginasTotais.value);
  } else if (this.isLivroBacklog()) {
    paginasLidas.setValue(0);
    nota.setValue(null);
  } else if (this.isLeituraIniciada()) {
    if (paginasLidas.value === paginasTotais.value) {
      paginasLidas.setValue(0);
    }
    nota.setValue(null);
  }

  this.atualizarValidadoresPorStatus();
}

  private atualizarValidadoresPorStatus(): void {
  const { paginasLidas } = this.form.controls;

  if (this.isLeituraIniciada()) {
    paginasLidas.setValidators([Validators.required, Validators.min(1)]);
  } else {
    paginasLidas.clearValidators();
  }

  paginasLidas.updateValueAndValidity();
}

  isFormInvalid() : boolean{
    if(this.form.invalid){
      this.form.markAllAsTouched();
      this.toast.error('Erro de validação. Verifique os valores informados.');
      return true;
    }
    return false;
  }

  handleSubmit(){
    if(this.isFormInvalid()){
      console.log('Form inválido');
      return;
    }

    const dados = this.form.value as DadosLivroForm;
    dados.preco = this.parsePreco(dados.preco);

    const requisicao : Observable<DetalhesLivros | void> = this.idLivroEdicao ?
    this.service.atualizar(this.idLivroEdicao, dados) :
    this.service.criar(dados);

   requisicao.subscribe({
      next: () => {
        this.toast.success('Livro cadastrado com sucesso!');
        this.form.reset();
        this.idLivroEdicao = null;
      },
      error: (error) => this.onApiError(error)
    });
  }

  private aplicarErrosValidacao(error: ValidationErrorResponse){
      error.camposInvalidos.forEach( ci => {
        const control = this.form.get(ci.campo);
        if(control){
          control.setErrors({apiError: ci.erro});
          control.markAsTouched();
        }
      });
    }
  
  private onApiError(response: any) : void{
    if(response.status === 422){
      this.aplicarErrosValidacao(response.error);
      this.toast.error('Erro de validação. Verifique os valores informados.');
      return;
    }
    this.toast.error('Ocorreu um erro ao processar a requisição.');
    console.error(response.error);
  }

  private parsePreco(precoMascarado: string) : string {
    if(!precoMascarado){
      return '';
    }

    return precoMascarado.replace(/\./g, '').replace(',','.');
  }

   buscarDadosPorIsbn(): void {
    const isbn = this.form.controls.isbn.value;
    if (!isbn) {
      this.toast.warning('Informe o ISBN antes de buscar.');
      return;
    }

    this.service.buscarPorIsbnExterno(isbn).subscribe({
      next: (dados) => {
        this.form.patchValue({
          titulo: dados.titulo || this.form.controls.titulo.value,
          isbn: dados.isbn || isbn,
          editora: dados.editora || this.form.controls.editora.value,
          paginasTotais: dados.paginasTotais || this.form.controls.paginasTotais.value,
          urlCapa: dados.urlCapa || this.form.controls.urlCapa.value
        });
        this.toast.success('Dados do livro preenchidos com sucesso!');
      },
      error: () => {
        this.toast.info('Livro não encontrado na base externa. Preencha manualmente.');
      }
    });
  }

  pesquisarEdicoesPorTitulo(): void {
    const titulo = this.form.controls.titulo.value?.trim();
    if (!titulo || titulo.length < 3) {
      this.toast.warning('Digite pelo menos 3 letras do título para buscar.');
      return;
    }

    this.buscandoExterno = true;
    this.resultadosBuscaExterna = [];

    this.service.buscarPorTituloExterno(titulo).subscribe({
      next: (lista) => {
        this.resultadosBuscaExterna = lista;
        this.buscandoExterno = false;
        if (lista.length === 0) {
          this.toast.info('Nenhuma edição encontrada para este título.');
        }
      },
      error: () => {
        this.buscandoExterno = false;
        this.toast.error('Erro ao consultar o catálogo externo.');
      }
    });
  }

  selecionarEdicao(edicao: LivroBuscaExterna): void {
    this.form.patchValue({
      titulo: edicao.titulo || this.form.controls.titulo.value,
      isbn: edicao.isbn || this.form.controls.isbn.value,
      editora: edicao.editora || this.form.controls.editora.value,
      paginasTotais: edicao.paginasTotais || this.form.controls.paginasTotais.value,
      urlCapa: edicao.urlCapa || this.form.controls.urlCapa.value
    });

    // Fecha a lista de resultados após escolher
    this.resultadosBuscaExterna = [];
    this.toast.success('Dados da edição selecionada foram preenchidos!');
  }

}

export const paginasValidator: ValidatorFn = (group: AbstractControl): ValidationErrors | null => {
  const totais = group.get('paginasTotais')?.value;
  const lidas = group.get('paginasLidas')?.value;

  if (totais !== null && lidas !== null && lidas > totais) {
    return { paginasExcedidas: true };
  }
  return null;
};

export function dataPublicacaoValidator(opcoes?: { 
  permitirFutura?: boolean; 
  anoMinimo?: number; 
}): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const valor = control.value;

    if (!valor) {
      return null;
    }

    const regexFormato = /^\d{4}-\d{2}-\d{2}$/;
    if (!regexFormato.test(valor)) {
      return { formatoInvalido: true };
    }

    const [anoStr, mesStr, diaStr] = valor.split('-');
    const ano = Number(anoStr);
    const mes = Number(mesStr);
    const dia = Number(diaStr);

    const anoMin = opcoes?.anoMinimo ?? 1900;
    if (ano < anoMin) {
      return { anoInvalido: { min: anoMin, atual: ano } };
    }

    const dataObjeto = new Date(ano, mes - 1, dia);
    const dataReal =
      dataObjeto.getFullYear() === ano &&
      dataObjeto.getMonth() === mes - 1 &&
      dataObjeto.getDate() === dia;

    if (!dataReal) {
      return { dataInexistente: true };
    }

    if (opcoes?.permitirFutura === false) {
      const hoje = new Date();
      hoje.setHours(0, 0, 0, 0); // Zera as horas para comparar apenas os dias

      if (dataObjeto > hoje) {
        return { dataFutura: true };
      }
    }
    return null; 
  };
}

export function dataLeituraValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const valor = control.value;

    if (!valor) {
      return null;
    }

    const regexFormato = /^\d{4}-\d{2}$/;
    if (!regexFormato.test(valor)) {
      return { formatoInvalido: true };
    }

    const [anoStr, mesStr] = valor.split('-');
    const ano = Number(anoStr);
    const mes = Number(mesStr);

    if (mes < 1 || mes > 12) {
      return { mesInvalido: true };
    }

    const hoje = new Date();
    const anoAtual = hoje.getFullYear();
    const mesAtual = hoje.getMonth() + 1;

    if (ano > anoAtual || (ano === anoAtual && mes > mesAtual)) {
      return { dataFutura: true };
    }

    return null;
  };
}

export function ordemDatasLivroValidator(
  campoPublicacao = 'dataPublicacao',
  campoLeitura = 'lidoEm'
): ValidatorFn {
  return (formGroup: AbstractControl): ValidationErrors | null => {
    const pubControl = formGroup.get(campoPublicacao);
    const leituraControl = formGroup.get(campoLeitura);

    if (!pubControl || !leituraControl) return null;

    const dataPub = pubControl.value;       
    const dataLeitura = leituraControl.value; 

    
    if (!dataPub || !dataLeitura || dataPub.length < 7 || dataLeitura.length < 7) {
      return null;
    }

    const [anoPub, mesPub] = dataPub.split('-').map(Number);
    const [anoLeitura, mesLeitura] = dataLeitura.split('-').map(Number);

    const leituraAntesDaPublicacao =
      anoLeitura < anoPub || (anoLeitura === anoPub && mesLeitura < mesPub);

    if (leituraAntesDaPublicacao) {
      leituraControl.setErrors({ ...leituraControl.errors, leituraAnteriorPublicacao: true });
      return { leituraAnteriorPublicacao: true };
    } else {
      if (leituraControl.hasError('leituraAnteriorPublicacao')) {
        const { leituraAnteriorPublicacao, ...outrosErros } = leituraControl.errors || {};
        leituraControl.setErrors(Object.keys(outrosErros).length ? outrosErros : null);
      }
    }

    return null;
  };
}
