import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  cpf = '';
  senha = '';
  carregando = false;
  erro = '';

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router
  ) {}

  entrar(): void {
    this.erro = '';
    this.carregando = true;
    this.authService.login({ login: this.cpf, senha: this.senha }).subscribe({
      next: () => {
        this.carregando = false;
        this.router.navigate(['/minhas-solicitacoes']);
      },
      error: () => {
        this.carregando = false;
        this.erro = 'CPF ou senha inválidos.';
      }
    });
  }
}
