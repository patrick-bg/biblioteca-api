package io.github.patrickbg.bibliotecaapi.dominio.autor;

import io.github.patrickbg.bibliotecaapi.dominio.autor.dto.AutorDetalhes;
import io.github.patrickbg.bibliotecaapi.dominio.autor.dto.AutorForm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("autores")
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
}
