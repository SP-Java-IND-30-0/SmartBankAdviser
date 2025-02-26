package com.star.bank.event;

import com.star.bank.model.product.Product;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SendRecommendationEvent extends ApplicationEvent {
    private final Product product;

    public SendRecommendationEvent(Object source, Product product) {
        super(source);
        this.product = product;
    }
}