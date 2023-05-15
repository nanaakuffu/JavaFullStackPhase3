package com.vodafone.sportyshoes.controllers;

import com.vodafone.sportyshoes.dtos.CheckoutDto;
import com.vodafone.sportyshoes.services.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = {"", "store"})
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("")
    public String index(Model model, HttpSession session) {
        storeService.getProducts(model, session);
        return "index";
    }

    @GetMapping("product/add-to-cart/{id}")
    public String addToCart(Model model, HttpSession session, @PathVariable long id) {
        return storeService.addToCart(model, session, id);
    }

    @GetMapping("checkout")
    public String checkout(Model model, HttpSession session) {
        return storeService.checkout(model, session);
    }

    @PostMapping("checkout")
    public String checkout(Model model, HttpSession session, CheckoutDto checkoutDto) {
        return storeService.checkout(model, session, checkoutDto);
    }

    @GetMapping("/remove-from-cart/{id}")
    public String removeFromCart(Model model, HttpSession session, @PathVariable long id) {
        return storeService.removeFromCart(model, session, id);
    }
}
