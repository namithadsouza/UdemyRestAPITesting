package org.example.enums;

public enum GoogleAPIResources {
    AddPlaceAPI("/maps/api/place/add/json"),
    getPlaceAPI("/maps/api/place/get/json"),
    deletePlaceAPI("/maps/api/place/delete/json");
    private String resource;

    GoogleAPIResources(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }

}