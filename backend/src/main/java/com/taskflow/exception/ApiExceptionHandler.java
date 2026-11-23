package com.taskflow.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    private Map<String, Object> erro(int status, String titulo, String mensagem) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status);
        body.put("erro", titulo);
        body.put("mensagem", mensagem);
        return body;
    }

    @ExceptionHandler(TarefaNaoEncontradaException.class)
    public ResponseEntity<Map<String, Object>> naoEncontrada(TarefaNaoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(erro(404, "Não encontrado", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> dadosInvalidos(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .distinct().collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(erro(400, "Dados inválidos", mensagem));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> jsonInvalido(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(erro(400, "JSON inválido", "Confira o formato do JSON enviado"));
    }
}
