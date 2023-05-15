package com.vodafone.sportyshoes.controllers;

import com.vodafone.sportyshoes.dtos.AuthDto;
import com.vodafone.sportyshoes.dtos.ProfileUpdateDto;
import com.vodafone.sportyshoes.dtos.SignupDto;
import com.vodafone.sportyshoes.services.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "login";
    }

    @GetMapping("profile")
    public String profile() {
        return "profile";
    }

    @PostMapping("/login")
    public String login(Model model, AuthDto authDto, HttpSession session) {
        return authService.login(model, authDto, session);
    }

    @PostMapping("/signup")
    public String signup(Model model, SignupDto details, RedirectAttributes redirectAttributes) {
        return authService.signup(model, details, redirectAttributes);
    }

    @PostMapping("/profile")
    public String profile(Model model, ProfileUpdateDto details, HttpSession session, RedirectAttributes redirectAttributes) {
        return authService.updateProfileDetails(model, details, session, redirectAttributes);
    }
}
