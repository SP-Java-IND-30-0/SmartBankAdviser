package com.star.bank.repositories;

import com.star.bank.model.product.DynamicRule;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DynamicRuleRepository extends JpaRepository<DynamicRule, UUID> {

    @Override
    void deleteById(@NonNull UUID productId);

}
