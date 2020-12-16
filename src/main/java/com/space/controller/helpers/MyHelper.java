package com.space.controller.helpers;

import com.space.controller.exceptions.InvalidArgException;
import com.space.model.ShipType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

@Component
    public class MyHelper {

    public long getValidId(String id) {
        long res_id = -1;
        try {
            res_id = Long.parseLong(id);
        } catch (NullPointerException | NumberFormatException e) {
            throw new InvalidArgException();
        }
        if(res_id < 0) throw new InvalidArgException();
        return res_id;
    }

    public String getValidName(String name) {
        if(name != null && !name.isEmpty()) {
            if(name.length() > 50) {
                return name.substring(0, 50);
            }
            else return name;
        }
        return null;
    }

    public ShipType getShipType(String shipType) {
        try {
            return ShipType.valueOf(shipType);
        } catch (NullPointerException | IllegalArgumentException e) {
            return null;
        }
    }

    public Date getValidDate(String prodDate) {
        Date result = null;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(prodDate));
            calendar.set(calendar.get(Calendar.YEAR), 0, 1, 0, 0, 0);
            result = new Date(calendar.getTimeInMillis());
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException ignored) {
        }
        return result;
    }

    public Double getValidSpeed(String inSpeed) {
        try {
            BigDecimal speed = new BigDecimal(inSpeed);
            return (speed.setScale(2, RoundingMode.HALF_EVEN)).doubleValue();
        } catch (NullPointerException | ArithmeticException | NumberFormatException e) {
            return null;
        }
    }

    public Integer getValidCrewSize(String crewSize) {
        try {
            return Integer.parseInt(crewSize);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Double getRating(String inRating) {
        try {
            BigDecimal rating = new BigDecimal(inRating);
            return (rating.setScale(2, RoundingMode.HALF_EVEN)).doubleValue();
        } catch (NullPointerException | ArithmeticException | NumberFormatException e) {
            return null;
        }
    }

    public Date getAfterDate(String after) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2800, 0, 1, 0, 0, 0);
        return getDate(after, calendar, false);
    }

    public Date getBeforeDate(String before) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(3019, 11, 31, 23, 59, 59);
        return getDate(before, calendar, true);
    }

    public Date getDate(String before, Calendar calendar, boolean isBeforeDate) {
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

    public int getPageNumber(String page) {
        int result = 0;
        try {
            result = Integer.parseInt(page);
        } catch (NumberFormatException ignored) {
        }
        return result;
    }

    public int getPageSize(String size) {
        int result = 3;
        try {
            result = Integer.parseInt(size);
        } catch (NumberFormatException ignored) {
        }
        return result;
    }




}
