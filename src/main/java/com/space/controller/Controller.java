package com.space.controller;


import com.space.controller.exceptions.InvalidIdException;
import com.space.controller.exceptions.ShipNotFoundException;
import com.space.model.Ship;
import com.space.service.ShipService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class Controller {
    private final JsonParser jsonParser;
    private final ShipService shipService;

    public Controller(JsonParser jsonParser, ShipService shipService) {
        this.jsonParser = jsonParser;
        this.shipService = shipService;
    }

    List<Ship> getShips(Map<String, String> params) {
       List<Ship> ships = shipService.getAllShips();
       return ships;
    }

    int getShipsCount(Map<String, String> params) {
        List<Ship> ships = shipService.getAllShips();
        int count = ships.size();
        return count;
    }

    Ship getShipById(String id) {
        long res_id = -1;
        try {
            res_id = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new InvalidIdException();
        }
        if(res_id < 0) throw new InvalidIdException();
        Ship result = shipService.getShipById(res_id);
        if(result == null) {
            throw new ShipNotFoundException();
        }
        return result;
    }
}
