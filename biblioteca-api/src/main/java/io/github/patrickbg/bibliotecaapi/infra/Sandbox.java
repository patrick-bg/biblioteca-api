package io.github.patrickbg.bibliotecaapi.infra;

import io.github.patrickbg.bibliotecaapi.dominio.autor.AutorRepository;
import io.github.patrickbg.bibliotecaapi.dominio.autor.model.AutorEntity;
import io.github.patrickbg.bibliotecaapi.dominio.autor.model.GeneroAutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Sandbox implements CommandLineRunner {

    @Autowired
    private AutorRepository autorRepository;

    public void salvarAutor(){
        AutorEntity autor = new AutorEntity();
        autor.setNome("Patrick Brian");
        autor.setGeneroAutor(GeneroAutor.MASCULINO);
        autor.setDataNascimento(LocalDate.of(1989, 10, 10));
        autor.setPaisOrigem("Brasil");
        autorRepository.save(autor);
    }

    @Override
    public void run(String... args) throws Exception {
        //salvarAutor();
    }
}
