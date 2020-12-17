package com.space.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class RequestParamsModel {
    String name;
    String planet;
    String shipType;
    String after;
    String before;
    String isUsed;
    String minSpeed;
    String maxSpeed;
    String minCrewSize;
    String maxCrewSize;
    String minRating;
    String maxRating;
    String order;
    String pageNumber;
    String pageSize;

    public RequestParamsModel() {
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getMinCrewSize() {
        return minCrewSize;
    }

    public void setMinCrewSize(String minCrewSize) {
        this.minCrewSize = minCrewSize;
    }

    public String getMaxCrewSize() {
        return maxCrewSize;
    }

    public void setMaxCrewSize(String maxCrewSize) {
        this.maxCrewSize = maxCrewSize;
    }

    public String getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(String minSpeed) {
        this.minSpeed = minSpeed;
    }

    public String getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(String maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getMinRating() {
        return minRating;
    }

    public void setMinRating(String minRating) {
        this.minRating = minRating;
    }

    public String getMaxRating() {
        return maxRating;
    }

    public void setMaxRating(String maxRating) {
        this.maxRating = maxRating;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    @Override
    public String toString() {
        return String.format("name: %s, planet: %s, after: %s, before: %s, minCrew: %s, maxCrew: %s, " +
                "minSpeed: %s, maxSpeed: %s, minRating: %s, maxRating: %s, shipType: %s, isUsed: %s, " +
                        "order: %s, pageNumber: %s, pageSize: %s",
                name, planet, after, before, minCrewSize, maxCrewSize, minSpeed, maxSpeed,
                minRating, maxRating, shipType, isUsed, order, pageNumber, pageSize);
    }
}
