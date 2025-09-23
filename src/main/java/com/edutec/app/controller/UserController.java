package com.edutec.app.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/me")
    public MeDTO me(Authentication auth) {
        String correo = auth.getName();
        List<String> roles = auth.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList();
        return new MeDTO(correo, roles);
    }

    public record MeDTO(String usuario, List<String> authorities) {}
}
