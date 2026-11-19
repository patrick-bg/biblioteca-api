package io.github.patrickbg.bibliotecaapi.dominio.googlebooks.dto;

import java.util.List;

public record VolumeInfo(
        String title,
        List<String> authors,
        List<IndustryIdentifier> industryIdentifiers,
        ImageLinks imageLinks
){}
