package org.skypro.banking_service.repository.impl;

import org.skypro.banking_service.dto.ProductInfo;
import org.skypro.banking_service.repository.ProductRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<ProductInfo> findProductInfoById(String productId) {
        return jdbcTemplate.query(
                """
                        SELECT id, name, description
                        FROM products
                        WHERE id = ?
                        """,
                (rs, rowNum) -> new ProductInfo(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("description")
                ),
                productId
        ).stream().findFirst();
        // Выдает информации о продукте по его id.
    }

}
