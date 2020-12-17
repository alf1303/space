package com.space.controller.specifications;

import com.space.model.Ship;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Date;

public class ShipDateSpec implements Specification<Ship> {
    private final Date after;
    private final Date before;

    public ShipDateSpec(Date after, Date before) {
        this.after = after;
        this.before = before;
    }

    @Override
    public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        Predicate afterPred = criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), after);
        Predicate beforePred = criteriaBuilder.lessThanOrEqualTo(root.get("prodDate"), before);
        Predicate finalPred = criteriaBuilder.and(afterPred, beforePred);

        return finalPred;

    }
}

