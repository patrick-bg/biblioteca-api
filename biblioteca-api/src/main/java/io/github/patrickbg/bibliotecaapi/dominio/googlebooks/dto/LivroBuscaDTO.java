package io.github.patrickbg.bibliotecaapi.dominio.googlebooks.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.List;

public record LivroBuscaDTO(
        String titulo,
        List<String> autores,
        String isbn,
        String capaUrl
) {}

