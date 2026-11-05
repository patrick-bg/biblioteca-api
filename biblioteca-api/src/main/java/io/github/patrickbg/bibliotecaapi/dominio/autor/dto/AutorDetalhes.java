package io.github.patrickbg.bibliotecaapi.dominio.autor.dto;

import io.github.patrickbg.bibliotecaapi.dominio.autor.model.GeneroAutor;

import java.time.LocalDate;

public record AutorDetalhes(
        String id,
        String nome,
        LocalDate dataNascimento,
        GeneroAutor generoAutor,
        String paisOrigem
) {
}
