import { Routes } from '@angular/router';
import { Template } from './template/template';
import { CadastroAutor } from './autores/cadastro-autor/cadastro-autor';
import { ListagemAutores } from './autores/listagem-autores/listagem-autores';
import { CadastroLivro } from './livros/cadastro-livro/cadastro-livro';

export const routes: Routes = [
    {
        path: 'paginas',
        component: Template,
        children: [
            {
                path: 'cadastro-autores',
                component: CadastroAutor
            },
            {
                path: 'listagem-autores',
                component: ListagemAutores
            },
            {
                path: 'cadastro-livros',
                component: CadastroLivro
            }
        ]
    }
];
