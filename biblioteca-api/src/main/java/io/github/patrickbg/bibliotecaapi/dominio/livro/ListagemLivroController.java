package io.github.patrickbg.bibliotecaapi.dominio.livro;

import io.github.patrickbg.bibliotecaapi.dominio.autor.AutorService;
import io.github.patrickbg.bibliotecaapi.dominio.livro.dto.LivroDetalhes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("livros")
@CrossOrigin("*")
public class ListagemLivroController {

    @Autowired
    private LivroService livroService;

    @Autowired
    private AutorService autorService;

    @GetMapping
    public Page<LivroDetalhes> listar (
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10")int size){
        var pageRequest = PageRequest.of(page, size);
        return livroService.listar(pageRequest);
    }
}
