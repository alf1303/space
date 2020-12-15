package com.space.controller;


import com.space.model.Ship;
import com.space.service.ShipService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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
}
