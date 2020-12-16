package com.space.service;

import com.space.model.Ship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ShipService {
    List<Ship> getAllShips();
    Ship addShip(Ship ship);
    Ship updateShip(Ship ship);
    void deleteShipById(long id);
    void deleteShip(Ship ship);
    Ship getShipById(long id);
    List<Ship> getAllShipsFiltered(Specification<Ship> spec);
    Page<Ship> getAllShipsFilteredPaged(Specification<Ship> spec, Pageable pageData);


}
