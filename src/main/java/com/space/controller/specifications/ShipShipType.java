package com.space.controller.specifications;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ShipShipType implements Specification<Ship> {
    ShipType shipType;

    public ShipShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    @Override
    public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if(shipType == null) {
            return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        }
        else {
            return criteriaBuilder.equal(root.get("shipType"), shipType);
        }
    }
}
