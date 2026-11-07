import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

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
  }
}
