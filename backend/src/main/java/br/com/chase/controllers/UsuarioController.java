package br.com.chase.controllers;
import br.com.chase.models.Usuario;
import br.com.chase.repositories.UsuarioRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/criar-usuarios")
    public String criarUsuarios() {
        Usuario usuarioTeste = new Usuario("Giatto", "giatto@email.com");
        usuarioRepository.save(usuarioTeste);
        return "Coleção 'usuarios' criada com sucesso!";
    }
}