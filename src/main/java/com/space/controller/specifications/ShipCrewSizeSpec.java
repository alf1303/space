package com.space.controller.specifications;

import com.space.model.Ship;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ShipCrewSizeSpec implements Specification<Ship> {
    private final int min;
    private final int max;

    public ShipCrewSizeSpec(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate greater = criteriaBuilder.greaterThanOrEqualTo(root.get("crewSize"), min);
        Predicate less = criteriaBuilder.lessThanOrEqualTo(root.get("crewSize"), max);
        Predicate finalPred = criteriaBuilder.and(greater, less);
        return finalPred;
    }
}
