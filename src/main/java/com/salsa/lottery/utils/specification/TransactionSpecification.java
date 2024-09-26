package com.salsa.lottery.utils.specification;

import com.salsa.lottery.dto.request.lottery.LotteryRequest;
import com.salsa.lottery.dto.request.transaction.TransactionRequest;
import com.salsa.lottery.dto.request.transaction.TransactionSearchRequest;
import com.salsa.lottery.entity.Lottery;
import com.salsa.lottery.entity.Transaction;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification {
    public static Specification<Transaction> getSpecification(TransactionSearchRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getUser() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + request.getUser() + "%"));
            }
            if (request.getLottery() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + request.getLottery() + "%"));
            }

            jakarta.persistence.criteria.Predicate[] predicates1 = predicates.toArray(new jakarta.persistence.criteria.Predicate[predicates.size()]);
            return criteriaBuilder.and(predicates1);
        });
    }
}
