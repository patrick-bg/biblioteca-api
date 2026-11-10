package io.github.patrickbg.bibliotecaapi.dominio.autor.dto;

import io.github.patrickbg.bibliotecaapi.dominio.autor.model.GeneroAutor;
import io.github.patrickbg.bibliotecaapi.dominio.autor.model.StatusAutor;

import java.time.LocalDate;

public record AutorDetalhes(
        String id,
        String nome,
        LocalDate dataNascimento,
        LocalDate dataFalecimento,
        StatusAutor statusAutor,
        GeneroAutor generoAutor,
        String paisOrigem
) {
}
