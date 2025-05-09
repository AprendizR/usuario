package com.renan.usuario.controller;

import com.renan.usuario.infrastructure.clients.ViaCepDTO;
import com.renan.usuario.infrastructure.entity.Usuario;
import com.renan.usuario.infrastructure.security.JwtUtil;
import com.renan.usuario.services.UsuarioService;
import com.renan.usuario.services.ViaCepService;
import com.renan.usuario.services.dto.EnderecoDTO;
import com.renan.usuario.services.dto.TelefoneDTO;
import com.renan.usuario.services.dto.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/usuario")
@RequiredArgsConstructor

public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ViaCepService viaCepService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO usuarioDTO){
        return ResponseEntity.ok(usuarioService.salvaUsuario(usuarioDTO));
    }
    @PostMapping("/login")
    public String login(@RequestBody UsuarioDTO usuarioDTO){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha())
        );
        return jwtUtil.generateToken(authentication.getName());
    }
    @GetMapping
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email){
        usuarioService.deletaUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO> atualizaDadoUsuario(@RequestBody UsuarioDTO dto,
                                                          @RequestHeader ("Authorization") String token){
        return ResponseEntity.ok(usuarioService.atualizarDadosUsuario(token, dto));
    }

    @PutMapping ("/endereco")
    public ResponseEntity<EnderecoDTO> atualizaEndereco(@RequestBody EnderecoDTO dto,
                                                        @RequestParam ("id") Long id){
        return ResponseEntity.ok(usuarioService.atualizaEndereco(id, dto));
    }

    @PutMapping ("/telefone")
    public ResponseEntity<TelefoneDTO> atualizaTelefone(@RequestBody TelefoneDTO dto,
                                                        @RequestParam ("id") Long id){
        return ResponseEntity.ok(usuarioService.atualizaTelefone(id, dto));
    }

    @PostMapping ("/endereco")
    public ResponseEntity<EnderecoDTO> cadastroEndereco(@RequestBody EnderecoDTO dto,
                                                        @RequestHeader ("authorization") String token){
        return ResponseEntity.ok(usuarioService.cadastroEndereco(token, dto));
    }

    @PostMapping ("/telefone")
    public ResponseEntity<TelefoneDTO> cadastroTelefone(@RequestBody TelefoneDTO dto,
                                                        @RequestHeader ("authorization") String token){
        return ResponseEntity.ok(usuarioService.cadastroTelefone(token, dto));
    }

    @GetMapping ("/endereco/{cep}")
    public ResponseEntity<ViaCepDTO> buscarDadosCep(@PathVariable("cep") String cep){
        return ResponseEntity.ok(viaCepService.buscarDadosEndereco(cep));
    }
}
