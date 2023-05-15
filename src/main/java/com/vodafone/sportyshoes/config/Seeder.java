package com.vodafone.sportyshoes.config;

import com.vodafone.sportyshoes.dao.ProductCategoryDao;
import com.vodafone.sportyshoes.dao.ProductDao;
import com.vodafone.sportyshoes.dao.UserDao;
import com.vodafone.sportyshoes.entities.Product;
import com.vodafone.sportyshoes.entities.ProductCategory;
import com.vodafone.sportyshoes.entities.User;
import com.vodafone.sportyshoes.enums.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Configuration
public class Seeder {

    @Bean
    public CommandLineRunner seedAdmin(UserDao userDao) {
        return args -> {
            if (!userDao.findFirstByUserType(UserRole.ADMIN).isPresent()) {
                //create admin user
                userDao.save(User.builder()
                                .name("Admin User")
                        .email("admin@gmail.com")
                        .password("admin")
                        .userType(UserRole.ADMIN)
                        .build());
            }
        };
    }

    @Bean
    public CommandLineRunner seedProductCategories(ProductCategoryDao productCategoryDao) {
        return args -> {
            if (productCategoryDao.findAll().isEmpty()) {
                //generate dummy products
                productCategoryDao.saveAll(Stream.of("Devices", "Accessories", "Shoes", "Books", "Manuals")
                        .map(value -> ProductCategory.builder()
                                .name(value)
                                .build())
                        .collect(Collectors.toList()));
            }
        };
    }

    @Bean
    public CommandLineRunner seedProducts(ProductDao productDao, ProductCategoryDao productCategoryDao) {
        return args -> {
            Random random = new Random();
            if (productDao.findAll().isEmpty()) {
                //generate dummy products
                List<ProductCategory> productCategories = productCategoryDao.findAll();
                productDao.saveAll(IntStream.range(1, 101)
                        .mapToObj(value -> Product.builder()
                                .size(value)
                                .price(ThreadLocalRandom.current().nextInt(50, 9999))
                                .name(String.format("Product_%s", value))
                                .imageUrl("https://www.freepnglogos.com/uploads/shoes-png/dance-shoes-png-transparent-dance-shoes-images-5.png")
                                .description("Random product " + value)
                                .category(productCategories.get(random.nextInt(productCategories.size())))
                                .build())
                        .collect(Collectors.toList()));
            }
        };
    }
}
