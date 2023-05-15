package com.vodafone.sportyshoes.controllers;

import com.vodafone.sportyshoes.dtos.OrderSearchDto;
import com.vodafone.sportyshoes.dtos.ProductCategoryDto;
import com.vodafone.sportyshoes.dtos.ProductDto;
import com.vodafone.sportyshoes.services.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("dashboard")
public class AdminDashboard {

    private final DashboardService dashboardService;

    public AdminDashboard(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("")
    public String dashboard(Model model, HttpSession session) {
        return dashboardService.prepareDashboard(model, session);
    }

    @GetMapping("users")
    public String users(Model model, HttpSession session, @RequestParam(required = false) String query) {
        return dashboardService.displayUsers(model, session, query);
    }

    @GetMapping("users/delete/{id}")
    public String deleteUser(Model model, HttpSession session, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        return dashboardService.deleteUser(model, session, id, redirectAttributes);
    }

    @GetMapping("orders")
    public String orders(Model model, HttpSession session) {
        return dashboardService.displayOrders(model, session);
    }

    @GetMapping("products")
    public String products(Model model, HttpSession session) {
        return dashboardService.displayProducts(model, session);
    }

    @PostMapping("products")
    public String editProducts(Model model, HttpSession session, ProductDto product, RedirectAttributes redirectAttributes) {
        return dashboardService.upsertProduct(model, session, product, redirectAttributes);
    }


    @GetMapping("products/edit/{id}")
    public String editProduct(Model model, HttpSession session, @PathVariable Long id) {
        return dashboardService.editProduct(model, session, id);
    }


    @GetMapping("product-categories/edit/{id}")
    public String editProductCategories(Model model, HttpSession session, @PathVariable Long id) {
        return dashboardService.editProductCategories(model, session, id);
    }

    @GetMapping("products/delete/{id}")
    public String deleteProduct(Model model, HttpSession session, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        return dashboardService.deleteProduct(model, session, id, redirectAttributes);
    }
    @GetMapping("product-categories/delete/{id}")
    public String deleteProductCategory(Model model, HttpSession session, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        return dashboardService.deleteProductCategory(model, session, id, redirectAttributes);
    }

    @GetMapping("product-categories")
    public String productCategories(Model model, HttpSession session) {
        return dashboardService.displayProductCategories(model, session);
    }

    @PostMapping("product-categories")
    public String editProductCategories(Model model, HttpSession session, ProductCategoryDto categoryDto, RedirectAttributes redirectAttributes) {
        return dashboardService.upsertProductCategory(model, session, categoryDto, redirectAttributes);
    }

    @PostMapping("orders")
    public String orders(Model model, HttpSession session, OrderSearchDto orderSearchDto) {
        return dashboardService.displayOrders(model, session, orderSearchDto);
    }


}
