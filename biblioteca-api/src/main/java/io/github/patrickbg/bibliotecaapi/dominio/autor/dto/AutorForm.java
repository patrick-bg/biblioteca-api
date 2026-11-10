package io.github.patrickbg.bibliotecaapi.dominio.autor.dto;

import io.github.patrickbg.bibliotecaapi.dominio.autor.model.GeneroAutor;
import io.github.patrickbg.bibliotecaapi.dominio.autor.model.StatusAutor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AutorForm(
        @NotBlank(message = "Campo obrigatório.")
        String nome,
        @NotNull(message = "Campo obrigatório.")
        GeneroAutor generoAutor,
        @NotNull(message = "Campo obrigatório.")
        LocalDate dataNascimento,
        @NotNull(message = "Campo obrigatório.")
        StatusAutor statusAutor,
        LocalDate dataFalecimento,
        @NotNull(message = "Campo obrigatório.")
        String paisOrigem) {
}
