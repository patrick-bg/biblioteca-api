package io.github.patrickbg.bibliotecaapi.dominio.livro;

import io.github.patrickbg.bibliotecaapi.dominio.autor.model.GeneroAutor;
import io.github.patrickbg.bibliotecaapi.dominio.autor.model.PaisOrigemAutor;
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
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "autor-genero", required = false) String generoAutor,
            @RequestParam(value = "pais-origem", required = false) String paisOrigem,
            @RequestParam(value = "editora", required = false) String editora
    ) {
        var pageRequest = PageRequest.of(page, size);
        var anoConvertido = parseAno(ano);
        var statusLivro = parseStatus(status);
        var generoConvertido = parseGenero(genero);
        var idiomaConvertido = parseIdioma(idioma);
        var generoAutorConvertido = parseGeneroAutor(generoAutor);
        var paisOrigemConvertido = parsePaisOrigemAutor(paisOrigem);


        return livroService.listar(pageRequest,anoConvertido, statusLivro, autorId, generoConvertido, idiomaConvertido, titulo, generoAutorConvertido, paisOrigemConvertido, editora);
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

    private GeneroAutor parseGeneroAutor(String generoAutor){
        if (generoAutor == null || generoAutor.isBlank()) {
            return null;
        }

        try {
            return GeneroAutor.valueOf(generoAutor);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Parâmetro 'generoAutor' inválido.");
        }
    }

    private PaisOrigemAutor parsePaisOrigemAutor(String paisOrigem){
        if (paisOrigem == null || paisOrigem.isBlank()) {
            return null;
        }

        try {
            return PaisOrigemAutor.valueOf(paisOrigem);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Parâmetro 'paisOrigem' inválido.");
        }
    }



}
