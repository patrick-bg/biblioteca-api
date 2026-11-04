package io.github.patrickbg.bibliotecaapi.dominio.autor.model;

import io.github.patrickbg.bibliotecaapi.dominio.livro.model.LivroEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "autor")
@Getter
@Setter
@ToString(exclude = "livros")
public class AutorEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero_autor", nullable = false)
    private GeneroAutor generoAutor;

    @Column(name = "pais")
    private String paisOrigem;

    @OneToMany(mappedBy = "autor", fetch = FetchType.LAZY)
    private List<LivroEntity> livros;
}
