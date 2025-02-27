package com.star.bank.repositories;

import com.star.bank.model.rule.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RuleRepository {
    private final EntityManager entityManager;

    private Predicate buildArgumentPredicate(CriteriaBuilder cb, Root<SimpleRule> root, RuleArguments ruleArguments) {
        Predicate predicate = cb.conjunction();

        if (ruleArguments instanceof RuleUserOf ruleUserOf) {
            Join<SimpleRule, RuleUserOf> ruleUserOfJoin = root.join("arguments", JoinType.INNER);
            predicate = cb.and(predicate,
                    cb.equal(ruleUserOfJoin.get("productType"), ruleUserOf.getProductType()));
        } else if (ruleArguments instanceof RuleActiveUserOf ruleActiveUserOf) {
            Join<SimpleRule, RuleActiveUserOf> ruleActiveUserOfJoin = root.join("arguments", JoinType.INNER);
            predicate = cb.and(predicate,
                    cb.equal(ruleActiveUserOfJoin.get("productType"), ruleActiveUserOf.getProductType()));
        } else if (ruleArguments instanceof RuleCompareSum ruleCompareSum) {
            Join<SimpleRule, RuleCompareSum> ruleCompareSumJoin = root.join("arguments", JoinType.INNER);
            predicate = cb.and(predicate,
                    cb.equal(ruleCompareSumJoin.get("productType"), ruleCompareSum.getProductType()),
                    cb.equal(ruleCompareSumJoin.get("operationType"), ruleCompareSum.getOperationType()),
                    cb.equal(ruleCompareSumJoin.get("compareType"), ruleCompareSum.getCompareType()),
                    cb.equal(ruleCompareSumJoin.get("amount"), ruleCompareSum.getAmount()));
        } else if (ruleArguments instanceof RuleCompareOperationSum ruleCompareOperationSum) {
            Join<SimpleRule, RuleCompareOperationSum> ruleCompareOperationSumJoin = root.join("arguments", JoinType.INNER);
            predicate = cb.and(predicate,
                    cb.equal(ruleCompareOperationSumJoin.get("productType"), ruleCompareOperationSum.getProductType()),
                    cb.equal(ruleCompareOperationSumJoin.get("compareType"), ruleCompareOperationSum.getCompareType()));
        } else {
            throw new IllegalArgumentException("Неизвестный тип правила: " + ruleArguments.getClass().getSimpleName());
        }

        return predicate;
    }

    public Optional<SimpleRule> findRuleById(int id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SimpleRule> query = cb.createQuery(SimpleRule.class);
        Root<SimpleRule> root = query.from(SimpleRule.class);

        Predicate predicate = cb.equal(root.get("id"), id);

        query.select(root).where(predicate);

        return entityManager.createQuery(query).getResultStream().findFirst();
    }

    public boolean existsRuleByType(int id, RuleArguments ruleArguments) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<SimpleRule> root = query.from(SimpleRule.class);

        Predicate predicate = cb.equal(root.get("id"), id);

        predicate = cb.and(predicate, buildArgumentPredicate(cb, root, ruleArguments));

        query.select(cb.count(root)).where(predicate);

        return entityManager.createQuery(query).getSingleResult() > 0;
    }
}
