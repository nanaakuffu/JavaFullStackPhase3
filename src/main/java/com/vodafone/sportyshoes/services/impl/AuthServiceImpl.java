package com.vodafone.sportyshoes.services.impl;

import com.vodafone.sportyshoes.dao.UserDao;
import com.vodafone.sportyshoes.dtos.AuthDto;
import com.vodafone.sportyshoes.dtos.ProfileUpdateDto;
import com.vodafone.sportyshoes.dtos.SignupDto;
import com.vodafone.sportyshoes.entities.User;
import com.vodafone.sportyshoes.enums.UserRole;
import com.vodafone.sportyshoes.services.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;

    public AuthServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public String login(Model model, AuthDto authDto, HttpSession session) {
        Optional<User> optionalUser = userDao.findFirstByEmailAndDeletedFalse(authDto.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            boolean passwordValid = user.getPassword().equalsIgnoreCase(authDto.getPassword());
            if (passwordValid) {
                log.info("Logging in with info {}", user);
                session.setAttribute("user", user);
                return user.getUserType() == UserRole.ADMIN ? "redirect:/dashboard" : "redirect:/";
            }
        }
        model.addAttribute("error", "Invalid email or password");
        return "login";
    }

    @Override
    public String signup(Model model, SignupDto signupDto, RedirectAttributes redirectAttributes) {
        Optional<User> optionalUser = userDao.findFirstByEmailAndDeletedFalse(signupDto.getEmail());
        if (optionalUser.isPresent()) {
            model.addAttribute("error", "Email already taken");
            return "/signup";
        }
        else {
            User user = new User();
            if (!signupDto.getPassword().equals(signupDto.getPassword2())) {
                model.addAttribute("error", "Passwords do not match");
                return "/signup";
            } else {
                user.setName(signupDto.getName());
                user.setUserType(UserRole.USER);
                user.setPassword(signupDto.getPassword());
                user.setEmail(signupDto.getEmail());
                userDao.save(user);
                redirectAttributes.addFlashAttribute("message", "Account created successfully");
            }
        }
        return "redirect:/auth/login";
    }

    @Override
    public String updateProfileDetails(Model model, ProfileUpdateDto details, HttpSession httpSession, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (!user.getPassword().equals(details.getPassword())) {
            model.addAttribute("error", "Password is not valid");
            return "profile";
        }
        if (!ObjectUtils.isEmpty(details.getNewPassword())) {
            if (!details.getNewPassword2().equals(details.getNewPassword())) {
                model.addAttribute("error", "Passwords do not match");
                if (details.getNewPassword().length() < 3) {
                    model.addAttribute("error", "New Password is not valid. Use 3 or more characters");
                }
                return "profile";
            } else {
                user.setPassword(details.getNewPassword());
            }
        }
        user.setName(details.getName());
        httpSession.setAttribute("user", userDao.save(user));
        redirectAttributes.addFlashAttribute("message", "Profile updated successfully");

        if (user.getUserType() == UserRole.ADMIN) {
            return "redirect:/dashboard";
        }
        return "redirect:/";
    }
}
