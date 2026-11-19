package io.github.patrickbg.bibliotecaapi.dominio.googlebooks.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GoogleBooksResponse(List<GoogleBookItem> items){}
