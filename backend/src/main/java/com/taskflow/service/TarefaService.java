package com.taskflow.service;

import com.taskflow.dto.TarefaRequest;
import com.taskflow.entity.Tarefa;
import com.taskflow.exception.TarefaNaoEncontradaException;
import com.taskflow.repository.TarefaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TarefaService {
    private final TarefaRepository repository;

    public TarefaService(TarefaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Tarefa> listarTodas() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Tarefa buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException(id));
    }

    @Transactional
    public Tarefa criar(TarefaRequest dados) {
        Tarefa tarefa = new Tarefa();
        aplicarCampos(tarefa, dados);
        return repository.save(tarefa);
    }

    @Transactional
    public Tarefa atualizar(Long id, TarefaRequest dados) {
        Tarefa tarefa = buscarPorId(id);
        aplicarCampos(tarefa, dados);
        return repository.save(tarefa);
    }

    @Transactional
    public Tarefa atualizarStatus(Long id, String status) {
        Tarefa tarefa = buscarPorId(id);
        tarefa.setStatus(status);
        return repository.save(tarefa);
    }

    @Transactional
    public void excluir(Long id) {
        Tarefa tarefa = buscarPorId(id);
        repository.delete(tarefa);
    }

    private void aplicarCampos(Tarefa tarefa, TarefaRequest dados) {
        tarefa.setTitulo(dados.titulo().trim());
        tarefa.setDescricao(dados.descricao() == null ? "" : dados.descricao().trim());
        tarefa.setResponsavel(dados.responsavel().trim());
        tarefa.setStatus(dados.status());
        tarefa.setPrioridade(dados.prioridade());
    }
}
