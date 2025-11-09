package br.com.chase.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import br.com.chase.models.Rota;
import java.util.List;

@Repository
public interface RotaRepository extends MongoRepository<Rota, String> {
    List<Rota> findByUid(String criadorId);
}
