package com.space.controller;

import com.space.controller.exceptions.InvalidArgException;
import com.space.controller.exceptions.ShipNotFoundException;
import com.space.model.RequestParamsModel;
import com.space.controller.helpers.MyHelper;
import com.space.model.RequestBodyModel;
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

    List<Ship> getShips(RequestParamsModel params) {
        Specification<Ship> spec = getShipSpecification(params);
        Pageable pageData = getPageable(params);
        return shipService.getAllShipsFilteredPaged(spec, pageData).getContent();
    }

    int getShipsCount(RequestParamsModel params) {
        Specification<Ship> spec = getShipSpecification(params);
        List<Ship> ships = shipService.getAllShipsFiltered(spec);
        return ships != null ? ships.size() : 0;
    }

    private Pageable getPageable(RequestParamsModel params) {
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

    private Specification<Ship> getShipSpecification(RequestParamsModel params) {
        ShipType shipTypeVal = helper.getShipType(params.getShipType());
        double minSpeedVal = helper.getValidSpeed(params.getMinSpeed(), false) == null ? 0.01 : helper.getValidSpeed(params.getMinSpeed(), false);
        double maxSpeedVal = helper.getValidSpeed(params.getMaxSpeed(), false) == null ? 0.99 : helper.getValidSpeed(params.getMaxSpeed(), false);
        double minRatingValue = helper.getRating(params.getMinRating()) == null ? 0 : helper.getRating(params.getMinRating());
        double maxRatingValue = helper.getRating(params.getMaxRating()) == null ? Double.MAX_VALUE : helper.getRating(params.getMaxRating());
        int minCrewSize = helper.getValidCrewSize(params.getMinCrewSize(), false) == null ? 1 : helper.getValidCrewSize(params.getMinCrewSize(), false);
        int maxCrewSize = helper.getValidCrewSize(params.getMaxCrewSize(), false) == null ? 9999 : helper.getValidCrewSize(params.getMaxCrewSize(), false);

        return Specification.where(
                new ShipNameSpec(params.getName()))
                .and(new ShipPlanetSpec(params.getPlanet()))
                .and(new ShipDateSpec(helper.getAfterDate(params.getAfter()), helper.getBeforeDate(params.getBefore()))
                .and(new ShipCrewSizeSpec(minCrewSize, maxCrewSize))
                .and(new ShipSpeedSpec(minSpeedVal, maxSpeedVal))
                .and(new ShipRatingSpec(minRatingValue, maxRatingValue))
                .and(new ShipShipTypeSpec(shipTypeVal))
                .and(new ShipIsUsedSpec(params.getIsUsed())));
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

    public Ship updateShip(String id, RequestBodyModel shipParams) {
        Ship ship = getShipById(id);
        Ship tempShip = getShipFromParams(shipParams);
        Double speed = helper.getValidSpeed(shipParams.getSpeed(), true);
        Integer crewSize = helper.getValidCrewSize(shipParams.getCrewSize(), true);

        if(tempShip.getName() != null) ship.setName(tempShip.getName());
        if(tempShip.getPlanet() != null) ship.setPlanet(tempShip.getPlanet());
        if(tempShip.getShipType() != null) ship.setShipType(tempShip.getShipType());
        if(tempShip.getProdDate() != null) ship.setProdDate(tempShip.getProdDate());
        if(speed != null) ship.setSpeed(speed);
        if(crewSize != null) ship.setCrewSize(crewSize);
        if(shipParams.getIsUsed() != null) {
            ship.setUsed(shipParams.getIsUsed().equals("true"));
        }
        double rating = helper.calculateRating(ship);
        ship.setRating(rating);
        return shipService.updateShip(ship);
    }

    Ship getShipFromParams(RequestBodyModel json) {
        Ship ship = new Ship();
        ship.setName(helper.getValidName(json.getName()));
        ship.setPlanet(helper.getValidName(json.getPlanet()));
        ship.setShipType(helper.getShipType(json.getShipType()));
        ship.setProdDate(helper.getValidDate(json.getProdDate()));
        return ship;
    }

    public Ship createShip(RequestBodyModel json) {
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
