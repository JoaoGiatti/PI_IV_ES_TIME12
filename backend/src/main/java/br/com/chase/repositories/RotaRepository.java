package br.com.chase.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import br.com.chase.models.Rota;

public interface RotaRepository extends MongoRepository<Rota, String> {}