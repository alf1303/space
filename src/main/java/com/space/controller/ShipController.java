package com.space.controller;

import com.space.model.RequestParamsModel;
import com.space.model.RequestBodyModel;
import com.space.model.Ship;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("rest")
public class ShipController {

    private final Controller controller;

    public ShipController(Controller controller) {
        this.controller = controller;
    }

    static Logger logger = LogManager.getLogger(ShipController.class);

    @GetMapping("/ships")
    public List<Ship> getShips(RequestParamsModel params) {
        logger.info(String.format("GET /ships parameters: %s", params.toString()));
        List<Ship> result = controller.getShips(params);
        logger.info("result ship list: ");
        result.forEach(e -> logger.info(String.format("name: %s, date: %s", e.getName(), e.getProdDate())));
        return result;
    }

    @GetMapping("/ships/count")
    public int getShipsCount(RequestParamsModel params) {
        return controller.getShipsCount(params);
    }

    @GetMapping("/ships/{id}")
    public Ship getShipById(@PathVariable String id) {
        return controller.getShipById(id);
    }

    @DeleteMapping("/ships/{id}")
    public void deleteShip(@PathVariable String id) {
        logger.info(String.format("Delete id: %s", id));
        controller.deleteShipById(id);
    }

    @PostMapping("/ships/{id}")
    public ResponseEntity<Ship> updateShip(@PathVariable String id, @RequestBody RequestBodyModel json) {
        logger.info(String.format("POST Update. id: %s, params: %s", id, json));
        Ship ship = controller.updateShip(id, json);
        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    @PostMapping("ships")
    public ResponseEntity<Ship> createShip(@RequestBody RequestBodyModel json) {
        logger.info(String.format("POST Create. params: %s", json));
        Ship ship = controller.createShip(json);
        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

}
