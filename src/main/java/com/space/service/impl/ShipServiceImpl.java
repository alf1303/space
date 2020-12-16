package com.space.service.impl;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShipServiceImpl implements ShipService {

    private ShipRepository shipRepository;

    @Autowired
    public void setShipRepository(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    public List<Ship> getAllShips() {
        return shipRepository.findAll();
    }

    public Page<Ship> getAllShipsPaged(Pageable pageData) {
        return shipRepository.findAll(pageData);
    }

    public Page<Ship> getAllShipsFilteredPaged(Specification<Ship> spec, Pageable pageData) {
        Page<Ship> result = null;
        try {
            result =  shipRepository.findAll(spec, pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Ship addShip(Ship ship) {
        return shipRepository.saveAndFlush(ship);
    }

    @Override
    public Ship updateShip(Ship ship) {
        return shipRepository.saveAndFlush(ship);
    }

    @Override
    public void deleteShipById(long id) {
        shipRepository.deleteById(id);
    }

    @Override
    public void deleteShip(Ship ship) {
        shipRepository.delete(ship);
    }

    @Override
    public Ship getShipById(long id) {
        Optional<Ship> sh = shipRepository.findById(id);
        return sh.get();
    }


}
