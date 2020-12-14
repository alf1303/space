package com.space.service;

import com.space.model.Ship;

import java.util.List;

public interface ShipService {
    List<Ship> getAllShips();
    Ship addShip(Ship ship);
    Ship updateShip(Ship ship);
    void deleteShipById(long id);
    void deleteShip(Ship ship);
    Ship getShipById(long id);


}
