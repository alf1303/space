package com.space.controller;

import com.space.model.Ship;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ShipController {

    private final Controller controller;

    public ShipController(Controller controller) {
        this.controller = controller;
    }

    @GetMapping("/rest/ships")
    List<Ship> getShips(@RequestParam Map<String, String> params) {
        System.out.println("params: " + params.entrySet());
        return controller.getShips(params);
    }

    @GetMapping("rest/ships/count")
    int getShipsCount(@RequestParam Map<String, String> params) {
        int result = controller.getShipsCount(params);
        return result;
    }

}
