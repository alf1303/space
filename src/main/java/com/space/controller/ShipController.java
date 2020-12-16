package com.space.controller;

import com.space.controller.exceptions.InvalidIdException;
import com.space.controller.exceptions.ShipNotFoundException;
import com.space.model.Ship;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("rest")
public class ShipController {

    private final Controller controller;

    public ShipController(Controller controller) {
        this.controller = controller;
    }

    @GetMapping("/ships")
    List<Ship> getShips(@RequestParam Map<String, String> params) {
        System.out.println("params: " + params.entrySet());
        List<Ship> sh = controller.getShips(params);
        sh.forEach(el -> {
            System.out.println("name: " + el.getName() + " year: " + el.getProdDate());
        });
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

}
