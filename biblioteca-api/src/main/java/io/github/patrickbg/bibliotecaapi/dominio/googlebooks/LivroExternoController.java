package io.github.patrickbg.bibliotecaapi.dominio.googlebooks;

import io.github.patrickbg.bibliotecaapi.dominio.googlebooks.dto.LivroBuscaDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("livros")

public class LivroExternoController {

    private final GoogleBookService googleBookService;

    public LivroExternoController(GoogleBookService googleBooksService) {
        this.googleBookService = googleBooksService;
    }

    @GetMapping("/buscar")
    public ResponseEntity<LivroBuscaDTO> buscarDadosExternos(@RequestParam String isbn) {
        return googleBookService.buscarPorIsbn(isbn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/titulo")
    public ResponseEntity<List<LivroBuscaDTO>> buscarExternoPorTitulo(@RequestParam String titulo) {
        List<LivroBuscaDTO> resultados = googleBookService.buscarPorTitulo(titulo);
        return ResponseEntity.ok(resultados);
    }
}
