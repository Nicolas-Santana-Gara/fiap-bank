import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest, LoginResponse } from '../models/solicitacao.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private readonly http: HttpClient) {}

  login(credenciais: LoginRequest): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${environment.apiUrl}/api/auth/login`, credenciais)
      .pipe(tap((resposta) => localStorage.setItem('access_token', resposta.accessToken)));
  }

  logout(): void {
    localStorage.removeItem('access_token');
  }

  estaAutenticado(): boolean {
    return !!localStorage.getItem('access_token');
  }
}
