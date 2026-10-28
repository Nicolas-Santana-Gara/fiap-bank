import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'minhas-solicitacoes' },
  {
    path: 'login',
    loadComponent: () => import('./features/login/login.component').then((m) => m.LoginComponent)
  },
  {
    path: 'solicitar-credito',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/solicitar-credito/solicitar-credito.component').then((m) => m.SolicitarCreditoComponent)
  },
  {
    path: 'minhas-solicitacoes',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/minhas-solicitacoes/minhas-solicitacoes.component').then(
        (m) => m.MinhasSolicitacoesComponent
      )
  },
  { path: '**', redirectTo: 'minhas-solicitacoes' }
];
