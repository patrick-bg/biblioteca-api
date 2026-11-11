import { Component, OnInit, inject} from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AutorService } from '../autor-service';
import { DadosAutorForm, DetalhesAutor, StatusAutor } from '../dados-autor';
import { ValidationErrorResponse } from '../../common/validation/validation-error-model';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

interface CadastroAutorForm{
  nome: FormControl<string>;
  statusAutor: FormControl<StatusAutor | ''>;
  dataNascimento: FormControl<string>;
  dataFalecimento: FormControl<string>;
  generoAutor: FormControl<string>;
  paisOrigem: FormControl<string>;
}

@Component({
  selector: 'app-cadastro-autor',
  imports: [ ReactiveFormsModule, CommonModule ],
  templateUrl: './cadastro-autor.html',
  styleUrl: './cadastro-autor.scss',
})
export class CadastroAutor implements OnInit {

  form!: FormGroup<CadastroAutorForm>;
  rotaAtiva = inject (ActivatedRoute);
  service = inject(AutorService);
  toast = inject(ToastrService);
  idAutorEdicao?: string | null;

  ngOnInit(): void {
    this.form = new FormGroup<CadastroAutorForm>({
      nome: new FormControl<string>('', {nonNullable: true, validators: Validators.required }),
      statusAutor: new FormControl<StatusAutor | ''>('', {nonNullable: true, validators: Validators.required}),
      dataNascimento: new FormControl<string>('', {nonNullable: true, validators: Validators.required }),
      dataFalecimento: new FormControl<string>('', {nonNullable: true}),
      generoAutor: new FormControl<string> ('', {nonNullable: true, validators: Validators.required}),
      paisOrigem: new FormControl<string>('', {nonNullable: true, validators: Validators.required})
    });

    this.carregarDadosParaEdicao();
  }

  carregarDadosParaEdicao(){
    this.idAutorEdicao = this.rotaAtiva.snapshot.queryParamMap.get('id')

    if(!this.idAutorEdicao){
      return;
    }

    this.service.obterPorId(this.idAutorEdicao).subscribe({
      next: (autor) => {
        this.form.patchValue({
          nome: autor.nome,
          statusAutor: autor.statusAutor as "" | StatusAutor,
          dataNascimento: autor.dataNascimento ? new Date(autor.dataNascimento).toISOString().split('T')[0] : '',
          dataFalecimento: autor.dataFalecimento ? new Date(autor.dataFalecimento).toISOString().split('T')[0] : '',
          generoAutor: autor.generoAutor,
          paisOrigem: autor.paisOrigem
        });
      },
      error: () => this.toast.error('Erro ao carregar os dados do autor.')
    });
  }

  isFalecido() : boolean {
    return this.form.controls.statusAutor.value === 'FALECIDO';
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
    const dadosAutor = this.form.value as DadosAutorForm;

    const requisicao : Observable<DetalhesAutor | void > = this.idAutorEdicao ?
      this.service.atualizar(this.idAutorEdicao,dadosAutor) : 
      this.service.criar(dadosAutor);
    
    requisicao.subscribe({
      next: (response) =>{
        this.toast.success('Autor cadastrado/atualizado com sucesso.')
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
  
}
