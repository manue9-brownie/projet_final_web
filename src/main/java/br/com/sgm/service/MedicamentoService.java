package br.com.sgm.service;

import br.com.sgm.model.Medicamento;
import br.com.sgm.repository.MedicamentoRepository;
import org.springframework.stereotype.Service;

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