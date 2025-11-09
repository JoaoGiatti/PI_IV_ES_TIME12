package br.com.chase.controllers;

import br.com.chase.models.Usuario;
import br.com.chase.services.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // POST - Cria um usuario...
    @PostMapping("/create")
    public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.criarUsuario(usuario);
        return ResponseEntity.ok(novoUsuario);
    }

    // GET - Mostra detalhes de um usuario pelo UID...
    @GetMapping("/{uid}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable String uid) {
        Usuario usuario = usuarioService.buscarUsuario(uid);
        return ResponseEntity.ok(usuario);
    }

    // PUT - Atualiza um usuario pelo UID
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
