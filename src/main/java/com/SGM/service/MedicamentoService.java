package com.SGM.service;


import com.SGM.model.Medicamento;
import org.springframework.stereotype.Service;
import com.SGM.repository.MedicamentoRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicamentoService {

    private final MedicamentoRepository repository;


    public MedicamentoService(MedicamentoRepository repository) {
        this.repository = repository;
    }


    // Cadastrar
    public Medicamento salvar(Medicamento medicamento) {
        return repository.save(medicamento);
    }


    // Consultar todos
    public List<Medicamento> listarTodos() {
        return repository.findAll();
    }

    public long getTotalMedicamentos() {
        return repository.count();
    }

    public int getTotalUnidades() {
        return repository.findAll().stream()
                .mapToInt(medicamento -> Math.max(0, medicamento.getQuantidade()))
                .sum();
    }

    public List<Medicamento> getProximosHorarios() {
        LocalDateTime agora = LocalDateTime.now();
        return repository.findAll().stream()
                .filter(medicamento -> proximaDose(medicamento, agora) != null)
                .sorted(Comparator.comparing(medicamento -> proximaDose(medicamento, agora)))
                .collect(Collectors.toList());
    }

    public List<Medicamento> getResumoEstoque() {
        return repository.findAll().stream()
                .sorted(Comparator.comparingInt(Medicamento::getQuantidade))
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<Medicamento> getMedicamentosEstoqueBaixo() {
        return repository.findAll().stream()
                .filter(medicamento -> medicamento.getQuantidade() <= 5)
                .sorted(Comparator.comparingInt(Medicamento::getQuantidade))
                .collect(Collectors.toList());
    }

    public List<String> getAlertasProximasDoses() {
        return getProximosHorarios().stream()
                .limit(3)
                .filter(medicamento -> medicamento.getQuantidade() <= 5)
                .map(medicamento -> medicamento.getQuantidade() <= 0
                        ? "Não há estoque para a próxima dose de " + medicamento.getNome() + "."
                        : "Estoque baixo para a próxima dose de " + medicamento.getNome() + ".")
                .collect(Collectors.toList());
    }

    public String validarMedicamento(Medicamento medicamento) {
        if (medicamento.getNome() == null || medicamento.getNome().isBlank()) {
            return "Informe o nome do medicamento.";
        }
        if (medicamento.getQuantidade() < 0) {
            return "A quantidade em estoque não pode ser negativa.";
        }
        if (medicamento.getHorario() != null && !medicamento.getHorario().isBlank()) {
            try {
                LocalTime.parse(medicamento.getHorario());
            } catch (DateTimeParseException e) {
                return "Informe um horário válido no formato HH:mm.";
            }
        }
        return null;
    }

    private LocalDateTime proximaDose(Medicamento medicamento, LocalDateTime agora) {
        if (medicamento.getHorario() == null || medicamento.getHorario().isBlank()) {
            return null;
        }
        try {
            LocalTime horario = LocalTime.parse(medicamento.getHorario());
            LocalDateTime proxima = agora.toLocalDate().atTime(horario);
            return proxima.isBefore(agora) ? proxima.plusDays(1) : proxima;
        } catch (DateTimeParseException e) {
            return null;
        }
    }


    // Buscar por ID
    public Medicamento buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }


    // Excluir
    public void excluir(Long id) {
        repository.deleteById(id);
    }


    // Editar
    public Medicamento editar(Medicamento medicamento) {
        return repository.save(medicamento);
    }

}
