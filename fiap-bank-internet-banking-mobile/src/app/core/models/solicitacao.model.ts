export interface SolicitarCreditoRequest {
  numeroConta: string;
  valor: number;
  prazoMeses: number;
  finalidade: string;
}

export interface SolicitacaoResponse {
  id: number;
  cpfCliente: string;
  numeroConta: string;
  valor: number;
  prazoMeses: number;
  finalidade: string;
  status: 'PENDENTE' | 'APROVADA' | 'REPROVADA';
  operadorResponsavel: string | null;
  dataCriacao: string;
  dataAtualizacao: string;
}

export interface LoginRequest {
  login: string;
  senha: string;
}

export interface LoginResponse {
  accessToken: string;
  tokenType: string;
  expiresIn: number;
}
