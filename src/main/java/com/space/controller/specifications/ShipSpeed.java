package com.space.controller.specifications;

import com.space.model.Ship;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ShipSpeed implements Specification<Ship> {
    private double min;
    private double max;

    public ShipSpeed(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        //System.out.printf("Speed, min: %f, max: %f%n", min, max);
        Predicate greater = criteriaBuilder.greaterThanOrEqualTo(root.get("speed"), min);
        Predicate less = criteriaBuilder.lessThanOrEqualTo(root.get("speed"), max);
        Predicate finalPred = criteriaBuilder.and(greater, less);

        return finalPred;
    }
}
