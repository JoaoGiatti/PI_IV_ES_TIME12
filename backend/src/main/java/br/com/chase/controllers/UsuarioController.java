package br.com.chase.controllers;

import br.com.chase.models.Usuario;
import br.com.chase.services.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
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

    @PutMapping("/{uid}")
    public ResponseEntity<Map<String, Object>> atualizarUsuario(
            @PathVariable String uid,
            @RequestBody Usuario usuarioAtualizado) {

        Usuario usuario = usuarioService.atualizarUsuario(uid, usuarioAtualizado);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Perfil atualizado com sucesso!");
        response.put("updatedUser", usuario);

        return ResponseEntity.ok(response);
    }

}
