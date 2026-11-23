package com.taskflow.controller;

import com.taskflow.dto.StatusRequest;
import com.taskflow.dto.TarefaRequest;
import com.taskflow.entity.Tarefa;
import com.taskflow.service.TarefaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tarefas")
@CrossOrigin(origins = "*") // SOMENTE ambiente de desenvolvimento (Expo Web/Angular local).
public class TarefaController {
    private final TarefaService service;

    public TarefaController(TarefaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Tarefa> listar() { return service.listarTodas(); }

    @GetMapping("/{id}")
    public Tarefa buscar(@PathVariable Long id) { return service.buscarPorId(id); }

    @PostMapping
    public ResponseEntity<Tarefa> criar(@Valid @RequestBody TarefaRequest dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dados));
    }

    @PutMapping("/{id}")
    public Tarefa atualizar(@PathVariable Long id, @Valid @RequestBody TarefaRequest dados) {
        return service.atualizar(id, dados);
    }

    @PatchMapping("/{id}/status")
    public Tarefa atualizarStatus(@PathVariable Long id, @Valid @RequestBody StatusRequest dados) {
        return service.atualizarStatus(id, dados.status());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
