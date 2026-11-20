import { Routes } from '@angular/router';
import { Template } from './template/template';
import { CadastroAutor } from './autores/cadastro-autor/cadastro-autor';
import { ListagemAutores } from './autores/listagem-autores/listagem-autores';
import { CadastroLivro } from './livros/cadastro-livro/cadastro-livro';
import { ListagemLivros } from './livros/listagem-livros/listagem-livros';
import { DashboardPage } from './dashboard/dashboard-page/dashboard-page';

export const routes: Routes = [
    {
        path: 'paginas',
        component: Template,
        children: [
            {
                path: '',
                redirectTo: 'dashboard',
                pathMatch: 'full'
            },
            {
                path: 'dashboard',
                component: DashboardPage
            },
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
            },
            {
                path: 'listagem-livros',
                component: ListagemLivros
            }
        ]
    }
];
