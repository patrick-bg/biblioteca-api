package io.github.patrickbg.bibliotecaapi.dominio.googlebooks;

import io.github.patrickbg.bibliotecaapi.dominio.googlebooks.dto.LivroBuscaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GoogleBookService {

    private static final Logger log = LoggerFactory.getLogger(GoogleBookService.class);

    private final RestClient restClient = RestClient.builder()
            .defaultHeader("Accept", "application/json")
            .defaultHeader("User-Agent", "BibliotecaApp/1.0")
            .build();

    public Optional<LivroBuscaDTO> buscarPorIsbn(String isbn) {
        String isbnLimpo = isbn.replaceAll("[^0-9X]", "");

        // 1. Se for ISBN brasileiro (começa com 97885 ou 97865), prioriza a BrasilAPI (CBL)
        if (isbnLimpo.startsWith("97885") || isbnLimpo.startsWith("97865")) {
            Optional<LivroBuscaDTO> resultadoBrasilApi = buscarBrasilApiPorIsbn(isbnLimpo);
            if (resultadoBrasilApi.isPresent()) {
                return resultadoBrasilApi;
            }
        }

        // 2. Fallback para livros internacionais ou não encontrados na BrasilAPI: Open Library
        return buscarOpenLibraryPorIsbn(isbnLimpo);
    }

    public List<LivroBuscaDTO> buscarPorTitulo(String titulo) {
        return buscarOpenLibraryPorTitulo(titulo.trim());
    }

    // --- CONSULTA BRASILAPI (CÂMARA BRASILEIRA DO LIVRO) ---

    @SuppressWarnings("unchecked")
    private Optional<LivroBuscaDTO> buscarBrasilApiPorIsbn(String isbn) {
        try {
            URI uri = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host("brasilapi.com.br")
                    .path("/api/isbn/v1/" + isbn)
                    .build()
                    .toUri();

            Map<String, Object> response = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            if (response == null || !response.containsKey("title")) {
                return Optional.empty();
            }

            String titulo = (String) response.getOrDefault("title", "");
            String editora = (String) response.getOrDefault("publisher", "");
            Integer paginas = (response.get("page_count") instanceof Number n) ? n.intValue() : 0;
            String urlCapa = (response.get("cover_url") instanceof String s) ? s : "";

            List<String> autores = (List<String>) response.get("authors");
            String autorNome = (autores != null && !autores.isEmpty()) ? autores.get(0) : "";

            // Se a BrasilAPI não trouxer a capa, tenta buscar apenas a capa na Open Library
            if (urlCapa.isBlank()) {
                urlCapa = buscarOpenLibraryPorIsbn(isbn)
                        .map(LivroBuscaDTO::urlCapa)
                        .orElse("");
            }

            return Optional.of(new LivroBuscaDTO(
                    titulo,
                    isbn,
                    editora,
                    paginas,
                    urlCapa,
                    autorNome
            ));
        } catch (Exception ex) {
            log.warn("Falha ao consultar BrasilAPI por ISBN ({}): {}", isbn, ex.getMessage());
            return Optional.empty();
        }
    }

    // --- FALLBACK: OPEN LIBRARY API ---

    @SuppressWarnings("unchecked")
    private Optional<LivroBuscaDTO> buscarOpenLibraryPorIsbn(String isbn) {
        try {
            String key = "ISBN:" + isbn;
            URI uri = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host("openlibrary.org")
                    .path("/api/books")
                    .queryParam("bibkeys", key)
                    .queryParam("format", "json")
                    .queryParam("jscmd", "data")
                    .build()
                    .encode()
                    .toUri();

            Map<String, Object> response = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            if (response == null || !response.containsKey(key)) {
                return Optional.empty();
            }

            Map<String, Object> info = (Map<String, Object>) response.get(key);
            String titulo = (String) info.getOrDefault("title", "");
            Integer paginas = (info.get("number_of_pages") instanceof Number n) ? n.intValue() : 0;

            String editora = "";
            List<Map<String, String>> publishers = (List<Map<String, String>>) info.get("publishers");
            if (publishers != null && !publishers.isEmpty()) {
                editora = publishers.get(0).getOrDefault("name", "");
            }

            String autorNome = "";
            List<Map<String, String>> authors = (List<Map<String, String>>) info.get("authors");
            if (authors != null && !authors.isEmpty()) {
                autorNome = authors.get(0).getOrDefault("name", "");
            }

            String urlCapa = "";
            Map<String, String> cover = (Map<String, String>) info.get("cover");
            if (cover != null) {
                urlCapa = cover.getOrDefault("large", cover.getOrDefault("medium", ""));
            }

            return Optional.of(new LivroBuscaDTO(titulo, isbn, editora, paginas, urlCapa, autorNome));
        } catch (Exception ex) {
            log.warn("Falha na Open Library por ISBN ({}): {}", isbn, ex.getMessage());
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    private List<LivroBuscaDTO> buscarOpenLibraryPorTitulo(String tituloBusca) {
        try {
            URI uri = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host("openlibrary.org")
                    .path("/search.json")
                    .queryParam("title", tituloBusca)
                    .queryParam("limit", 10)
                    .build()
                    .encode()
                    .toUri();

            Map<String, Object> response = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            if (response == null || !response.containsKey("docs")) {
                return List.of();
            }

            List<Map<String, Object>> docs = (List<Map<String, Object>>) response.get("docs");

            return docs.stream().map(doc -> {
                String titulo = (String) doc.getOrDefault("title", "");
                Integer paginas = (doc.get("number_of_pages_median") instanceof Number n) ? n.intValue() : 0;

                List<String> autores = (List<String>) doc.get("author_name");
                String autorNome = (autores != null && !autores.isEmpty()) ? autores.get(0) : "";

                List<String> editoras = (List<String>) doc.get("publisher");
                String editora = (editoras != null && !editoras.isEmpty()) ? editoras.get(0) : "";

                List<String> isbns = (List<String>) doc.get("isbn");
                String isbn = (isbns != null && !isbns.isEmpty()) ? isbns.get(0) : "";

                Object coverId = doc.get("cover_i");
                String urlCapa = (coverId != null)
                        ? UriComponentsBuilder.newInstance()
                        .scheme("https")
                        .host("covers.openlibrary.org")
                        .path("/b/id/" + coverId + "-L.jpg")
                        .build()
                        .toUriString()
                        : "";

                return new LivroBuscaDTO(titulo, isbn, editora, paginas, urlCapa, autorNome);
            }).toList();
        } catch (Exception ex) {
            log.warn("Falha na Open Library por título ('{}'): {}", tituloBusca, ex.getMessage());
            return List.of();
        }
    }
}