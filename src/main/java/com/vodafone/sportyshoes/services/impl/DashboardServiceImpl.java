package com.vodafone.sportyshoes.services.impl;

import com.vodafone.sportyshoes.dao.*;
import com.vodafone.sportyshoes.dtos.OrderSearchDto;
import com.vodafone.sportyshoes.dtos.ProductCategoryDto;
import com.vodafone.sportyshoes.dtos.ProductDto;
import com.vodafone.sportyshoes.entities.*;
import com.vodafone.sportyshoes.enums.UserRole;
import com.vodafone.sportyshoes.services.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final UserDao userDao;
    private final ProductDao productDao;
    private final ProductCategoryDao productCategoryDao;
    private final PurchaseDao purchaseDao;
    private final CartProductRepository cartProductRepository;

    public DashboardServiceImpl(UserDao userDao, ProductDao productDao, ProductCategoryDao productCategoryDao,
                                PurchaseDao purchaseDao,
                                CartProductRepository cartProductRepository) {
        this.userDao = userDao;
        this.productDao = productDao;
        this.productCategoryDao = productCategoryDao;
        this.purchaseDao = purchaseDao;
        this.cartProductRepository = cartProductRepository;
    }

    private static boolean isNotAdmin(HttpSession session) {
        return session.getAttribute("user") == null || ((User) session.getAttribute("user")).getUserType() != UserRole.ADMIN;
    }

    @Override
    public String prepareDashboard(Model model, HttpSession session) {
        if (isNotAdmin(session)) {
            return "redirect:/";
        }
        model.addAttribute("userCount", userDao.countByDeletedFalse());
        model.addAttribute("productCount", productDao.countByDeletedFalseAndCategoryDeletedFalse());
        model.addAttribute("productCategoryCount", productCategoryDao.countByDeletedFalse());
        model.addAttribute("revenue", purchaseDao.getRevenue());
        model.addAttribute("itemsSold", cartProductRepository.getItemsSold());
        return "dashboard-statistics";

    }

    @Override
    public String displayUsers(Model model, HttpSession session, String query) {
        if (isNotAdmin(session)) {
            return "redirect:/";
        }
        if (query == null) {
            model.addAttribute("users", userDao.findAllByDeletedFalse());
        } else {
            model.addAttribute("users", userDao.findAllByNameContainingIgnoreCase(query));
            model.addAttribute("searching", true);
            model.addAttribute("query", query);
        }
        return "dashboard-users";
    }

    public String deleteUser(Model model, HttpSession session, Long id, RedirectAttributes redirectAttributes) {
        if (isNotAdmin(session)) {
            return "redirect:/";
        }
        userDao.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "User deleted successfully");
        return "redirect:/dashboard/users";
    }

    public String deleteProductCategory(Model model, HttpSession session, Long id, RedirectAttributes redirectAttributes) {
        if (isNotAdmin(session)) {
            return "redirect:/";
        }
        productCategoryDao.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Product category deleted successfully");
        return "redirect:/dashboard/product-categories";
    }

    public String deleteProduct(Model model, HttpSession session, Long id, RedirectAttributes redirectAttributes) {
        if (isNotAdmin(session)) {
            return "redirect:/";
        }
        productDao.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Product deleted successfully");
        return "redirect:/dashboard/products";
    }

    @Override
    public String displayOrders(Model model, HttpSession session) {
        if (isNotAdmin(session)) {
            return "redirect:/";
        }
        model.addAttribute("orders", purchaseDao.findAll());
        model.addAttribute("categories", productCategoryDao.findAllByDeletedFalse());
        return "dashboard-orders";
    }

    @Override
    public String displayOrders(Model model, HttpSession session, OrderSearchDto orderSearchDto) {
        if (isNotAdmin(session)) {
            return "redirect:/";
        }
        LocalDateTime start = LocalDateTime.ofInstant(orderSearchDto.getStartDate().toInstant(), ZoneId.systemDefault());
        LocalDateTime end = LocalDateTime.ofInstant(orderSearchDto.getEndDate().toInstant(), ZoneId.systemDefault());
        List<Purchase> purchases =  purchaseDao.findAllByDateCreatedBetween(start, end)
                .stream().peek(p -> {
                    AtomicLong amt = new AtomicLong();
                    List<CartProduct> cartProductsInCategory = p.getProducts()
                            .stream()
                            .filter(cartProduct -> {
                                boolean valid = Objects.equals(cartProduct.getProduct().getCategory().getId(), orderSearchDto.getCategoryId());
                                if(valid) {
                                    amt.addAndGet((cartProduct.getQuantity() * cartProduct.getProduct().getPrice()));
                                }
                                return valid;
                            })
                            .collect(Collectors.toList());
                    p.setAmountPaid(amt.get()/100.0);
                    p.setProducts(cartProductsInCategory);
                })
                .filter(purchase -> !purchase.getProducts().isEmpty())
                .collect(Collectors.toList());
        model.addAttribute("orders", purchases);
        model.addAttribute("categories", productCategoryDao.findAllByDeletedFalse());
        model.addAttribute("message", "Orders retrieved successfully");
        return "dashboard-orders";
    }

    @Override
    public String displayProducts(Model model, HttpSession session) {
        if (isNotAdmin(session)) {
            return "redirect:/";
        }
        model.addAttribute("products", productDao.findAllByDeletedFalseAndCategoryDeletedFalse());
        model.addAttribute("categories", productCategoryDao.findAllByDeletedFalse());
        return "dashboard-products";
    }

    @Override
    public String displayProductCategories(Model model, HttpSession session) {
        if (isNotAdmin(session)) {
            return "redirect:/";
        }
        model.addAttribute("productCategories", productCategoryDao.findAllByDeletedFalse());
        return "dashboard-product-categories";
    }

    @Override
    public String upsertProduct(Model model, HttpSession session, ProductDto product, RedirectAttributes redirectAttributes) {
        if (product.getId() != null) {
            Optional<Product> p = productDao.findById(product.getId());
            if (p.isPresent()) {
                BeanUtils.copyProperties(product, p.get());
                try {
                    long id = Long.parseLong(product.getCategory());
                    Optional<ProductCategory> cat = productCategoryDao.findById(id);
                    cat.ifPresent(p.get()::setCategory);
                } catch (Exception ignored) {
                }
                productDao.save(p.get());
                redirectAttributes.addFlashAttribute("message", "Product updated Successfully");
            }
        } else {
            Product p = new Product();
            BeanUtils.copyProperties(product, p);
            try {
                long id = Long.parseLong(product.getCategory());
                Optional<ProductCategory> cat = productCategoryDao.findById(id);
                cat.ifPresent(p::setCategory);
            } catch (Exception ignored) {
            }
            redirectAttributes.addFlashAttribute("message", "Product added Successfully");
            productDao.save(p);
        }
        return "redirect:/dashboard/products";
    }

    public String editProduct(Model model, HttpSession session, Long id) {
        Optional<Product> p = productDao.findById(id);
        model.addAttribute("product", p.orElse(null));
        return displayProducts(model, session);
    }

    public String editProductCategories(Model model, HttpSession session, Long id) {
        Optional<ProductCategory> p = productCategoryDao.findById(id);
        p.ifPresent(category -> model.addAttribute("productCategory", category));
        return displayProductCategories(model, session);
    }

    public String upsertProductCategory(Model model, HttpSession session, ProductCategoryDto categoryDto, RedirectAttributes redirectAttributes) {
        if (categoryDto.getId() != null) {
            Optional<ProductCategory> p = productCategoryDao.findById(categoryDto.getId());
            if (p.isPresent()) {
                p.get().setName(categoryDto.getName());
                productCategoryDao.save(p.get());
                redirectAttributes.addFlashAttribute("message", "Product category updated successfully");
            }
        } else {
            ProductCategory p = new ProductCategory();
            p.setName(categoryDto.getName());
            productCategoryDao.save(p);
            redirectAttributes.addFlashAttribute("message", "Product category added successfully");
        }
        return "redirect:/dashboard/product-categories";
    }


}
