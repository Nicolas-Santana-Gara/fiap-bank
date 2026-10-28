import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CreditoService } from '../../core/services/credito.service';
import { SolicitacaoResponse } from '../../core/models/solicitacao.model';

@Component({
  selector: 'app-minhas-solicitacoes',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './minhas-solicitacoes.component.html',
  styleUrl: './minhas-solicitacoes.component.css'
})
export class MinhasSolicitacoesComponent implements OnInit {
  solicitacoes: SolicitacaoResponse[] = [];
  carregando = true;
  erro = '';

  constructor(private readonly creditoService: CreditoService) {}

  ngOnInit(): void {
    this.creditoService.minhasSolicitacoes().subscribe({
      next: (dados) => {
        this.solicitacoes = dados;
        this.carregando = false;
      },
      error: () => {
        this.erro = 'Não foi possível carregar suas propostas.';
        this.carregando = false;
      }
    });
  }
}
