package br.com.upa.notifier.controller;

import br.com.upa.notifier.dto.AlertaSemAltaEvent;
import br.com.upa.notifier.service.NotifierService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifier")
public class NotifierController {

    private final NotifierService service;

    public NotifierController(NotifierService service) {
        this.service = service;
    }

    @PostMapping("/alerta")
    public ResponseEntity<Void> enviar(@Valid @RequestBody AlertaSemAltaEvent alerta) {
        service.enviarAlerta(alerta);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("Conexões WebSocket: " + service.totalConexoes());
    }
}
