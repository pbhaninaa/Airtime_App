package com.example.testingmyskills.JavaClasses;

public class Country {
    private String countryCode;
    private String countryName;
    private String countryFlag;

    public Country(String countryCode, String countryName, String countryFlag) {
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.countryFlag = countryFlag;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCountryFlag() {
        return countryFlag;
    }

    @Override
    public String toString() {
        return countryCode;
    }
}
