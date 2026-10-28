package br.com.fiapbank.credito.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class UsuarioJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String login;

    @Column(name = "senha_hash", nullable = false, length = 100)
    private String senhaHash;

    @Column(nullable = false, length = 11)
    private String cpf;

    @Column(name = "numero_conta", nullable = false, length = 20)
    private String numeroConta;

    @Column(nullable = false, length = 20)
    private String roles;

    protected UsuarioJpaEntity() {
    }

    public UsuarioJpaEntity(String login, String senhaHash, String cpf, String numeroConta, String roles) {
        this.login = login;
        this.senhaHash = senhaHash;
        this.cpf = cpf;
        this.numeroConta = numeroConta;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public String getCpf() {
        return cpf;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public String getRoles() {
        return roles;
    }
}
