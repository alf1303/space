package com.space.controller;

import com.space.controller.exceptions.InvalidArgException;
import com.space.controller.exceptions.ShipNotFoundException;
import com.space.controller.helpers.GetRequestParams;
import com.space.controller.helpers.MyHelper;
import com.space.controller.helpers.UpdateResponseBody;
import com.space.controller.specifications.*;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class Controller {
    private final ShipService shipService;
    private final MyHelper helper;

    public Controller(ShipService shipService, MyHelper helper) {
        this.shipService = shipService;
        this.helper = helper;
    }

    List<Ship> getShips(GetRequestParams params) {
        Specification<Ship> spec = getShipSpecification(params);
        Pageable pageData = getPageable(params);
        return shipService.getAllShipsFilteredPaged(spec, pageData).getContent();
    }

    int getShipsCount(GetRequestParams params) {
        Specification<Ship> spec = getShipSpecification(params);
        List<Ship> ships = shipService.getAllShipsFiltered(spec);
        return ships != null ? ships.size() : 0;
    }

    private Pageable getPageable(GetRequestParams params) {
        String order;
        try {
            order = ShipOrder.valueOf(params.getOrder()).getFieldName();
        }
        catch (IllegalArgumentException | NullPointerException e) {
            order = "id";
        }
        int pageNumber = params.getPageNumber() != null ? helper.getPageNumber(params.getPageNumber()) : 0;
        int pageSize = params.getPageSize() != null ? helper.getPageSize(params.getPageSize()) : 3;
        return PageRequest.of(pageNumber, pageSize, Sort.by(order));
    }

    private Specification<Ship> getShipSpecification(GetRequestParams params) {
        ShipType shipTypeVal = helper.getShipType(params.getShipType());
        double minSpeedVal = helper.getValidSpeed(params.getMinSpeed(), false) == null ? 0.01 : helper.getValidSpeed(params.getMinSpeed(), false);
        double maxSpeedVal = helper.getValidSpeed(params.getMaxSpeed(), false) == null ? 0.99 : helper.getValidSpeed(params.getMaxSpeed(), false);
        double minRatingValue = helper.getRating(params.getMinRating()) == null ? 0 : helper.getRating(params.getMinRating());
        double maxRatingValue = helper.getRating(params.getMaxRating()) == null ? Double.MAX_VALUE : helper.getRating(params.getMaxRating());
        int minCrewSize = helper.getValidCrewSize(params.getMinCrewSize(), false) == null ? 1 : helper.getValidCrewSize(params.getMinCrewSize(), false);
        int maxCrewSize = helper.getValidCrewSize(params.getMaxCrewSize(), false) == null ? 9999 : helper.getValidCrewSize(params.getMaxCrewSize(), false);

        return Specification.where(
                new ShipName(params.getName()))
                .and(new ShipPlanet(params.getPlanet()))
                .and(new ShipDate(helper.getAfterDate(params.getAfter()), helper.getBeforeDate(params.getBefore()))
                .and(new ShipCrewSize(minCrewSize, maxCrewSize))
                .and(new ShipSpeed(minSpeedVal, maxSpeedVal))
                .and(new ShipRating(minRatingValue, maxRatingValue))
                .and(new ShipShipType(shipTypeVal))
                .and(new ShipIsUsed(params.getIsUsed())));
    }

    Ship getShipById(String id) {
        long resultId = helper.getValidId(id);
        Ship result;
        try {
            result = shipService.getShipById(resultId);
        } catch (IllegalArgumentException e) {
            throw new InvalidArgException();
        }
        catch (NoSuchElementException e1) {
            throw new ShipNotFoundException();
        }
        return result;
    }

    void deleteShipById(String id) {
        Ship ship = getShipById(id);
        shipService.deleteShip(ship);
    }

    public Ship updateShip(String id, UpdateResponseBody shipParams) {
        Ship ship = getShipById(id);
        System.out.println("updDate from DB: " + ship.getProdDate());
        Ship tempShip = getShipFromParams(shipParams);
        System.out.println("updDate from Request: " + tempShip.getProdDate());
        Double speed = helper.getValidSpeed(shipParams.getSpeed(), true);
        Integer crewSize = helper.getValidCrewSize(shipParams.getCrewSize(), true);
        double rating = helper.calculateRating(ship);

        if(tempShip.getName() != null) ship.setName(tempShip.getName());
        if(tempShip.getPlanet() != null) ship.setPlanet(tempShip.getPlanet());
        if(tempShip.getShipType() != null) ship.setShipType(tempShip.getShipType());
        if(tempShip.getProdDate() != null) ship.setProdDate(tempShip.getProdDate());
        if(speed != null) ship.setSpeed(speed);
        if(crewSize != null) ship.setCrewSize(crewSize);
        if(shipParams.getIsUsed() != null) {
            ship.setUsed(shipParams.getIsUsed().equals("true"));
        }
        ship.setRating(rating);
        Ship returnShip = shipService.updateShip(ship);
        System.out.println("updDate after saving: " + returnShip.getProdDate());
        List<Ship> tmp = shipService.getAllShips();
        tmp.forEach(e -> {
            if (e.getName().equals("Orion III")) {
                System.out.println(e.getName() + ": " + e.getProdDate());
            }
        });
        return returnShip;
    }

    Ship getShipFromParams(UpdateResponseBody json) {
        Ship ship = new Ship();
        ship.setName(helper.getValidName(json.getName()));
        ship.setPlanet(helper.getValidName(json.getPlanet()));
        ship.setShipType(helper.getShipType(json.getShipType()));
        ship.setProdDate(helper.getValidDate(json.getProdDate()));
        return ship;
    }

    public Ship createShip(UpdateResponseBody json) {
        Ship newShip = getShipFromParams(json);
        Double speed = helper.getValidSpeed(json.getSpeed(), true);
        Integer crewSize = helper.getValidCrewSize(json.getCrewSize(), true);
        if(newShip.getName() == null || newShip.getPlanet() == null || newShip.getProdDate() == null || newShip.getShipType() == null
        || speed == null || crewSize == null) {
            throw new InvalidArgException();
        }
        newShip.setSpeed(speed);
        newShip.setCrewSize(crewSize);
        if(json.getIsUsed() == null || json.getIsUsed().isEmpty()) newShip.setUsed(false);
        else newShip.setUsed(json.getIsUsed().equals("true"));
        double rating = helper.calculateRating(newShip);
        newShip.setRating(rating);
        return shipService.updateShip(newShip);
    }

}
