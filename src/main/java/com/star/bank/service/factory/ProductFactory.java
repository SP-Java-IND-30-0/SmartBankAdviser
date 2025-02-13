package com.star.bank.service.factory;

import com.star.bank.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class ProductFactory {

    private final Rule rule1;
    private final Rule rule2;
    private final Rule rule3;
    private final Rule rule4;
    private final Rule rule5;
    private final Rule rule6;
    private final Rule rule7;
    private final Rule rule8;
    private final Rule rule9;

    @Autowired
    public ProductFactory(
            @Qualifier("ruleImplInvest500One") Rule rule1,
            @Qualifier("ruleImplInvest500Two") Rule rule2,
            @Qualifier("ruleImplInvest500Three") Rule rule3,
            @Qualifier("ruleImplSimpleCreditOne") Rule rule4,
            @Qualifier("ruleImplSimpleCreditTwo") Rule rule5,
            @Qualifier("ruleImplSimpleCreditThree") Rule rule6,
            @Qualifier("ruleImplTopSavingOne") Rule rule7,
            @Qualifier("ruleImplTopSavingTwo") Rule rule8,
            @Qualifier("ruleImplTopSavingThree") Rule rule9
    ) {
        this.rule1 = rule1;
        this.rule2 = rule2;
        this.rule3 = rule3;
        this.rule4 = rule4;
        this.rule5 = rule5;
        this.rule6 = rule6;
        this.rule7 = rule7;
        this.rule8 = rule8;
        this.rule9 = rule9;
    }

    public Product createInvest500Product() {
        return new Invest500Product(Set.of(rule1, rule2, rule3));
    }

    public Product createTopSavingProduct() {
        return new TopSavingProduct(Set.of(rule4, rule5, rule6));
    }

    public Product createSimpleCreditProduct() {
        return new SimpleCreditProduct(Set.of(rule7, rule8, rule9));
    }
}
