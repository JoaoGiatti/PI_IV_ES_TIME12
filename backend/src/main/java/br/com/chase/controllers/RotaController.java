package br.com.chase.controllers;

import org.springframework.web.bind.annotation.*;
import br.com.chase.models.Rota;
import br.com.chase.services.RotaService;

import java.util.List;

@RestController
@RequestMapping("/rotas")
public class RotaController {

    private final RotaService rotaService;

    public RotaController(RotaService rotaService) {
        this.rotaService = rotaService;
    }

    @PostMapping("/criar")
    public Rota criarRota(@RequestBody Rota rota) {
        return rotaService.criarRota(rota);
    }

    @GetMapping("/usuario/{userId}")
    public List<Rota> listarRotasPorUsuario(@PathVariable String userId) {
        return rotaService.listarRotasPorUsuario(userId);
    }
}
