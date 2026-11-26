import { Component, inject, OnInit } from '@angular/core';
import { DashboardService } from '../dashboard-service';
import { BehaviorSubject, catchError, Observable, of, switchMap } from 'rxjs';
import { Dashboard } from '../dados-dashboard';
import { Header } from '../../common/components/header/header';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { EnumLabelPipe } from '../../common/components/enum-label.pipe';

@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  imports: [Header, CommonModule, FormsModule, EnumLabelPipe],
  templateUrl: './dashboard-page.html',
  styleUrl: './dashboard-page.scss',
})
export class DashboardPage implements OnInit{
  service = inject(DashboardService);
  router = inject(Router)
  anoSelecionado: number = new Date().getFullYear();
  anosDisponiveis: number[] = [];
  erro: string | null = null;

  dashboard$!: Observable<Dashboard | null >;
  private anoSubject = new BehaviorSubject<number>(this.anoSelecionado);

  constructor() {
    const anoAtual = new Date().getFullYear();
    for(let i = 0; i <=11; i++) {
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

  percentual(valor: number, total: number){
    if(!total){
      return 0;
    }

    const resultado = (valor / total) * 100;
    return Math.round(resultado);
  }

  prepararEdicao(idLivro: string){
    
    if(!idLivro) return;
    this.router.navigate(['/paginas/cadastro-livros'], {
      queryParams: {
        id: idLivro
      }
    });
  }

  calcularPercentualBarra(quantidade: number, lista: { livrosLidos?: number; quantidade?: number }[]): number {
    if (!lista || lista.length === 0) return 0;

    // Pega o maior valor (que é o primeiro item da lista ordenada)
    const maximo = lista[0].livrosLidos ?? lista[0].quantidade ?? 1;
    if (maximo === 0) return 0;

    return Math.round((quantidade / maximo) * 100);
  }
  
}
