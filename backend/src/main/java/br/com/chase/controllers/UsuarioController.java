package br.com.chase.controllers;

import br.com.chase.models.Usuario;
import br.com.chase.services.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // POST - Criar novo usuário
    @PostMapping("/criar")
    public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.criarUsuario(usuario);
        return ResponseEntity.ok(novoUsuario);
    }

    // GET - Buscar usuário pelo UID (Firebase)
    @GetMapping("/{uid}")
    public ResponseEntity<Usuario> buscarUsuarioPorUid(@PathVariable String uid) {
        Usuario usuario = usuarioService.buscarUsuarioPorUid(uid);
        return ResponseEntity.ok(usuario);
    }
}
