import { Component, inject, OnInit } from '@angular/core';
import { DashboardService } from '../dashboard-service';
import { Observable } from 'rxjs';
import { Dashboard } from '../dados-dashboard';

@Component({
  selector: 'app-dashboard-page',
  imports: [],
  templateUrl: './dashboard-page.html',
  styleUrl: './dashboard-page.scss',
})
export class DashboardPage implements OnInit{
  service = inject(DashboardService);
  dashboard$!: Observable<Dashboard>;

  ngOnInit(): void {
    this.dashboard$ = this.service.obterDashboardPorAno();
  }
}
