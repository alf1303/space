package com.space.controller;

public enum ShipParams {
    NAME("name"),
    PLANET("planet"),
    SHIPTYPE("shipType"),
    AFTER("after"),
    BEFORE("before"),
    ISUSED("isUsed"),
    MINSPEED("minSpeed"),
    MAXSPEED("maxSpeed"),
    MINCREWSIZE("minCrewSize"),
    MAXCREWSIZE("maxCrewSize"),
    MINRATING("minRating"),
    MAXRATING("maxRating"),
    ORDER("order"),
    PAGENUMBER("pageNumber"),
    PAGESIZE("pageSize");

    public final String label;
    private ShipParams(String label) {
        this.label = label;
    }
}
