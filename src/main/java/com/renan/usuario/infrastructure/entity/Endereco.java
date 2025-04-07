package com.renan.usuario.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity        //aponta para o spring que essa é a tabela de um banco de dados
@Table(name = "endereco")  //nome da tabela
@Builder

public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "rua")
    private String rua;
    @Column(name = "numero")
    private Long numero;
    @Column(name = "complemento", length = 10)
    private String complemento;
    @Column(name = "cidade")
    private String cidade;
    @Column(name = "estado", length = 2)
    private String estado;
    @Column(name = "cep", length = 9)
    private String cep;
    @Column (name = "usuario_id")
    private Long usuario_id;
}
