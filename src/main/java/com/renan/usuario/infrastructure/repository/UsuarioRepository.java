package com.renan.usuario.infrastructure.repository;

import com.renan.usuario.infrastructure.entity.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //Acesso ao banco de dados
public interface UsuarioRepository extends JpaRepository <Usuario, Long> {
    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);

    @Transactional
    void deleteByEmail(String email);
}
