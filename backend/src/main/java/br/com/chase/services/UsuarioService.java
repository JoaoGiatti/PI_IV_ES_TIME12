package br.com.chase.services;

import br.com.chase.exceptions.NotFoundException;
import br.com.chase.exceptions.UserAlreadyExistsException;
import br.com.chase.models.Usuario;
import br.com.chase.repositories.UsuarioRepository;
import br.com.chase.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    private Optional<Usuario> buscarPorUid(String uid) {
        return usuarioRepository.findById(uid);
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario.getUid() == null || usuario.getUid().isBlank())
            throw new BadRequestException("O campo 'uid' (UID Firebase) é obrigatório.");

        if (usuario.getEmail() == null || usuario.getEmail().isBlank())
            throw new BadRequestException("O campo 'email' é obrigatório.");

        if (usuario.getDisplayName() == null || usuario.getDisplayName().isBlank())
            throw new BadRequestException("O campo 'displayName' é obrigatório.");
    }


    public Usuario criarUsuario(Usuario usuario) {
        validarUsuario(usuario);

        if (usuarioRepository.existsById(usuario.getUid())) {
            throw new UserAlreadyExistsException("Usuário já cadastrado.");
        }

        usuario.setCreatedAt(new Date());
        usuario.setMedals(new ArrayList<>());
        usuario.setTotalCalories(0);
        usuario.setTotalDistance(0);
        usuario.setTotalTime(0);

        return usuarioRepository.save(usuario);
    }

    public Usuario buscarUsuario(String uid) {
        return usuarioRepository.findById(uid)
                .orElseThrow(() -> new BadRequestException("Usuário com UID " + uid + " não encontrado."));
    }

    public Usuario atualizarUsuario(String uid, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(uid)
                .orElseThrow(() -> new NotFoundException("Usuário com UID " + uid + " não encontrado."));

        // Atualiza apenas os campos que foram enviados no body
        if (usuarioAtualizado.getDisplayName() != null && !usuarioAtualizado.getDisplayName().isBlank()) {
            usuarioExistente.setDisplayName(usuarioAtualizado.getDisplayName());
        }

        if (usuarioAtualizado.getPhotoUrl() != null && !usuarioAtualizado.getPhotoUrl().isBlank()) {
            usuarioExistente.setPhotoUrl(usuarioAtualizado.getPhotoUrl());
        }

        if (usuarioAtualizado.getBio() != null) {
            usuarioExistente.setBio(usuarioAtualizado.getBio());
        }

        return usuarioRepository.save(usuarioExistente);
    }
}
