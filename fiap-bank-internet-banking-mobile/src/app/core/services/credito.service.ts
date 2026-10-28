import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { SolicitacaoResponse, SolicitarCreditoRequest } from '../models/solicitacao.model';

@Injectable({ providedIn: 'root' })
export class CreditoService {
  constructor(private readonly http: HttpClient) {}

  solicitar(request: SolicitarCreditoRequest): Observable<SolicitacaoResponse> {
    return this.http.post<SolicitacaoResponse>(`${environment.apiUrl}/api/v1/solicitacoes`, request);
  }

  minhasSolicitacoes(): Observable<SolicitacaoResponse[]> {
    return this.http.get<SolicitacaoResponse[]>(`${environment.apiUrl}/api/v1/solicitacoes/minhas`);
  }
}
