package com.taskflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Dados recebidos no POST/PUT. O ID é gerenciado pelo servidor. */
public record TarefaRequest(
    @NotBlank(message = "O título é obrigatório")
    @Size(max = 255, message = "O título deve ter até 255 caracteres")
    String titulo,

    @Size(max = 2000, message = "A descrição deve ter até 2000 caracteres")
    String descricao,

    @NotBlank(message = "O responsável é obrigatório")
    @Size(max = 255, message = "O responsável deve ter até 255 caracteres")
    String responsavel,

    @NotBlank(message = "O status é obrigatório")
    @Pattern(regexp = "PENDENTE|EM_ANDAMENTO|CONCLUIDA",
             message = "O status deve ser PENDENTE, EM_ANDAMENTO ou CONCLUIDA")
    String status,

    @NotBlank(message = "A prioridade é obrigatória")
    @Pattern(regexp = "BAIXA|MEDIA|ALTA",
             message = "A prioridade deve ser BAIXA, MEDIA ou ALTA")
    String prioridade
) {}
