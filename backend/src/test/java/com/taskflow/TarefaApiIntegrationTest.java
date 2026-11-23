package com.taskflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.repository.TarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TarefaApiIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired TarefaRepository repository;
    @Autowired ObjectMapper mapper;

    @BeforeEach
    void limparBanco() { repository.deleteAll(); }

    private String dados() {
        return """
            {"titulo":"Estudar Spring Boot","descricao":"Tarefa de teste",
             "responsavel":"Matheus","status":"PENDENTE","prioridade":"ALTA"}
            """;
    }

    @Test
    void criaEListaTarefa() throws Exception {
        mvc.perform(post("/api/tarefas").contentType(MediaType.APPLICATION_JSON).content(dados()))
           .andExpect(status().isCreated()).andExpect(jsonPath("$.titulo").value("Estudar Spring Boot"));
        mvc.perform(get("/api/tarefas"))
           .andExpect(status().isOk()).andExpect(jsonPath("$[0].prioridade").value("ALTA"));
    }

    @Test
    void atualizaStatusSemAlterarOutrosCampos() throws Exception {
        String criacao = mvc.perform(post("/api/tarefas").contentType(MediaType.APPLICATION_JSON).content(dados()))
                .andReturn().getResponse().getContentAsString();
        Long id = mapper.readTree(criacao).get("id").asLong();
        mvc.perform(patch("/api/tarefas/" + id + "/status")
                .contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"CONCLUIDA\"}"))
           .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("CONCLUIDA"))
           .andExpect(jsonPath("$.titulo").value("Estudar Spring Boot"));
    }

    @Test
    void validaCamposERecursoInexistente() throws Exception {
        mvc.perform(post("/api/tarefas").contentType(MediaType.APPLICATION_JSON)
           .content("{\"titulo\":\"\",\"responsavel\":\"\",\"status\":\"ERRADO\",\"prioridade\":\"ALTA\"}"))
           .andExpect(status().isBadRequest());
        mvc.perform(get("/api/tarefas/999999")).andExpect(status().isNotFound());
        mvc.perform(patch("/api/tarefas/999999/status").contentType(MediaType.APPLICATION_JSON)
           .content("{\"status\":\"ERRADO\"}"))
           .andExpect(status().isBadRequest());
    }

    @Test
    void editaEExclui() throws Exception {
        String criacao = mvc.perform(post("/api/tarefas").contentType(MediaType.APPLICATION_JSON).content(dados()))
                .andReturn().getResponse().getContentAsString();
        Long id = mapper.readTree(criacao).get("id").asLong();
        String atualizado = dados().replace("Estudar Spring Boot", "Aprender API REST");
        mvc.perform(put("/api/tarefas/" + id).contentType(MediaType.APPLICATION_JSON).content(atualizado))
           .andExpect(status().isOk()).andExpect(jsonPath("$.titulo").value("Aprender API REST"));
        mvc.perform(delete("/api/tarefas/" + id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/tarefas/" + id)).andExpect(status().isNotFound());
    }
}
