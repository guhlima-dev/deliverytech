package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.requests.LoginRequestDTO;
import com.deliverytech.delivery_api.dto.responses.LoginResponseDTO;
import com.deliverytech.delivery_api.enums.Role;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import com.deliverytech.delivery_api.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public  AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> cadastrar(@RequestBody LoginRequestDTO request){

        if (usuarioRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email já cadastrado");
        }
        if (request.getRole() == Role.ADMIN){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Não é permitido criar usuário com função ADMIN");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));

        usuario.setRole(request.getRole() != null ? request.getRole() : Role.CLIENTE);

        usuario.setAtivo(true);
        usuarioRepository.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<?> Login (@RequestBody LoginRequestDTO login){

        Usuario usuario = usuarioRepository.findByEmail(login.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Email inválido."));

        if (!passwordEncoder.matches(login.getSenha(), usuario.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha inválida");
        }

        String token = jwtUtil.generateToken(usuario);
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @GetMapping("/me")
    public ResponseEntity<Usuario> me(Authentication auth){

        String email = auth.getName();

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();

        return ResponseEntity.ok(usuario);
    }
}
