package br.com.chase.controllers;

import org.springframework.web.bind.annotation.*;
import br.com.chase.models.Rota;
import br.com.chase.services.RotaService;

import java.util.List;

@RestController
@RequestMapping("/routes")
public class RotaController {

    private final RotaService rotaService;

    public RotaController(RotaService rotaService) {
        this.rotaService = rotaService;
    }

    // POST - Cria uma rota...
    @PostMapping("/create")
    public Rota criarRota(@RequestBody Rota rota) {
        return rotaService.criarRota(rota);
    }

    // GET - Mostra detalhes de uma rota pelo RID => Rota ID...
    //@GetMapping("/{rid}")
    // public ?? buscarRota() {} // TODO

    // GET - Lista todas as rotas(publicas)...
    // @GetMapping("/public")
    // public ?? buscarRotasPublicas() {} // TODO

    // GET - Lista todas as rotas de um usuario pelo UID...
    @GetMapping("/users/{uid}")
    public List<Rota> buscarRotasPorUsuario(@PathVariable String uid) {
        return rotaService.buscarRotasPorUsuario(uid);
    }

    // PUT - Atualiza o top3 de uma rota pelo RID => Rota ID...
    // @PutMapping("/{rid}/attempt")
    // public ?? atualizarRota() {}  // TODO
}
