package io.github.patrickbg.bibliotecaapi.dominio.livro.model;

import io.github.patrickbg.bibliotecaapi.dominio.autor.model.AutorEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "livro")
@Getter
@Setter
@ToString(exclude = "autor")
public class LivroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "isbn", length = 20, nullable = false)
    private String isbn;

    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name = "data_publicacao")
    private LocalDate dataPublicacao;

    @Column(name = "genero")
    @Enumerated(EnumType.STRING)
    private GeneroLivro genero;

    @Column(name = "preco")
    private BigDecimal preco;

    @ManyToOne
    @JoinColumn(name = "autor")
    private AutorEntity autor;

    @Column(name = "idioma", nullable = false)
    private String idioma;

    @Enumerated(EnumType.STRING)
    @Column(name = "formato", nullable = false)
    private FormatoLivro formato;

    @Column(name = "editora")
    private String editora;

    @Column(name = "paginas", nullable = false)
    private Integer paginas;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusLivro status;

}
