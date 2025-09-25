package com.edutec.app.config;

import com.edutec.app.domain.entity.Usuario;
import com.edutec.app.repository.UsuarioRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile({"default","prod","dev"}) // corre en tus perfiles habituales
public class PasswordMigrationRunner implements CommandLineRunner {

    private final UsuarioRepo usuarioRepo;
    private final PasswordEncoder encoder;

    public PasswordMigrationRunner(UsuarioRepo usuarioRepo, PasswordEncoder encoder) {
        this.usuarioRepo = usuarioRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        int updated = 0;
        for (Usuario u : usuarioRepo.findAll()) {
            String stored = u.getContrasena();
            if (stored == null || stored.isBlank()) continue;

            // Si NO parece BCrypt, lo re-hasheamos a BCrypt
            boolean isBcrypt = stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$");
            if (!isBcrypt) {
                u.setContrasena(encoder.encode(stored));
                usuarioRepo.save(u);
                updated++;
            }
        }
        if (updated > 0) {
            System.out.println("[PasswordMigrationRunner] Contraseñas migradas a BCrypt: " + updated);
        } else {
            System.out.println("[PasswordMigrationRunner] No había contraseñas en texto plano que migrar.");
        }
    }
}
