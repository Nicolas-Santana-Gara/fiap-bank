package br.com.fiapbank.credito.presentation.security;

import br.com.fiapbank.credito.infrastructure.persistence.UsuarioJpaEntity;
import br.com.fiapbank.credito.infrastructure.persistence.UsuarioJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioSeeder implements CommandLineRunner {

    private final UsuarioJpaRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioSeeder(UsuarioJpaRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            return;
        }

        usuarioRepository.save(new UsuarioJpaEntity(
                "35211022087", passwordEncoder.encode("credito123"), "35211022087", "00112233-4", "CLIENTE"));
        usuarioRepository.save(new UsuarioJpaEntity(
                "operador.mesa", passwordEncoder.encode("mesa123"), "00000000000", "-", "OPERADOR"));
        usuarioRepository.save(new UsuarioJpaEntity(
                "admin.credito", passwordEncoder.encode("admin123"), "00000000001", "-", "ADMIN"));
    }
}
