package com.SGM.service;


import com.SGM.model.Medicamento;
import org.springframework.stereotype.Service;
import com.SGM.repository.MedicamentoRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

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
        int total = 0;
        for (Medicamento medicamento : repository.findAll()) {
            total += Math.max(0, medicamento.getQuantidade());
        }
        return total;
    }

    public List<Medicamento> getProximosHorarios() {
        LocalDateTime agora = LocalDateTime.now();
        List<Medicamento> proximos = new ArrayList<>();
        for (Medicamento medicamento : repository.findAll()) {
            if (proximaDose(medicamento, agora) != null) {
                proximos.add(medicamento);
            }
        }

        // Put the soonest dose first with a simple selection sort.
        for (int i = 0; i < proximos.size(); i++) {
            int primeiro = i;
            for (int j = i + 1; j < proximos.size(); j++) {
                LocalDateTime horarioJ = proximaDose(proximos.get(j), agora);
                LocalDateTime horarioPrimeiro = proximaDose(proximos.get(primeiro), agora);
                if (horarioJ.isBefore(horarioPrimeiro)) {
                    primeiro = j;
                }
            }
            Medicamento temporario = proximos.get(i);
            proximos.set(i, proximos.get(primeiro));
            proximos.set(primeiro, temporario);
        }
        return proximos;
    }

    public long getMinutosAteProximaDose(List<Medicamento> proximos) {
        if (proximos == null || proximos.isEmpty()) {
            return -1;
        }
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime proxima = proximaDose(proximos.get(0), agora);
        if (proxima == null) {
            return -1;
        }
        return Math.max(0, Duration.between(agora, proxima).toMinutes());
    }

    public List<Medicamento> getResumoEstoque() {
        List<Medicamento> resumo = new ArrayList<>(repository.findAll());
        for (int i = 0; i < resumo.size(); i++) {
            int menor = i;
            for (int j = i + 1; j < resumo.size(); j++) {
                if (resumo.get(j).getQuantidade() < resumo.get(menor).getQuantidade()) {
                    menor = j;
                }
            }
            Medicamento temporario = resumo.get(i);
            resumo.set(i, resumo.get(menor));
            resumo.set(menor, temporario);
        }
        if (resumo.size() > 3) {
            return new ArrayList<>(resumo.subList(0, 3));
        }
        return resumo;
    }

    public List<Medicamento> getMedicamentosEstoqueBaixo() {
        List<Medicamento> baixo = new ArrayList<>();
        for (Medicamento medicamento : repository.findAll()) {
            if (medicamento.getQuantidade() <= 5) {
                baixo.add(medicamento);
            }
        }
        for (int i = 0; i < baixo.size(); i++) {
            int menor = i;
            for (int j = i + 1; j < baixo.size(); j++) {
                if (baixo.get(j).getQuantidade() < baixo.get(menor).getQuantidade()) {
                    menor = j;
                }
            }
            Medicamento temporario = baixo.get(i);
            baixo.set(i, baixo.get(menor));
            baixo.set(menor, temporario);
        }
        return baixo;
    }

    public List<String> getAlertasProximasDoses() {
        List<String> alertas = new ArrayList<>();
        List<Medicamento> proximos = getProximosHorarios();
        for (Medicamento medicamento : proximos) {
            if (medicamento.getQuantidade() <= 5) {
                if (medicamento.getQuantidade() <= 0) {
                    alertas.add("Não há estoque para a próxima dose de " + medicamento.getNome() + ".");
                } else {
                    alertas.add("Estoque baixo para a próxima dose de " + medicamento.getNome() + ".");
                }
                if (alertas.size() == 3) {
                    break;
                }
            }
        }
        return alertas;
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
