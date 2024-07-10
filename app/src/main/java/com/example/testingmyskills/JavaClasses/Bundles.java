package com.example.testingmyskills.JavaClasses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Bundles {
    @JsonProperty("Category")
    private String category;

    @JsonProperty("MapName")
    private String mapName;

    @JsonProperty("PricePlanCode")
    private String pricePlanCode;

    @JsonProperty("Bundle")
    private String bundle;

    @JsonProperty("Validity")
    private String validity;

    @JsonProperty("VolumeMB")
    private Object volumeMB; // Object to handle both double and "Null"

    @JsonProperty("CurrentUSDCharge")
    private double currentUSDCharge;

    // Getters and setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getPricePlanCode() {
        return pricePlanCode;
    }

    public void setPricePlanCode(String pricePlanCode) {
        this.pricePlanCode = pricePlanCode;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public Object getVolumeMB() {
        return volumeMB;
    }

    public void setVolumeMB(Object volumeMB) {
        this.volumeMB = volumeMB;
    }

    public double getCurrentUSDCharge() {
        return currentUSDCharge;
    }

    public void setCurrentUSDCharge(double currentUSDCharge) {
        this.currentUSDCharge = currentUSDCharge;
    }
}
