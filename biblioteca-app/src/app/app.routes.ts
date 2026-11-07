import { Routes } from '@angular/router';
import { Template } from './template/template';
import { CadastroAutor } from './autores/cadastro-autor/cadastro-autor';

export const routes: Routes = [
    {
        path: 'paginas',
        component: Template,
        children: [
            {
                path: 'cadastro-autores',
                component: CadastroAutor
            }
        ]
    }
];
