package com.vodafone.sportyshoes.services;

import com.vodafone.sportyshoes.dtos.CheckoutDto;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

public interface StoreService {
    String checkout(Model model, HttpSession session);

    void search(String query, Model model);

    void getProducts(Model model, HttpSession session);

    String addToCart(Model model, HttpSession session, long id);

    String removeFromCart(Model model, HttpSession session, long id);

    String checkout(Model model, HttpSession session, CheckoutDto checkoutDto);
}
