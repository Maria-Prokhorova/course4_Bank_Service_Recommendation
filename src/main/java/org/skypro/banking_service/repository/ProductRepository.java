package org.skypro.banking_service.repository;

import org.skypro.banking_service.dto.ProductInfo;

import java.util.Optional;

public interface ProductRepository {
    Optional<ProductInfo> findProductInfoById(String productId);
}
