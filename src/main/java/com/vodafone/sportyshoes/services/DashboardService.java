package com.vodafone.sportyshoes.services;

import com.vodafone.sportyshoes.dtos.OrderSearchDto;
import com.vodafone.sportyshoes.dtos.ProductCategoryDto;
import com.vodafone.sportyshoes.dtos.ProductDto;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

public interface DashboardService {
    String prepareDashboard(Model model, HttpSession session);

    String displayUsers(Model model, HttpSession session, String query);

    String displayProductCategories(Model model, HttpSession session);

    String displayProducts(Model model, HttpSession session);

    String displayOrders(Model model, HttpSession session);

    String deleteUser(Model model, HttpSession session, Long id, RedirectAttributes redirectAttributes);

    String upsertProduct(Model model, HttpSession session, ProductDto product, RedirectAttributes redirectAttributes);

    String editProduct(Model model, HttpSession session, Long id);

    String upsertProductCategory(Model model, HttpSession session, ProductCategoryDto categoryDto, RedirectAttributes redirectAttributes);

    String editProductCategories(Model model, HttpSession session, Long id);

    String deleteProductCategory(Model model, HttpSession session, Long id, RedirectAttributes redirectAttributes);
    String deleteProduct(Model model, HttpSession session, Long id, RedirectAttributes redirectAttributes);

    String displayOrders(Model model, HttpSession session, OrderSearchDto orderSearchDto);
}
