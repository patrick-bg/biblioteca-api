package io.github.patrickbg.bibliotecaapi.dominio.livro;

import io.github.patrickbg.bibliotecaapi.dominio.livro.dto.LivroDetalhes;
import io.github.patrickbg.bibliotecaapi.dominio.livro.model.GeneroLivro;
import io.github.patrickbg.bibliotecaapi.dominio.livro.model.IdiomaLivro;
import io.github.patrickbg.bibliotecaapi.dominio.livro.model.StatusLivro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.DateTimeException;
import java.time.Year;
import java.util.UUID;

@RestController
@RequestMapping("livros")
public class ListagemLivroController {

    @Autowired
    private LivroService livroService;

    @GetMapping
    public Page<LivroDetalhes> listar(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "ano", required = false) String ano,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "autor-id", required = false) UUID autorId,
            @RequestParam(value = "genero", required = false) String genero,
            @RequestParam(value = "idioma", required = false) String idioma,
            @RequestParam(value = "titulo", required = false) String titulo
    ) {
        var pageRequest = PageRequest.of(page, size);
        var anoConvertido = parseAno(ano);
        var statusLivro = parseStatus(status);
        var generoConvertido = parseGenero(genero);
        var idiomaConvertido = parseIdioma(idioma);

        return livroService.listar(pageRequest,anoConvertido, statusLivro, autorId, generoConvertido, idiomaConvertido, titulo);
    }

    private Year parseAno(String ano) {
        if (ano == null || ano.isBlank()) {
            return null;
        }

        try {
            return Year.parse(ano);
        } catch (DateTimeException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Parâmetro 'ano' inválido.");
        }
    }

    private StatusLivro parseStatus(String status){
        if (status == null || status.isBlank()) {
            return null;
        }

        try {
            return StatusLivro.valueOf(status);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Parâmetro 'status' inválido.");
        }
    }

    private GeneroLivro parseGenero(String genero){
        if (genero == null || genero.isBlank()) {
            return null;
        }

        try {
            return GeneroLivro.valueOf(genero);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Parâmetro 'genero' inválido.");
        }
    }

    private IdiomaLivro parseIdioma(String idioma){
        if (idioma == null || idioma.isBlank()) {
            return null;
        }

        try {
            return IdiomaLivro.valueOf(idioma);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Parâmetro 'idioma' inválido.");
        }
    }


}
