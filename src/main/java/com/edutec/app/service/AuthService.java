package com.edutec.app.service;

import com.edutec.app.auth.dto.TokenDTO;
import com.edutec.app.config.JwtUtil;
import com.edutec.app.domain.entity.Rol;
import com.edutec.app.domain.entity.Usuario;
import com.edutec.app.repository.RolRepo;
import com.edutec.app.repository.UsuarioRepo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final UsuarioRepo usuarios;
    private final RolRepo roles;
    private final PasswordEncoder enc;
    private final JwtUtil jwt;

    @PostConstruct
    void seed() {
        // Asegurar rol ADMIN
        Rol rolAdmin = roles.findByNombre("ADMIN").orElseGet(() -> {
            var r = new Rol();
            r.setNombre("ADMIN");
            r.setDescripcion("Administrador del sistema");
            return roles.save(r);
        });

        // Asegurar usuario admin
        usuarios.findByCorreo("admin@edutec.cl").orElseGet(() -> {
            var u = new Usuario();
            u.setNombre("Administrador");
            u.setCorreo("admin@edutec.cl");
            u.setContrasena(enc.encode("1234")); // inicial en BCrypt
            u.setRol(rolAdmin);
            return usuarios.save(u);
        });
    }

    public TokenDTO autenticar(String correo, String contrasena) {
        String correoNorm = correo == null ? null : correo.trim().toLowerCase();

        Usuario u = usuarios.findByCorreo(correoNorm)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String almacenada = u.getContrasena();
        boolean esBcrypt = almacenada != null &&
                (almacenada.startsWith("$2a$") || almacenada.startsWith("$2b$") || almacenada.startsWith("$2y$"));

        if (esBcrypt) {
            if (!enc.matches(contrasena, almacenada)) {
                throw new BadCredentialsException("Credenciales inválidas");
            }
        } else {
            if (!contrasena.equals(almacenada)) {
                throw new BadCredentialsException("Credenciales inválidas");
            }
            u.setContrasena(enc.encode(contrasena));
            usuarios.save(u);
        }

        return new TokenDTO(jwt.generate(u.getCorreo()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = usuarios.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("No existe"));
        String rol = "ROLE_" + u.getRol().getNombre();
        return new User(u.getCorreo(), u.getContrasena(), List.of(new SimpleGrantedAuthority(rol)));
    }
}
