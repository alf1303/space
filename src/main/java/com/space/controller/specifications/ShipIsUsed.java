package com.space.controller.specifications;

import com.space.model.Ship;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ShipIsUsed implements Specification<Ship> {
    String isUsed;

    public ShipIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    @Override
    public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        System.out.println("isUsed: " + isUsed);
        Predicate def = criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        try {
            if(isUsed.equals("true"))
                def = criteriaBuilder.isTrue(root.get("isUsed"));
            else
                def = criteriaBuilder.isFalse(root.get("isUsed"));
        } catch (NullPointerException ignored) {
        }
        return def;
    }
}
