import { Component, OnInit, inject} from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AutorService } from '../autor-service';
import { DadosAutorForm, DetalhesAutor } from '../dados-autor';
import { ValidationErrorResponse } from '../../common/validation/validation-error-model';
import { CommonModule } from '@angular/common';
import { retry } from 'rxjs';

interface CadastroAutorForm{
  nome: FormControl<string>;
  dataNascimento: FormControl<string>;
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
  service = inject(AutorService);

  ngOnInit(): void {
    this.form = new FormGroup<CadastroAutorForm>({
      nome: new FormControl<string>('', {nonNullable: true, validators: Validators.required }),
      dataNascimento: new FormControl<string>('', {nonNullable: true, validators: Validators.required }),
      generoAutor: new FormControl<string> ('', {nonNullable: true, validators: Validators.required}),
      paisOrigem: new FormControl<string>('', {nonNullable: true, validators: Validators.required})
    });
  }

  isFormInvalid() : boolean{
    if(this.form.invalid){
      this.form.markAllAsTouched();
      return true;
    }
    return false;
  }

  handleSubmit(){
    if(this.isFormInvalid()){
      return;
    }

    console.log(this.form.value);
    const dadosAutor = this.form.value as DadosAutorForm;
    this.service.criar(dadosAutor).subscribe({
      next: (response: DetalhesAutor) =>{
        console.log('recebendo a resposta do servidor', response);
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
      this.aplicarErrosValidacao(response.error)
      return;
    }
  }
  
}
