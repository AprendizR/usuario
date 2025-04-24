package com.renan.usuario.services;

import com.renan.usuario.infrastructure.clients.ViaCepClient;
import com.renan.usuario.infrastructure.clients.ViaCepDTO;
import com.renan.usuario.infrastructure.exceptions.IllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ViaCepService {
    private final ViaCepClient viaCepClient;

    public ViaCepDTO buscarDadosEndereco(String cep) {
        return viaCepClient.buscaDadosEndereco(processarCep(cep));
    }

    private String processarCep(String cep) {
        String cepFormatado = cep.replace(" ", "").replace("-", "");

        if (!cepFormatado.matches("\\d{8}")) {
            throw new IllegalArgumentException("O CEP contém caracteres inválidos, favor verificar");
        }
        return cepFormatado;
    }
}
