import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CreditoService } from '../../core/services/credito.service';

@Component({
  selector: 'app-solicitar-credito',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './solicitar-credito.component.html',
  styleUrl: './solicitar-credito.component.css'
})
export class SolicitarCreditoComponent {
  numeroConta = '';
  valor: number | null = null;
  prazoMeses: number | null = null;
  finalidade = '';
  enviando = false;
  erro = '';
  sucesso = false;

  constructor(
    private readonly creditoService: CreditoService,
    private readonly router: Router
  ) {}

  enviar(): void {
    if (!this.valor || !this.prazoMeses) {
      return;
    }
    this.erro = '';
    this.enviando = true;
    this.creditoService
      .solicitar({
        numeroConta: this.numeroConta,
        valor: this.valor,
        prazoMeses: this.prazoMeses,
        finalidade: this.finalidade
      })
      .subscribe({
        next: () => {
          this.enviando = false;
          this.sucesso = true;
          setTimeout(() => this.router.navigate(['/minhas-solicitacoes']), 1200);
        },
        error: (err) => {
          this.enviando = false;
          this.erro = err?.error?.mensagem ?? 'Não foi possível enviar a solicitação.';
        }
      });
  }
}
