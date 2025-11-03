package br.com.chase.repositories;

import br.com.chase.models.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    boolean existsByEmail(String email);
}
