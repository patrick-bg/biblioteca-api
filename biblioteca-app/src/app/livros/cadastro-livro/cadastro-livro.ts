import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators, ReactiveFormsModule, ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';
import { DadosLivroForm, StatusLivro } from '../dados-livros';
import { ToastrService } from 'ngx-toastr';
import { LivroService } from '../livro-service';
import { DetalhesAutor } from '../../autores/dados-autor';
import { forkJoin, Observable } from 'rxjs';
import { Header } from '../../common/components/header/header';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NgxMaskDirective } from 'ngx-mask';
import { ValidationErrorResponse } from '../../common/validation/validation-error-model';

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
  urlCapa: FormControl<string | null>;
}

@Component({
  selector: 'app-cadastro-livro',
  imports: [Header, ReactiveFormsModule, CommonModule, NgxMaskDirective],
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
      preco: new FormControl<string>('', {nonNullable: true, validators: [Validators.required, Validators.min(1)]}),
      autorId: new FormControl<string>('', {nonNullable: true, validators: Validators.required}),
      idioma: new FormControl<string>('', {nonNullable: true, validators: Validators.required}),
      formato: new FormControl(<string>'', {nonNullable: true, validators: Validators.required}),
      editora: new FormControl<string>('', {nonNullable: true, validators: Validators.required}),
      dataPublicacao: new FormControl<string>('', {nonNullable: true, validators: [Validators.required, dataPublicacaoValidator({permitirFutura:false, anoMinimo: 0o1})]}),
      paginasTotais: new FormControl<number>(0, {nonNullable: true, validators: [Validators.required, Validators.min(1)]}),
      status: new FormControl<StatusLivro | ''>('', {nonNullable: true, validators: Validators.required}),
      paginasLidas: new FormControl<number>(0, {nonNullable: true, validators: Validators.min(1)} ),
      nota: new FormControl<number | null>(null, {nonNullable: true, validators: [Validators.min(0), Validators.max(5), Validators.pattern(/^\d+(\.\d{1})?$/), Validators.required ]}),
      urlCapa: new FormControl<string>('', {nonNullable: true}),
    },
    {validators: paginasValidator });

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

  isLivroBacklog(): boolean{
    return this.form.controls.status.value === 'BACKLOG'
  }

  handleStatusChange(): void{
    if(!this.isLeituraFinalizada()){
      this.form.controls.paginasLidas.setValue(this.form.controls.paginasTotais.value);
      this.form.controls.nota.setValue(null);
      this.form.controls.nota.setErrors(null);
    }

    if(this.isLeituraFinalizada()){
      this.form.controls.paginasLidas.setValue(this.form.controls.paginasTotais.value);
      this.form.controls.paginasLidas.setErrors(null);
      this.form.controls.nota.setErrors(null);
    }

    if(this.isLivroBacklog()){
      this.form.controls.paginasLidas.setValue(0);
      this.form.controls.nota.setValue(null);
      this.form.controls.nota.setErrors(null);
    }
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
      return;
    }

    const dados = this.form.value as DadosLivroForm;
    dados.preco = this.parsePreco(dados.preco);

    this.service.criar(dados).subscribe({
      next: () => {
        this.toast.success('Livro cadastrado com sucesso!');
        this.form.reset();
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
