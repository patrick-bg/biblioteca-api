package io.github.patrickbg.bibliotecaapi.dominio.usuario.dto;

public record AuthResponse(
        String token,
        String tipo,
        long expiraEm,
        String nome
) {
}
