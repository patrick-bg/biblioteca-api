package io.github.patrickbg.bibliotecaapi.dominio.autor;

import io.github.patrickbg.bibliotecaapi.dominio.autor.dto.AutorDetalhes;
import io.github.patrickbg.bibliotecaapi.dominio.autor.dto.AutorForm;
import io.github.patrickbg.bibliotecaapi.dominio.autor.mapper.AutorMapper;
import io.github.patrickbg.bibliotecaapi.dominio.autor.model.AutorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private AutorValidator validator;

    @Autowired
    private AutorMapper mapper;

    public AutorDetalhes criar(AutorForm form){
        validator.validar(form);
        AutorEntity entity = mapper.toEntity(form);
        autorRepository.save(entity);
        return mapper.toDetalhes(entity);
    }
}
