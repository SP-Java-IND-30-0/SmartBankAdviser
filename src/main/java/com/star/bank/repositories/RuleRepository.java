package com.star.bank.repositories;

import com.star.bank.model.enums.BankProductType;
import com.star.bank.model.enums.CompareType;
import com.star.bank.model.enums.OperationType;
import com.star.bank.model.enums.QueryType;
import com.star.bank.model.rule.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RuleRepository {
    private final EntityManager entityManager;

    public <T> boolean existsRule(Class<T> ruleClass, BankProductType productType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<T> root = query.from(ruleClass);

        query.select(cb.count(root))
                .where(cb.equal(root.get("productType"), productType));

        return entityManager.createQuery(query).getSingleResult() > 0;
    }

    public boolean existsRuleCompareSum(BankProductType productType, OperationType operationType, CompareType compareType, int amount) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<RuleCompareSum> root = query.from(RuleCompareSum.class);

        query.select(cb.count(root))
                .where(cb.equal(root.get("productType"), productType),
                        cb.equal(root.get("operationType"), operationType),
                        cb.equal(root.get("compareType"), compareType),
                        cb.equal(root.get("amount"), amount));

        return entityManager.createQuery(query).getSingleResult() > 0;
    }

    public boolean existsRuleCompareOperationSum(BankProductType productType, CompareType compareType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<RuleCompareOperationSum> root = query.from(RuleCompareOperationSum.class);

        query.select(cb.count(root))
                .where(cb.equal(root.get("productType"), productType),
                        cb.equal(root.get("compareType"), compareType));

        return entityManager.createQuery(query).getSingleResult() > 0;
    }

    public boolean ruleExists(SimpleRule rule, BankProductType productType) {
        QueryType queryType = rule.getQueryType();
        RuleArguments arguments = rule.getArguments();

        if (queryType == QueryType.USER_OF || queryType == QueryType.ACTIVE_USER_OF) {
            return existsRule(SimpleRule.class, productType);
        }

        boolean argumentsExist = switch (queryType) {
            case TRANSACTION_SUM_COMPARE -> arguments instanceof RuleCompareSum compareSumArguments &&
                    existsRuleCompareSum(productType, compareSumArguments.getOperationType(), compareSumArguments.getCompareType(), compareSumArguments.getAmount());

            case TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW -> arguments instanceof RuleCompareOperationSum compareOperationSumArguments &&
                    existsRuleCompareOperationSum(productType, compareOperationSumArguments.getCompareType());

            default -> false;
        };

        return argumentsExist && entityManager.createQuery("""
            SELECT COUNT(sr) FROM SimpleRule sr
            WHERE sr.arguments = :arguments
            """, Long.class)
                .setParameter("arguments", arguments)
                .getSingleResult() > 0;
    }
}