package io.github.patrickbg.bibliotecaapi.dominio.googlebooks;

import io.github.patrickbg.bibliotecaapi.dominio.googlebooks.dto.LivroBuscaDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<LivroBuscaDTO>> buscarLivros(@RequestParam String q){
        if (q == null || q.isBlank()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(googleBookService.pesquisaLivros(q));
    }
}
