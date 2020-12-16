package com.space.controller;

import com.space.controller.helpers.UpdateResponseBody;
import com.space.model.Ship;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.slf4j.impl.Log4jLoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("rest")
public class ShipController {

    private final Controller controller;

    public ShipController(Controller controller) {
        this.controller = controller;
    }

    static Logger logger = LogManager.getLogger(ShipController.class);

    @GetMapping("/ships")
    List<Ship> getShips(@RequestParam Map<String, String> params) {
        logger.info(String.format("GET /ships parameters: %s", params.entrySet()));
        List<Ship> sh = new ArrayList();
        sh = controller.getShips(params);
       // logger.info(String.format("List of sended ships, length=%d: ", sh.size()));
       // sh.forEach(el -> logger.info(String.format("  name: %s, planet: %s, prodYear: %s", el.getName(), el.getPlanet(), el.getProdDate())));
        return sh;
    }

    @GetMapping("/ships/count")
    int getShipsCount(@RequestParam Map<String, String> params) {
        int result = controller.getShipsCount(params);
        return result;
    }

    @GetMapping("/ships/{id}")
    Ship getShipById(@PathVariable String id) {
        Ship result = controller.getShipById(id);
        return result;
    }

    @DeleteMapping("/ships/{id}")
    void deleteShip(@PathVariable String id) {
        logger.info(String.format("Delete id: %s", id));
        controller.deleteShipById(id);
        //return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PostMapping("/ships/{id}")
    Ship updateShip(@PathVariable String id, @RequestBody UpdateResponseBody json) {
        logger.info(String.format("POST Update. id: %s, params: %s", id, json));
        Ship ship = controller.updateShip(id, json);
        return ship;
    }

}
