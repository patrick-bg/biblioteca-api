package io.github.patrickbg.bibliotecaapi.dominio.autor.mapper;

import io.github.patrickbg.bibliotecaapi.dominio.autor.dto.AutorDetalhes;
import io.github.patrickbg.bibliotecaapi.dominio.autor.dto.AutorForm;
import io.github.patrickbg.bibliotecaapi.dominio.autor.model.AutorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AutorMapper {
     AutorEntity toEntity(AutorForm form);

     AutorDetalhes toDetalhes(AutorEntity entity);
}
