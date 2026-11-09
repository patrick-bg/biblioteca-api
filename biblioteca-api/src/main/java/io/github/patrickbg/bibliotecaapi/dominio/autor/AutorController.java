package io.github.patrickbg.bibliotecaapi.dominio.autor;

import io.github.patrickbg.bibliotecaapi.dominio.autor.dto.AutorDetalhes;
import io.github.patrickbg.bibliotecaapi.dominio.autor.dto.AutorForm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("autores")
@CrossOrigin("*")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @PostMapping
    public ResponseEntity<AutorDetalhes> criar(@RequestBody @Valid AutorForm novo){
        AutorDetalhes detalhes = autorService.criar(novo);
        return ResponseEntity.status(HttpStatus.CREATED).body(detalhes);
    }

    @GetMapping("{id}")
    public ResponseEntity<AutorDetalhes> obterDetalhes(@PathVariable UUID id){
        var result = autorService.obterDetalhes(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> atualizar(@PathVariable UUID id, @RequestBody @Valid AutorForm dadosAtualizados){
        autorService.atualizar(id, dadosAtualizados);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public Page<AutorDetalhes> listar (
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){

        var pageRequest = PageRequest.of(page, size);
        return autorService.listar(pageRequest);
    }
}
