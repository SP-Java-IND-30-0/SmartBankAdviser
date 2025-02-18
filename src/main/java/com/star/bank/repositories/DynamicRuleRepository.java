package com.star.bank.repositories;

import com.star.bank.model.product.DynamicRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DynamicRuleRepository extends JpaRepository {
    void save(DynamicRule dynamicRule);
    void deleteById(String productId);
    List<DynamicRule> findAll();

}
