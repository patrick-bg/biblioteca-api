package io.github.patrickbg.bibliotecaapi.dominio.autor;

import io.github.patrickbg.bibliotecaapi.dominio.autor.model.AutorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AutorRepository extends JpaRepository<AutorEntity, UUID> {
}
