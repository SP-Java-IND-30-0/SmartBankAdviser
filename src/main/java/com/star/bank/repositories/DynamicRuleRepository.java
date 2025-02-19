package com.star.bank.repositories;

import com.star.bank.model.product.DynamicRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DynamicRuleRepository extends JpaRepository<DynamicRule, UUID> {

    @Override
    @Query(value = "select * from dynamic_rule where product_id = ?1", nativeQuery = true)
    void deleteById(UUID productId);

}
