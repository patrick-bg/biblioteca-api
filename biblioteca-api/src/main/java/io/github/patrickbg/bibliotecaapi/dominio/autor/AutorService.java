package io.github.patrickbg.bibliotecaapi.dominio.autor;

import io.github.patrickbg.bibliotecaapi.common.exceptions.RegistroNaoEncontradoException;
import io.github.patrickbg.bibliotecaapi.common.exceptions.ValidationException;
import io.github.patrickbg.bibliotecaapi.dominio.autor.dto.AutorDetalhes;
import io.github.patrickbg.bibliotecaapi.dominio.autor.dto.AutorForm;
import io.github.patrickbg.bibliotecaapi.dominio.autor.mapper.AutorMapper;
import io.github.patrickbg.bibliotecaapi.dominio.autor.model.AutorEntity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private AutorValidator validator;

    @Autowired
    private AutorMapper mapper;

    public AutorDetalhes criar(AutorForm form){
        var result = validator.validar(form, null);

        if(result.isInvalido()){
            throw new ValidationException(result.getCamposInvalidos());
        }
        AutorEntity entity = mapper.toEntity(form);
        autorRepository.save(entity);
        return mapper.toDetalhes(entity);
    }

    public AutorDetalhes obterDetalhes(UUID id){
        return autorRepository.findById(id)
                .map(mapper::toDetalhes)
                .orElseThrow(RegistroNaoEncontradoException::new);
    }

    public void atualizar(UUID id, @Valid AutorForm dadosAtualizados) {
        var entity = autorRepository.findById(id).orElseThrow(RegistroNaoEncontradoException::new);
        var result = validator.validar(dadosAtualizados,id);

        if(result.isInvalido()){
            throw new ValidationException(result.getCamposInvalidos());
        }

        mapper.update(entity, dadosAtualizados);
        autorRepository.save(entity);
    }
}
