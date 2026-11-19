package io.github.patrickbg.bibliotecaapi.dominio.googlebooks.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ImageLinks(String thumbnail){}
