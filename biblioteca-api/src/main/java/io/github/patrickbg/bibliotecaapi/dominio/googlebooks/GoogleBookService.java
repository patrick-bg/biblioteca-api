package io.github.patrickbg.bibliotecaapi.dominio.googlebooks;

import io.github.patrickbg.bibliotecaapi.dominio.googlebooks.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

@Service
public class GoogleBookService {

    private final RestClient restClient;

    public GoogleBookService(){
        this.restClient = RestClient.builder().baseUrl("https://googleapis.com").build();
    }

    public List<LivroBuscaDTO> pesquisaLivros(String termo){
        GoogleBooksResponse response = restClient.get().uri(
                uriBuilder -> uriBuilder
                        .path("/volumes")
                        .queryParam("q", termo)
                        .queryParam("maxResult", 8)
                        .build())
                .retrieve()
                .body(GoogleBooksResponse.class);

        if(response == null || response.items() == null){
            return Collections.emptyList();
        }

        return response.items().stream().map(this::converterParaDTO).toList();
    }

    private LivroBuscaDTO converterParaDTO(GoogleBookItem item){
        VolumeInfo info = item.volumeInfo();

        String isbn = extrairIsbn(info.industryIdentifiers());
        String capa = (info.imageLinks() != null && info.imageLinks().thumbnail() != null)
                ? info.imageLinks().thumbnail().replace("http://", "https://")
                : null;

        return new LivroBuscaDTO(
                info.title(),
                info.authors() != null ? info.authors() : List.of("Autor desconhecido"),
                isbn,
                capa
        );
    }

    private String extrairIsbn(List<IndustryIdentifier> industryIdentifiers){
        if(industryIdentifiers == null) return "";

        return industryIdentifiers.stream().filter(id -> "ISBN_13".equals(id.type()))
                .map(IndustryIdentifier::identifier).findFirst()
                .orElseGet(() -> industryIdentifiers.stream()
                        .filter(id -> "ISBN_10".equals(id.type()))
                        .map(IndustryIdentifier::identifier).findFirst().orElse(""));
    }
}
