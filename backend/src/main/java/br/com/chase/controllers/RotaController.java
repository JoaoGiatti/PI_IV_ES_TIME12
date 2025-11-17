package br.com.chase.controllers;

import br.com.chase.exceptions.RotaNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.chase.models.Rota;
import br.com.chase.services.RotaService;

import java.util.List;
import java.util.Map;

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

    // GET - Busca detalhes de uma rota
    @GetMapping("/{rid}")
    public ResponseEntity<?> getRotaById(@PathVariable String rid) {
        try {
            Rota rota = rotaService.getRotaById(rid);
            return ResponseEntity.ok(rota);
        } catch (RotaNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "routeId", rid
                    ));
        }
    }

    @DeleteMapping("/{rid}")
    public ResponseEntity<?> deletarRota(@PathVariable String rid) {
        try {
            rotaService.deletarRota(rid);
            return ResponseEntity.ok(Map.of(
                    "message", "Rota removida com sucesso!",
                    "routeId", rid
            ));
        } catch (RotaNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "routeId", rid
                    ));
        }
    }

    // GET - Lista todas as rotas(publicas)...
    @GetMapping("/public")
    public ResponseEntity<List<Rota>> getPublicRoutes(){
        List<Rota> rotas = rotaService.getPublicRoutes();
        return ResponseEntity.ok(rotas);
    }

    // GET - Lista todas as rotas de um usuario pelo UID...
    @GetMapping("/users/{uid}")
    public List<Rota> buscarRotasPorUsuario(@PathVariable String uid) {
        return rotaService.buscarRotasPorUsuario(uid);
    }

    // PUT - Atualiza o top3 de uma rota pelo RID => Rota ID...
    @PostMapping("/{rid}/record")
    public ResponseEntity<?> registerRecord(
            @PathVariable String rid,
            @RequestParam String uid,
            @RequestParam String timeString
    ) {
        return ResponseEntity.ok(rotaService.registerRecord(rid, uid, timeString));
    }
}
