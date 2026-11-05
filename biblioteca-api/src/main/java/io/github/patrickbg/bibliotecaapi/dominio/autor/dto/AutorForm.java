package io.github.patrickbg.bibliotecaapi.dominio.autor.dto;

import io.github.patrickbg.bibliotecaapi.dominio.autor.model.GeneroAutor;

import java.time.LocalDate;

public record AutorForm(
        String nome,
        GeneroAutor generoAutor,
        LocalDate dataNascimento,
        String paisOrigem) {
}
