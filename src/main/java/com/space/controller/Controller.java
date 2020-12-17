package com.space.controller;

import com.space.controller.exceptions.InvalidArgException;
import com.space.controller.exceptions.ShipNotFoundException;
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
    private final JsonParser jsonParser;
    private final ShipService shipService;
    private final MyHelper helper;

    public Controller(JsonParser jsonParser, ShipService shipService, MyHelper helper) {
        this.jsonParser = jsonParser;
        this.shipService = shipService;
        this.helper = helper;
    }

    List<Ship> getShips(Map<String, String> params) {
        Specification<Ship> spec = getShipSpecification(params);
        Pageable pageData = getPageable(params);
        return shipService.getAllShipsFilteredPaged(spec, pageData).getContent();
    }

    private Pageable getPageable(Map<String, String> params) {
        String order = params.get(ShipParams.ORDER.label);
        try { order = ShipOrder.valueOf(order).getFieldName();}
        catch (IllegalArgumentException | NullPointerException e) {order = "id";}
        int pageNumber = params.containsKey(ShipParams.PAGENUMBER.label) ? helper.getPageNumber(params.get(ShipParams.PAGENUMBER.label)) : 0;
        int pageSize = params.containsKey(ShipParams.PAGESIZE.label) ? helper.getPageSize(params.get(ShipParams.PAGESIZE.label)) : 3;
        return PageRequest.of(pageNumber, pageSize, Sort.by(order));
    }

    private Specification<Ship> getShipSpecification(Map<String, String> params) {
        String name = params.get(ShipParams.NAME.label);
        String planet = params.get(ShipParams.PLANET.label);
        String after = params.get(ShipParams.AFTER.label);
        String before = params.get(ShipParams.BEFORE.label);
        String minCrew = params.get(ShipParams.MINCREWSIZE.label);
        String maxCrew = params.get(ShipParams.MAXCREWSIZE.label);
        String minSpeed = params.get(ShipParams.MINSPEED.label);
        String maxSpeed = params.get(ShipParams.MAXSPEED.label);
        String minRating = params.get(ShipParams.MINRATING.label);
        String maxRating = params.get(ShipParams.MAXRATING.label);
        String shipType  = params.get(ShipParams.SHIPTYPE.label);
        String isUsed = params.get(ShipParams.ISUSED.label);

        ShipType shipTypeVal = helper.getShipType(shipType);
        double minSpeedVal = helper.getValidSpeed(minSpeed, false) == null ? 0.01 : helper.getValidSpeed(minSpeed, false);
        double maxSpeedVal = helper.getValidSpeed(maxSpeed, false) == null ? 0.99 : helper.getValidSpeed(maxSpeed, false);
        double minRatingValue = helper.getRating(minRating) == null ? 0 : helper.getRating(minRating);
        double maxRatingValue = helper.getRating(maxRating) == null ? Double.MAX_VALUE : helper.getRating(maxRating);
        int minCrewSize = helper.getValidCrewSize(minCrew, false) == null ? 1 : helper.getValidCrewSize(minCrew, false);
        int maxCrewSize = helper.getValidCrewSize(maxCrew, false) == null ? 9999 : helper.getValidCrewSize(maxCrew, false);

        return Specification.where(new ShipName(name))
                .and(new ShipPlanet(planet))
                .and(new ShipDate(helper.getAfterDate(after), helper.getBeforeDate(before))
                .and(new ShipCrewSize(minCrewSize, maxCrewSize))
                .and(new ShipSpeed(minSpeedVal, maxSpeedVal))
                .and(new ShipRating(minRatingValue, maxRatingValue))
                .and(new ShipShipType(shipTypeVal))
                .and(new ShipIsUsed(isUsed)));
    }

    int getShipsCount(Map<String, String> params) {
        Specification<Ship> spec = getShipSpecification(params);
        List<Ship> ships = shipService.getAllShipsFiltered(spec);
        return ships != null ? ships.size() : 0;
    }

    Ship getShipById(String id) {
        long res_id = helper.getValidId(id);
        Ship result;
        try {
            result = shipService.getShipById(res_id);
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

    public Ship updateShip(String id, UpdateResponseBody json) {
        Ship ship = getShipById(id);
        Ship updShip = getShipFromParams(json);
        if(updShip.getName() != null) ship.setName(updShip.getName());
        if(updShip.getPlanet() != null) ship.setPlanet(updShip.getPlanet());
        if(updShip.getShipType() != null) ship.setShipType(updShip.getShipType());
        if(updShip.getProdDate() != null) ship.setProdDate(updShip.getProdDate());
        Double speed = helper.getValidSpeed(json.getSpeed(), true);
        if(speed != null) ship.setSpeed(speed);
        Integer crewSize = helper.getValidCrewSize(json.getCrewSize(), true);
        if(crewSize != null) ship.setCrewSize(crewSize);
        if(json.getIsUsed() != null) {
            ship.setUsed(json.getIsUsed().equals("true"));
        }
        double rating = helper.calculateRating(ship);
        ship.setRating(rating);
        return shipService.updateShip(ship);
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
