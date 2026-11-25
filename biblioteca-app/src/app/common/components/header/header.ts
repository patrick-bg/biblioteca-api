import { Component, inject, input, OnInit } from '@angular/core';
import { AuthService } from '../../../auth/auth-service';
import { ThemeService } from '../theme-service';
import { CommonModule, NgClass } from '@angular/common';


@Component({
  selector: 'app-header',
  imports: [NgClass, CommonModule],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header implements OnInit{
  titulo = input.required<string>();
  authService = inject(AuthService);
  nomeUsuarioLogado = 'Usuário';
  themeService = inject(ThemeService);

  ngOnInit(): void {
    this.nomeUsuarioLogado = this.authService.getNomeUsuarioLogado() ?? 'Usuário';
  }
}
