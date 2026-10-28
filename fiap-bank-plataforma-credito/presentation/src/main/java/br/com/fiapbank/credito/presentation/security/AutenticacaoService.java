package br.com.fiapbank.credito.presentation.security;

import br.com.fiapbank.credito.infrastructure.persistence.UsuarioJpaEntity;
import br.com.fiapbank.credito.infrastructure.persistence.UsuarioJpaRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AutenticacaoService {

    private final UsuarioJpaRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AutenticacaoService(UsuarioJpaRepository usuarioRepository, PasswordEncoder passwordEncoder,
                                TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public String autenticar(String login, String senha) {
        UsuarioJpaEntity usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new BadCredentialsException("Login ou senha invalidos."));

        if (!passwordEncoder.matches(senha, usuario.getSenhaHash())) {
            throw new BadCredentialsException("Login ou senha invalidos.");
        }

        List<String> roles = Arrays.asList(usuario.getRoles().split(","));
        return tokenService.gerarToken(usuario.getCpf(), usuario.getNumeroConta(), roles);
    }
}
