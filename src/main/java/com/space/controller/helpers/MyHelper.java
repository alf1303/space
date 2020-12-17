package com.space.controller.helpers;

import com.space.controller.exceptions.InvalidArgException;
import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.Calendar;

/** Contains methods, used for validating and converting values,
 *  received from RestController
 */
@Component
public class MyHelper {

    /**
     * Converts id value from string to int with validation
     * @param id {@code String}
     * @return {@code int} valid id number
     * @throws InvalidArgException if provided value is not positive integer number > 0
     */
    public long getValidId(String id) {
        long resulId = -1;
        try {
            resulId = Long.parseLong(id);
        } catch (NullPointerException | NumberFormatException e) {
            throw new InvalidArgException();
        }
        if(resulId <= 0) throw new InvalidArgException();
        return resulId;
    }

    /**
     * Validates name value
     * @param name {@code String}
     * @return {@code String} Input value
     * @throws InvalidArgException if provided input String is empty or length > 50
     */
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

    /**
     * Used for converting String with milliseconds, received from page when updating or creating
     * @param prodDate {@code String}
     * @return {@code Date} converted value or null
     * @throws InvalidArgException if provided date is out of needed range
     */
    public Date getValidDate(String prodDate) {
        Date result = null;
        try {
            result = new Date(Long.parseLong(prodDate));
            Date after = getAfterDate(null);
            Date before = getBeforeDate(null);
            if(!result.after(after) || !result.before(before)) {
                throw new InvalidArgException();
            }
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException ignored) {
        }
        return result;
    }

    /**
     * Used for converting String with speed, received from page
     * @param inSpeed {@code String} -> input value
     * @param create {@code boolean} -> true if used for updating or creating ship
     * @return {@code Double} converted value or null
     * @throws InvalidArgException if provided speed is out of needed range
     */
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

    /**
     * Used for converting String with crewSize, received from page
     * @param crewSize {@code String} -> input value
     * @param create {@code boolean} -> true if used for updating or creating ship
     * @return {@code Double} converted value or null
     * @throws InvalidArgException if provided crewSize is out of needed range
     */
    public Integer getValidCrewSize(String crewSize, boolean create) {
        try {
            Integer res = Integer.parseInt(crewSize);
            if(create && (res < 1 || res > 9999)) throw new InvalidArgException();
            else return res;
        } catch (NumberFormatException e) {
            //e.printStackTrace();
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

    /**
     * Converts input value to Date, or return value, representing lower bound of valid dates range
     * @param after {@code String} -> input value
     * @return {@code Date} converted to Date value or lower bound
     */
    public Date getAfterDate(String after) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2800, 0, 1, 0, 0, 0);
        return getDate(after, calendar, false);
    }

    /**
     * Converts input value to Date, or return value, representing upper bound of valid dates range
     * @param before {@code String} -> input value
     * @return {@code Date} converted to Date value or upper bound
     */
    public Date getBeforeDate(String before) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(3019, 11, 31, 23, 59, 59);
        return getDate(before, calendar, true);
    }

    /**
     * Converts input value to Date, or return value, representing lower bound of valid dates range if input incorrect
     * @param inDate {@code String} -> input value
     * @param calendar {@code Calendar} -> default value
     * @param isBeforeDate {@code boolean} -> if true, set result date to the end of year, else to the beginning
     * @return {@code Date} converted to Date value or lower/upper bound of valid date range
     */
    public Date getDate(String inDate, Calendar calendar, boolean isBeforeDate) {
        try {
            long tmp = Long.parseLong(inDate);
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
        BigDecimal bd = BigDecimal.valueOf((80 * ship.getSpeed() * k) / (3019 - year + 1));
        bd = bd.setScale(2, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }
}
