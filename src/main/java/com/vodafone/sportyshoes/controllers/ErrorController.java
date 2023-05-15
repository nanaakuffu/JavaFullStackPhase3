package com.vodafone.sportyshoes.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("error")
@Slf4j
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    public ErrorController() {
    }

    @RequestMapping("")
    public String error(HttpServletRequest  request, RedirectAttributes redirectAttributes) {
        Object m = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String message = "Some error occurred: ";
        if(m != null) {
            log.error("Error occurred: {}", m);
            message += m.toString();
        }
        redirectAttributes.addFlashAttribute("error", message);
        return "redirect:/";
    }
}
