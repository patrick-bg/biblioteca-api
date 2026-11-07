import { Component, OnInit, inject} from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AutorService } from '../autor-service';
import { DadosAutorForm, DetalhesAutor } from '../dados-autor';

interface CadastroAutorForm{
  nome: FormControl<string>;
  dataNascimento: FormControl<string>;
  generoAutor: FormControl<string>;
  paisOrigem: FormControl<string>;
}

@Component({
  selector: 'app-cadastro-autor',
  imports: [ ReactiveFormsModule ],
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

  handleSubmit(){
    console.log(this.form.value);
    const dadosAutor = this.form.value as DadosAutorForm;
    this.service.criar(dadosAutor).subscribe({
      next: (response: DetalhesAutor) =>{
        console.log('recebendo a resposta do servidor', response);
      },
      error: (error) => console.log('ocorreu um erro: ', error)
    });
  }
}
