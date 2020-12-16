package com.space.controller;


import com.space.controller.exceptions.InvalidIdException;
import com.space.controller.exceptions.ShipNotFoundException;
import com.space.controller.specifications.*;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
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

       String name = params.get(ShipParams.NAME.label);
       String planet = params.get(ShipParams.PLANET.label);
       String order = params.get(ShipParams.ORDER.label);
       String after = params.get(ShipParams.AFTER.label);
       String before = params.get(ShipParams.BEFORE.label);
       String minCrew = params.get(ShipParams.MINCREWSIZE.label);
       String maxCrew = params.get(ShipParams.MAXCREWSIZE.label);
       String minSpeed = params.get(ShipParams.MINSPEED.label);
       String maxSpeed = params.get(ShipParams.MAXSPEED.label);
       String minRating = params.get(ShipParams.MINRATING.label);
       String maxRating = params.get(ShipParams.MAXRATING.label);
       String shipType  = params.get(ShipParams.SHIPTYPE.label);
       ShipType shipTypeVal = getShipType(shipType);
       String isUsed = params.get(ShipParams.ISUSED.label);

       int minCrewSize = getCrewSize(minCrew, false);
       int maxCrewSize = getCrewSize(maxCrew, true);
       double minSpeedVal = getSpeed(minSpeed, false);
       double maxSpeedVal = getSpeed(maxSpeed, true);
       double minRatingValue = getRating(minRating, false);
       double maxRatingValue = getRating(maxRating, true);

       try { order = ShipOrder.valueOf(order).getFieldName();}
       catch (IllegalArgumentException | NullPointerException e) {order = "id";}

       Specification<Ship> spec = Specification.where(new ShipName(name))
               .and(new ShipPlanet(planet))
               .and(new ShipDate(getAfterDate(after), getBeforeDate(before))
               .and(new ShipCrewSize(minCrewSize, maxCrewSize))
               .and(new ShipSpeed(minSpeedVal, maxSpeedVal))
               .and(new ShipRating(minRatingValue, maxRatingValue))
               .and(new ShipShipType(shipTypeVal))
               .and(new ShipIsUsed(isUsed)));

       int pageNumber = params.containsKey(ShipParams.PAGENUMBER.label) ? getPageNumber(params.get(ShipParams.PAGENUMBER.label)) : 0;
       int pageSize = params.containsKey(ShipParams.PAGESIZE.label) ? getPageSize(params.get(ShipParams.PAGESIZE.label)) : 3;
       Pageable pageData = PageRequest.of(pageNumber, pageSize, Sort.by(order));
       return shipService.getAllShipsFilteredPaged(spec, pageData).getContent();
    }

    private ShipType getShipType(String shipType) {
        try {
            return ShipType.valueOf(shipType);
        } catch (NullPointerException | IllegalArgumentException e) {
            return null;
        }
    }

    private double getRating(String inRating, boolean b) {
        try {
            BigDecimal rating = new BigDecimal(inRating);
            return (rating.setScale(2, RoundingMode.HALF_EVEN)).doubleValue();
        } catch (NullPointerException | ArithmeticException | NumberFormatException e) {
            return b ? Double.MAX_VALUE : 0.00;
        }
    }

    private double getSpeed(String inSpeed, boolean b) {
        try {
            BigDecimal speed = new BigDecimal(inSpeed);
            return (speed.setScale(2, RoundingMode.HALF_EVEN)).doubleValue();
        } catch (NullPointerException | ArithmeticException | NumberFormatException e) {
            return b ? 0.99 : 0.01;
        }
    }

    private int getCrewSize(String crew, boolean b) {
            try {
                return Integer.parseInt(crew);
            } catch (NullPointerException | NumberFormatException e) {
                if (b) {
                    return 9999;
                }
                else return 1;
            }
    }

    private Date getAfterDate(String after) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2800, 0, 1, 0, 0, 0);
        return getDate(after, calendar, false);
    }

    private Date getBeforeDate(String before) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(3019, 11, 31, 23, 59, 59);
        return getDate(before, calendar, true);
    }

    private Date getDate(String before, Calendar calendar, boolean isBeforeDate) {
        try {
            long tmp = Long.parseLong(before);
            calendar.setTimeInMillis(tmp);
            if (!isBeforeDate) {
                calendar.set(calendar.get(Calendar.YEAR), 0, 1, 0, 0, 0);
            } else {
                calendar.set(calendar.get(Calendar.YEAR), 11, 31, 23, 59, 59);
            }
        } catch (NullPointerException | NumberFormatException ignored) {
        }
        return new Date(calendar.getTimeInMillis());
    }

    private int getPageNumber(String page) {
        int result = 0;
        try {
            System.out.println(page);
            result = Integer.parseInt(page);
        } catch (NumberFormatException e) {
        }
        return result;
    }

    private int getPageSize(String size) {
        int result = 3;
        try {
            result = Integer.parseInt(size);
        } catch (NumberFormatException e) {
        }
        return result;
    }

    int getShipsCount(Map<String, String> params) {
        List<Ship> ships = shipService.getAllShips();
        return ships != null ? ships.size() : 0;
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
