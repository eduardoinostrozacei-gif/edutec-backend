package com.edutec.app.auth;

import com.edutec.app.auth.dto.LoginRequest;
import com.edutec.app.auth.dto.TokenDTO;
import com.edutec.app.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService svc;

    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginRequest req){
        String correo = req.getCorreo() == null ? null : req.getCorreo().trim().toLowerCase();
        String pass   = req.getContrasena();
        return svc.autenticar(correo, pass);
    }

    // Para que el cliente reciba 401 (y no 403) cuando falla login
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public Object onAuthErrors(RuntimeException ex){
        return new Object() { public final String error = "unauthorized"; public final String message = ex.getMessage(); };
    }
}
