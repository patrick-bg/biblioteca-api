package io.github.patrickbg.bibliotecaapi.dominio.livro;

import io.github.patrickbg.bibliotecaapi.dominio.livro.dto.LivroDetalhes;
import io.github.patrickbg.bibliotecaapi.dominio.livro.dto.LivroForm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("livros")
@CrossOrigin("*")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @PostMapping
    public ResponseEntity<LivroDetalhes> criar(
            @RequestBody @Valid LivroForm novoLivro){
        LivroDetalhes detalhes = livroService.criar(novoLivro);
        return ResponseEntity.status(HttpStatus.CREATED).body(detalhes);
    }

    @GetMapping("{id}")
    public ResponseEntity<LivroDetalhes> obterDetalhes(@PathVariable UUID id){
        var result = livroService.obterDetalhes(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> atualizar(@PathVariable UUID id, @RequestBody @Valid LivroForm livroAtualizado){
        livroService.atualizar(id, livroAtualizado);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public Page<LivroDetalhes> listar (
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10")int size){
        var pageRequest = PageRequest.of(page, size);
        return livroService.listar(pageRequest);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar (@PathVariable UUID id){
        livroService.deletar(id);
    }
}
