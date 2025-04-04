package com.renan.usuario.services.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class TelefoneDTO {
    private String numero;
    private String ddd;
}
