package com.SGM.controller;


import com.SGM.model.Medicamento;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.SGM.service.MedicamentoService;

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
    public String inicio(Model model) {
        model.addAttribute("totalMedicamentos", service.getTotalMedicamentos());
        model.addAttribute("totalUnidades", service.getTotalUnidades());
        model.addAttribute("proximosHorarios", service.getProximosHorarios());
        model.addAttribute("resumoEstoque", service.getResumoEstoque());
        model.addAttribute("alertasProximasDoses", service.getAlertasProximasDoses());
        return "inicio";
    }


    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastro";
    }


    @PostMapping("/salvar")
    public String salvar(Medicamento medicamento, Model model) {
        String erro = service.validarMedicamento(medicamento);
        if (erro != null) {
            model.addAttribute("erro", erro);
            return "cadastro";
        }

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
    public String estoque(Model model) {
        model.addAttribute("medicamentos", service.listarTodos());
        model.addAttribute("totalMedicamentos", service.getTotalMedicamentos());
        model.addAttribute("totalUnidades", service.getTotalUnidades());
        model.addAttribute("medicamentosEstoqueBaixo", service.getMedicamentosEstoqueBaixo());
        return "estoque";
    }


    @GetMapping("/horarios")
    public String horarios(Model model) {
        model.addAttribute("proximosHorarios", service.getProximosHorarios());
        model.addAttribute("alertasProximasDoses", service.getAlertasProximasDoses());
        return "horario";
    }


    @GetMapping("/editar")
    public String editar(@RequestParam Long id, Model model) {
        Medicamento medicamento = service.buscarPorId(id);
        if (medicamento == null) {
            return "redirect:/lista";
        }
        model.addAttribute("medicamento", medicamento);
        return "editar";
    }

    @PostMapping("/editar")
    public String atualizar(Medicamento medicamento, Model model) {
        String erro = service.validarMedicamento(medicamento);
        if (erro != null) {
            model.addAttribute("erro", erro);
            model.addAttribute("medicamento", medicamento);
            return "editar";
        }
        service.editar(medicamento);
        return "redirect:/lista";
    }

    @GetMapping("/excluir")
    public String excluir(@RequestParam Long id) {
        service.excluir(id);
        return "redirect:/lista";
    }

}
