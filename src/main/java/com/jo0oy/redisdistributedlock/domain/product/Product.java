package com.jo0oy.redisdistributedlock.domain.product;

import com.jo0oy.redisdistributedlock.common.exception.InvalidParamException;
import com.jo0oy.redisdistributedlock.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
@Entity
public class Product extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private Long availableStock;

    @Builder
    private Product(String productName, Long availableStock) {
        if(!StringUtils.hasText(productName))
            throw new InvalidParamException("invalid productName");
        if(Objects.isNull(availableStock) || availableStock < 10)
            throw new InvalidParamException("invalid availableStock");

        this.productName = productName;
        this.availableStock = availableStock;
    }

    public void decrease(Long quantity) {
        validateAvailableStock(quantity);
        this.availableStock -= quantity;
    }

    private void validateAvailableStock(Long quantity) {
        if(this.availableStock - quantity < 0)
            throw new IllegalArgumentException("재고 부족");
    }
}
