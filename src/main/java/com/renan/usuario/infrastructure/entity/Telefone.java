package com.renan.usuario.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity        //aponta para o spring que essa é a tabela de um banco de dados
@Table(name = "telefone")  //nome da tabela
@Builder

public class Telefone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "numero", length = 10)
    private String numero;
    @Column(name = "ddd", length = 3)
    private String ddd;
    @Column (name = "usuario_id")
    private Long usuario_id;

}
