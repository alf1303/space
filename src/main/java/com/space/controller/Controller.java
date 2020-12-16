package com.space.controller;


import com.space.controller.exceptions.InvalidIdException;
import com.space.controller.exceptions.ShipNotFoundException;
import com.space.controller.specifications.ShipDate;
import com.space.controller.specifications.ShipName;
import com.space.controller.specifications.ShipPlanet;
import com.space.model.Ship;
import com.space.service.ShipService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
       try { order = ShipOrder.valueOf(order).getFieldName();}
       catch (IllegalArgumentException | NullPointerException e) {order = "id";}

       Specification<Ship> spec = Specification.where(new ShipName(name)).and(new ShipPlanet(planet)).and(new ShipDate(getAfterDate(after), getBeforeDate(before)));

       int pageNumber = params.containsKey(ShipParams.PAGENUMBER.label) ? getPageNumber(params.get(ShipParams.PAGENUMBER.label)) : 0;
       int pageSize = params.containsKey(ShipParams.PAGESIZE.label) ? getPageSize(params.get(ShipParams.PAGESIZE.label)) : 3;
       Pageable pageData = PageRequest.of(pageNumber, pageSize, Sort.by(order));
       return shipService.getAllShipsFilteredPaged(spec, pageData).getContent();
    }

    private Date getAfterDate(String after) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");
        String afterStr = "2800.01.01 00:00:00.000";
        Date afterDate = null;
        try {
            afterDate = sdf.parse(afterStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long afterMillis = afterDate.getTime();
        try {
            afterMillis = Long.parseLong(after);
            afterDate = new Date(afterMillis);
        } catch (NullPointerException | NumberFormatException ignored) {
        }
        //System.out.println("after: " + result);
        return afterDate;
    }

    private Date getBeforeDate(String before) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");
        String beforeStr = "3019.12.12 23:59:59.999";
        Date beforeDate = null;
        try {
            beforeDate = sdf.parse(beforeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long beforeMillis = beforeDate.getTime();
        try {
            beforeMillis = Long.parseLong(before);
            beforeDate = new Date(beforeMillis);
        } catch (NullPointerException | NumberFormatException ignored) {
        }
        //System.out.println("before: " + result);
        return beforeDate;
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
