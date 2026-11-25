package io.github.patrickbg.bibliotecaapi.dominio.googlebooks.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.List;

public record LivroBuscaDTO(
        String titulo,
        String isbn,
        String editora,
        Integer paginasTotais,
        String urlCapa,
        String autorNome
) {}

