import { Routes } from '@angular/router';
import { Template } from './template/template';
import { CadastroAutor } from './autores/cadastro-autor/cadastro-autor';
import { ListagemAutores } from './autores/listagem-autores/listagem-autores';
import { CadastroLivro } from './livros/cadastro-livro/cadastro-livro';
import { ListagemLivros } from './livros/listagem-livros/listagem-livros';
import { DashboardPage } from './dashboard/dashboard-page/dashboard-page';
import { Login } from './auth/login/login';
import { authGuard } from './auth/auth-guard';

export const routes: Routes = [
    {
        path: 'login',
        component: Login
    },
    {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
    },
    {
        path: 'paginas',
        component: Template,
        canActivate:[authGuard],
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
