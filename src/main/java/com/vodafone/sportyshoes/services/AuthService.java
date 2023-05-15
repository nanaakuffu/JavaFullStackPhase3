package com.vodafone.sportyshoes.services;

import com.vodafone.sportyshoes.dtos.AuthDto;
import com.vodafone.sportyshoes.dtos.ProfileUpdateDto;
import com.vodafone.sportyshoes.dtos.SignupDto;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

public interface AuthService {
    String login(Model model, AuthDto authDto, HttpSession session);

    String signup(Model model, SignupDto signupDto, RedirectAttributes redirectAttributes);

    String updateProfileDetails(Model model, ProfileUpdateDto details, HttpSession httpSession, RedirectAttributes redirectAttributes);
}
