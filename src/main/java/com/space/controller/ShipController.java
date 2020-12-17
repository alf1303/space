package com.space.controller;

import com.space.controller.helpers.GetRequestParams;
import com.space.controller.helpers.UpdateResponseBody;
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
    public List<Ship> getShips(GetRequestParams params) {
        logger.info(String.format("GET /ships parameters: %s", params.toString()));
        return controller.getShips(params);
    }

    @GetMapping("/ships/count")
    public int getShipsCount(GetRequestParams params) {
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
    public ResponseEntity<Ship> updateShip(@PathVariable String id, @RequestBody UpdateResponseBody json) {
        logger.info(String.format("POST Update. id: %s, params: %s", id, json));
        Ship ship = null;
            ship = controller.updateShip(id, json);
        return new ResponseEntity<>(ship, HttpStatus.OK);    }

    @PostMapping("ships")
    public ResponseEntity<Ship> createShip(@RequestBody UpdateResponseBody json) {
        logger.info(String.format("POST Create. params: %s", json));
        Ship ship = controller.createShip(json);
        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

}
