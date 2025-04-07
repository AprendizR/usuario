package com.renan.usuario.infrastructure.repository;

import com.renan.usuario.infrastructure.entity.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //Acesso ao banco de dados
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
}
