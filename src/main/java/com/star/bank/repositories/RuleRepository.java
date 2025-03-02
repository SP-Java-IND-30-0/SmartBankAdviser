package com.star.bank.repositories;

import com.star.bank.model.rule.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RuleRepository {
    private final EntityManager entityManager;
    private static final String PRODUCT_TYPE = "productType";
    private static final String COMPARE_TYPE = "compareType";
    private static final String OPERATION_TYPE = "operationType";
    private static final String AMOUNT = "amount";


    public <T extends RuleArguments> Optional<T> findRuleArguments(T arguments) {
        if (arguments instanceof RuleUserOf ruleUserOf) {
            return entityManager.createQuery("""
                            SELECT ru FROM RuleUserOf ru
                            WHERE ru.productType=:productType
                            """, RuleUserOf.class)
                    .setParameter(PRODUCT_TYPE, ruleUserOf.getProductType())
                    .getResultList()
                    .stream().findFirst().map(r -> (T) r);
        } else if (arguments instanceof RuleActiveUserOf ruleActiveUserOf) {
            return entityManager.createQuery("""
                            SELECT ra FROM RuleActiveUserOf ra
                            WHERE ra.productType=:productType
                            """, RuleActiveUserOf.class)
                    .setParameter(PRODUCT_TYPE, ruleActiveUserOf.getProductType())
                    .getResultList()
                    .stream().findFirst().map(r -> (T) r);
        } else if (arguments instanceof RuleCompareSum ruleCompareSum) {
            return entityManager.createQuery("""
                            SELECT rc FROM RuleCompareSum rc
                            WHERE rc.productType=:productType AND rc.operationType=:operationType AND rc.compareType=:compareType AND rc.amount=:amount
                            """, RuleCompareSum.class)
                    .setParameter(PRODUCT_TYPE, ruleCompareSum.getProductType())
                    .setParameter(OPERATION_TYPE, ruleCompareSum.getOperationType())
                    .setParameter(COMPARE_TYPE, ruleCompareSum.getCompareType())
                    .setParameter(AMOUNT, ruleCompareSum.getAmount())
                    .getResultList()
                    .stream().findFirst().map(r -> (T) r);
        } else if (arguments instanceof RuleCompareOperationSum ruleCompareOperationSum) {
            return entityManager.createQuery("""
                            SELECT rcs FROM RuleCompareOperationSum rcs
                            WHERE rcs.productType=:productType AND rcs.compareType=:compareType
                            """, RuleCompareOperationSum.class)
                    .setParameter(PRODUCT_TYPE, ruleCompareOperationSum.getProductType())
                    .setParameter(COMPARE_TYPE, ruleCompareOperationSum.getCompareType())
                    .getResultList()
                    .stream().findFirst().map(r -> (T) r);
        }
        return Optional.empty();
    }
}