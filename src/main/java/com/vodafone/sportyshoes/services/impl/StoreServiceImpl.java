package com.vodafone.sportyshoes.services.impl;

import com.vodafone.sportyshoes.dao.CartProductRepository;
import com.vodafone.sportyshoes.dao.ProductDao;
import com.vodafone.sportyshoes.dao.PurchaseDao;
import com.vodafone.sportyshoes.dao.UserDao;
import com.vodafone.sportyshoes.dtos.CheckoutDto;
import com.vodafone.sportyshoes.entities.CartProduct;
import com.vodafone.sportyshoes.entities.Product;
import com.vodafone.sportyshoes.entities.Purchase;
import com.vodafone.sportyshoes.entities.User;
import com.vodafone.sportyshoes.services.StoreService;
import com.vodafone.sportyshoes.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StoreServiceImpl implements StoreService {
    private final PurchaseDao purchaseDao;
    private final CartProductRepository cartProductRepository;
    private final UserDao userDao;

    private final ProductDao productDao;

    public StoreServiceImpl(ProductDao productDao,
                            UserDao userDao,
                            CartProductRepository cartProductRepository,
                            PurchaseDao purchaseDao) {
        this.productDao = productDao;
        this.userDao = userDao;
        this.cartProductRepository = cartProductRepository;
        this.purchaseDao = purchaseDao;
    }

    @Override
    public void getProducts(Model model, HttpSession session) {
//        log.info("Fetching products");
        List<Product> productList = productDao.findAllByDeletedFalseAndCategoryDeletedFalse();
        model.addAttribute("products", productList);
        if (session.getAttribute("user") != null) {
            Object o = session.getAttribute("user");
            User user = (User) o;
            user = userDao.findById(user.getId()).orElse(null);
            log.info("User is {}", user);
            session.setAttribute("user", user);
        }
    }

    @Override
    public String addToCart(Model model, HttpSession session, long id) {
        User user = (User) session.getAttribute("user");
        if(ObjectUtils.isEmpty(user.getCart())){
            user.setCart(new ArrayList<>());
        }
        Optional<CartProduct> cartProduct = user.getCart().stream().filter(p -> p.getProduct().getId() == id).findFirst();
        CartProduct cp = cartProduct.orElse(null);
        if(cp != null) {
            cp.setQuantity(cp.getQuantity() + 1);
        }
        if (cp == null) {
            Optional<Product> optionalProduct = productDao.findById(id);
            if (optionalProduct.isPresent()) {
                Product p = optionalProduct.get();
                cp = new CartProduct();
                cp.setProduct(p);
                cp.setQuantity(1);
                cp.setUser(user);
                user.getCart().add(cartProductRepository.save(cp));
            }
        }
        if(cp != null) {
            cartProductRepository.save(cp);
        }
        model.addAttribute("message", "Added to cart successfully");
        return "redirect:/store";
    }


    @Override
    public String checkout(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user == null) {
            return "redirect:/";
        }
//        Cart cart = cartDao.findFirstByUser(user);
        user = userDao.findById(user.getId()).orElse(null);
        session.setAttribute("user", user);
        model.addAttribute("cart", user.getCart());
        return "checkout";
    }

    @Override
    public void search(String query, Model modelAndView) {

    }

    public String removeFromCart(Model model, HttpSession session, long id) {
        cartProductRepository.deleteById(id);
        return "redirect:/store/checkout";
    }

    public String checkout(Model model, HttpSession session, CheckoutDto checkoutDto) {
        User user = (User) session.getAttribute("user");
        user = userDao.findById(user.getId()).orElse(null);
        Purchase purchase = new Purchase();
        purchase.setAmountPaid(Utils.getTotalCost(user.getCart()));
        purchase.setUser(user);
        purchase.setPaymentChannel(checkoutDto.getPaymentChannel());
        purchase = purchaseDao.save(purchase);
        Purchase finalPurchase = purchase;
        purchase.setProducts(user.getCart().stream().peek(p-> {
            p.setPurchase(finalPurchase);
            p.setUser(null);
        }).collect(Collectors.toList()));
        user.setCart(new ArrayList<>());
        cartProductRepository.saveAll(purchase.getProducts());
        user = userDao.save(user);
        session.setAttribute("user", user);
        model.addAttribute("message", "Checkout completed Successfully");
        return "redirect:/";
    }


}
