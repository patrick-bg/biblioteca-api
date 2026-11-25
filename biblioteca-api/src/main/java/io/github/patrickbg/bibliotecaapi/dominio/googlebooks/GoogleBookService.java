package io.github.patrickbg.bibliotecaapi.dominio.googlebooks;

import io.github.patrickbg.bibliotecaapi.dominio.googlebooks.dto.LivroBuscaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

        try {
            URI uri = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host("www.googleapis.com")
                    .path("/books/v1/volumes")
                    .queryParam("q", "isbn:" + isbnLimpo)
                    .build()
                    .encode()
                    .toUri();

            Map<String, Object> response = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            if (response != null && response.containsKey("items")) {
                List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");
                if (!items.isEmpty()) {
                    return Optional.of(mapearVolumeGoogle((Map<String, Object>) items.get(0).get("volumeInfo")));
                }
            }
        } catch (Exception ex) {
            log.warn("Falha no Google Books por ISBN ({}): {}", isbnLimpo, ex.getMessage());
        }

        return buscarOpenLibraryPorIsbn(isbnLimpo);
    }

    public List<LivroBuscaDTO> buscarPorTitulo(String titulo) {
        try {
            URI uri = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host("www.googleapis.com")
                    .path("/books/v1/volumes")
                    .queryParam("q", "intitle:" + titulo.trim())
                    .queryParam("maxResults", 10)
                    .build()
                    .encode()
                    .toUri();

            Map<String, Object> response = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            if (response != null && response.containsKey("items")) {
                List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");
                if (!items.isEmpty()) {
                    return items.stream()
                            .map(item -> mapearVolumeGoogle((Map<String, Object>) item.get("volumeInfo")))
                            .toList();
                }
            }
        } catch (Exception ex) {
            log.warn("Falha no Google Books por título ('{}'): {}", titulo, ex.getMessage());
        }

        return buscarOpenLibraryPorTitulo(titulo.trim());
    }

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
            log.warn("Falha também na Open Library por ISBN ({}): {}", isbn, ex.getMessage());
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
            log.warn("Falha também na Open Library por título: {}", ex.getMessage());
            return List.of();
        }
    }

    @SuppressWarnings("unchecked")
    private LivroBuscaDTO mapearVolumeGoogle(Map<String, Object> volumeInfo) {
        if (volumeInfo == null) {
            return new LivroBuscaDTO("", "", "", 0, "", "");
        }

        String titulo = (String) volumeInfo.getOrDefault("title", "");
        String editora = (String) volumeInfo.getOrDefault("publisher", "");
        Integer paginas = (Integer) volumeInfo.getOrDefault("pageCount", 0);

        List<String> autores = (List<String>) volumeInfo.get("authors");
        String autorNome = (autores != null && !autores.isEmpty()) ? autores.get(0) : "";

        String isbn = "";
        List<Map<String, String>> identifiers = (List<Map<String, String>>) volumeInfo.get("industryIdentifiers");
        if (identifiers != null) {
            isbn = identifiers.stream()
                    .filter(id -> "ISBN_13".equals(id.get("type")))
                    .map(id -> id.get("identifier"))
                    .findFirst()
                    .orElseGet(() -> identifiers.stream()
                            .filter(id -> "ISBN_10".equals(id.get("type")))
                            .map(id -> id.get("identifier"))
                            .findFirst()
                            .orElse(""));
        }

        String urlCapa = "";
        Map<String, String> imageLinks = (Map<String, String>) volumeInfo.get("imageLinks");
        if (imageLinks != null && imageLinks.containsKey("thumbnail")) {
            urlCapa = imageLinks.get("thumbnail").replace("http://", "https://");
        }

        return new LivroBuscaDTO(titulo, isbn, editora, paginas, urlCapa, autorNome);
    }
}