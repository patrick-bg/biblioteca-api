package io.github.patrickbg.bibliotecaapi.dominio.livro.dto;

import io.github.patrickbg.bibliotecaapi.dominio.autor.model.AutorEntity;
import io.github.patrickbg.bibliotecaapi.dominio.livro.model.FormatoLivro;
import io.github.patrickbg.bibliotecaapi.dominio.livro.model.GeneroLivro;
import io.github.patrickbg.bibliotecaapi.dominio.livro.model.IdiomaLivro;
import io.github.patrickbg.bibliotecaapi.dominio.livro.model.StatusLivro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record LivroForm (
        @NotBlank(message = "Campo obrigatório.")
        String titulo,
        @ISBN
        @NotNull(message = "Campo obrigatório.")
        String isbn,
        @NotNull(message = "Campo obrigatório.")
        GeneroLivro genero,
        @NotNull(message = "Campo obrigatório.")
        BigDecimal preco,
        @NotNull(message = "Campo obrigatório.")
        UUID autorId,
        @NotNull(message = "Campo obrigatório.")
        IdiomaLivro idioma,
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
