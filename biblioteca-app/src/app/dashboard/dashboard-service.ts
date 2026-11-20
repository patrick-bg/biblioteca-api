import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Dashboard } from './dados-dashboard';

@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  http = inject(HttpClient);
  baseUrl = 'http://localhost:8080/dashboard';

  obterDashboardPorAno(year?: number) : Observable<Dashboard> {
    let params = new HttpParams();

    if(year){
      params = params.set('ano', year.toString());
    }

    return this.http.get<Dashboard>(this.baseUrl, {params});
  }
}
