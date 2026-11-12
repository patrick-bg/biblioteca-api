package io.github.patrickbg.bibliotecaapi.dominio.livro.dto;

import io.github.patrickbg.bibliotecaapi.dominio.autor.model.AutorEntity;
import io.github.patrickbg.bibliotecaapi.dominio.livro.model.FormatoLivro;
import io.github.patrickbg.bibliotecaapi.dominio.livro.model.GeneroLivro;
import io.github.patrickbg.bibliotecaapi.dominio.livro.model.StatusLivro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record LivroForm (
        @NotBlank(message = "Campo obrigatório.")
        String titulo,
        @NotNull(message = "Campo obrigatório.")
        String isbn,
        @NotNull(message = "Campo obrigatório.")
        GeneroLivro genero,
        @NotNull(message = "Campo obrigatório.")
        AutorEntity autor,
        @NotNull(message = "Campo obrigatório.")
        String idioma,
        @NotNull(message = "Campo obrigatório.")
        FormatoLivro formato,
        @NotNull(message = "Campo obrigatório.")
        String editora,
        @NotNull(message = "Campo obrigatório.")
        LocalDate dataPublicacao,
        @NotNull(message = "Campo obrigatório.")
        Integer paginasTotais,
        @NotNull(message = "Campo obrigatório.")
        StatusLivro status,
        Integer paginasLidas,
        String urlCapa
) {
}
