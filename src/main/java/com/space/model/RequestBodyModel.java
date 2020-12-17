package com.space.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class RequestBodyModel {
    String name;
    String planet;
    String shipType;
    String prodDate;
    String isUsed;
    String speed;
    String crewSize;

    public RequestBodyModel() {
    }

    public boolean isEmpty() {
        return ((name == null && planet == null && shipType == null && prodDate == null && isUsed == null && speed == null && crewSize == null));
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

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public String getProdDate() {
        return prodDate;
    }

    public void setProdDate(String prodDate) {
        this.prodDate = prodDate;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(String crewSize) {
        this.crewSize = crewSize;
    }

    @Override
    public String toString() {
        return String.format("name: %s, planet: %s, shipType: %s, prodDate: %s, isUsed: %s, speed: %s, crewSize: %s",
                name, planet, shipType, prodDate, isUsed, speed, crewSize);
    }
}
