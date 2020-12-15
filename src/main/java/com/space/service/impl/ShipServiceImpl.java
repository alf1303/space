package com.space.service.impl;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
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
