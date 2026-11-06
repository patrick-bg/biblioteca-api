package io.github.patrickbg.bibliotecaapi.dominio.autor;

import io.github.patrickbg.bibliotecaapi.dominio.autor.model.AutorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AutorRepository extends JpaRepository<AutorEntity, UUID> {

    Optional<AutorEntity> findByNome(String nome);

    @Query("""
        select a
        from AutorEntity a
        where ( :id is null or a.id != :id)
        and a.nome = :nome
""")
    List<AutorEntity> findByNomeAndNotId(@Param("nome") String nome, @Param("id") UUID id);
}
