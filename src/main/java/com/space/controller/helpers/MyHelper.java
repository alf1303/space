package com.space.controller.helpers;

import com.space.controller.exceptions.InvalidArgException;
import com.space.model.Ship;
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
        if(res_id <= 0) throw new InvalidArgException();
        return res_id;
    }

    public String getValidName(String name) {
        if(name != null) {
            if(name.isEmpty() || name.length() > 50) {
                throw new InvalidArgException();
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
            Date after = getAfterDate(null);
            Date before = getBeforeDate(null);
            if(!result.after(after) || !result.before(before)) {
                throw new InvalidArgException();
            }
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException ignored) {
        }
        return result;
    }

    public Double getValidSpeed(String inSpeed, boolean create) {
        try {
            BigDecimal speed = new BigDecimal(inSpeed);
            double result =(speed.setScale(2, RoundingMode.HALF_EVEN)).doubleValue();
            if(create && (result < 0.01 || result > 0.99)) throw new InvalidArgException();
            else
                return result;
        } catch (NullPointerException | ArithmeticException | NumberFormatException e) {
            return null;
        }
    }

    public Integer getValidCrewSize(String crewSize, boolean create) {
        try {
            Integer res = Integer.parseInt(crewSize);
            if(create && (res < 1 || res > 9999)) throw new InvalidArgException();
            else return res;
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

    public double calculateRating(Ship ship) {
        double k = ship.isUsed() ? 0.5 : 1;
        Date prodDate = ship.getProdDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(prodDate);
        int year = calendar.get(Calendar.YEAR);
        //double result = (80 * ship.getSpeed() * k) / (3019 - year + 1);
        BigDecimal bd = BigDecimal.valueOf((80 * ship.getSpeed() * k) / (3019 - year + 1));
        bd = bd.setScale(2, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }
}
