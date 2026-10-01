package br.com.sgm.controller;

import br.com.sgm.model.Medicamento;
import br.com.sgm.service.MedicamentoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MedicamentoController {

    private final MedicamentoService service;


    public MedicamentoController(MedicamentoService service) {
        this.service = service;
    }


    @GetMapping("/")
    public String login() {
        return "login";
    }


    @GetMapping("/inicio")
    public String inicio() {
        return "inicio";
    }


    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastro";
    }


    @PostMapping("/salvar")
    public String salvar(Medicamento medicamento) {

        service.salvar(medicamento);

        return "redirect:/lista";
    }


    @GetMapping("/lista")
    public String lista(Model model) {

        List<Medicamento> medicamentos = service.listarTodos();

        model.addAttribute("medicamentos", medicamentos);

        return "lista";
    }


    @GetMapping("/estoque")
    public String estoque() {
        return "estoque";
    }


    @GetMapping("/horarios")
    public String horarios() {
        return "horarios";
    }


    @GetMapping("/editar")
    public String editar() {
        return "editar";
    }

}