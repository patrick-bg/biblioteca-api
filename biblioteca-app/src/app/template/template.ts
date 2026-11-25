import { Component, inject } from '@angular/core';
import { RouterLinkActive, RouterModule, RouterOutlet } from '@angular/router';
import { AuthService } from '../auth/auth-service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-template',
  imports: [RouterOutlet, RouterModule, RouterLinkActive, CommonModule],
  templateUrl: './template.html',
  styleUrl: './template.scss',
})
export class Template {
  authService = inject(AuthService);

  sair(){
    this.authService.logout(true);
  }
}
