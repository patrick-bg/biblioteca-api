import { Component, inject, OnInit } from '@angular/core';
import { DashboardService } from '../dashboard-service';
import { BehaviorSubject, catchError, Observable, of, switchMap } from 'rxjs';
import { Dashboard } from '../dados-dashboard';
import { Header } from '../../common/components/header/header';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-dashboard-page',
  imports: [Header, CommonModule, FormsModule],
  templateUrl: './dashboard-page.html',
  styleUrl: './dashboard-page.scss',
})
export class DashboardPage implements OnInit{
  service = inject(DashboardService);
  
  anoSelecionado: number = new Date().getFullYear();
  anosDisponiveis: number[] = [];
  erro: string | null = null;

  dashboard$!: Observable<Dashboard | null >;
  private anoSubject = new BehaviorSubject<number>(this.anoSelecionado);

  constructor() {
    const anoAtual = new Date().getFullYear();
    for(let i = 0; i <=5; i++) {
      this.anosDisponiveis.push(anoAtual - i);
    }
  }


  ngOnInit(): void {
    this.dashboard$ = this.anoSubject.pipe(
      switchMap((ano) => {
        this.erro = null;
        return this.service.obterDashboardPorAno(ano).pipe(
          catchError((err) => {
            console.error('Erro ao carregar dashboard:', err);
            this.erro = err.error?.erro || 'Erro ao carregar os dados do dashboard.';
            return of(null);
          })
        );
      })
    );
  }

  aoMudarAno(novoAno: number): void {
    this.anoSelecionado = Number(novoAno);
    this.anoSubject.next(this.anoSelecionado);
  }
  
}
