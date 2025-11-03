package br.com.chase.services;

import br.com.chase.models.Usuario;
import br.com.chase.repositories.UsuarioRepository;
import br.com.chase.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario criarUsuario(Usuario usuario) {
        validarUsuario(usuario);

        if (usuarioRepository.existsById(usuario.getId())) {
            throw new BadRequestException("Usuário já cadastrado.");
        }

        usuario.setCreatedAt(new Date());
        usuario.setMedals(new ArrayList<>());
        usuario.setTotalCalories(0);
        usuario.setTotalDistance(0);
        usuario.setTotalTime(0);

        return usuarioRepository.save(usuario);
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario.getId() == null || usuario.getId().isBlank())
            throw new BadRequestException("O campo 'id' (UID Firebase) é obrigatório.");

        if (usuario.getEmail() == null || usuario.getEmail().isBlank())
            throw new BadRequestException("O campo 'email' é obrigatório.");

        if (usuario.getName() == null || usuario.getName().isBlank())
            throw new BadRequestException("O campo 'name' é obrigatório.");
    }
}
