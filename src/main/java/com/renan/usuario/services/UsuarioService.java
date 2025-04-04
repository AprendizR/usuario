package com.renan.usuario.services;

import com.renan.usuario.infrastructure.entity.Usuario;
import com.renan.usuario.infrastructure.exceptions.ConflictExceptions;
import com.renan.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.renan.usuario.infrastructure.repository.UsuarioRepository;
import com.renan.usuario.services.converter.UsuarioConverter;
import com.renan.usuario.services.dto.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public void emailExiste(String email){
        try {
            boolean existe = verificarEmailExistente(email);
            if (existe){
                throw new ConflictExceptions("Email já cadastrado " + email);
            }
        }catch (ConflictExceptions e){
            throw new ConflictExceptions("Email já cadastrado ", e.getCause());
        }
    }

    //Chamando metodo fora do try para poder reutilizar o código para outras demandas
    public boolean verificarEmailExistente(String email){
        return usuarioRepository.existsByEmail(email);
    }
    public Usuario buscarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Email não encontrado: " + email));
    }
    public void deletaUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }
}
