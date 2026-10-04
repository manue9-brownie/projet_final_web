package com.SGM.controller;

import com.SGM.model.Usuario;
import com.SGM.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsuarioController {

    private final UsuarioRepository repository;

    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/registrar")
    public String paginaCadastro() {
        return "registrar";
    }

    @PostMapping("/registrar")
    public String registrar(@RequestParam String email,
                            @RequestParam String senha,
                            Model model) {
        email = email.trim().toLowerCase();

        if (email.isBlank() || senha.isBlank()) {
            model.addAttribute("erro", "Preencha o e-mail e a senha.");
            model.addAttribute("email", email);
            return "registrar";
        }

        if (repository.existsByEmail(email)) {
            model.addAttribute("erro", "Esse e-mail já está cadastrado.");
            model.addAttribute("email", email);
            return "registrar";
        }

        repository.save(new Usuario(email, senha));
        return "redirect:/?registrado";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String senha) {
        email = email.trim().toLowerCase();
        Usuario usuario = repository.findByEmailAndSenha(email, senha);

        if (usuario == null) {
            return "redirect:/?erro";
        }
        return "redirect:/inicio";
    }
}
