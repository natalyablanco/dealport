package com.project.hackathon.dealport.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by natalyablanco on 17.06.17.
 */

public class Airports {
    private List<Airport> airports;

    public List<Airport> getAirports() {
        return airports;
    }

    public List<String> getAirportsName() {
        List<String> names = new ArrayList<>();
        for(Airport airport : airports){
            names.add(airport.getName());
        }
        return names;
    }


    public void setAirports (List<Airport> airports) {
        this.airports = airports;
    }

    @Override
    public String toString() {
        return "Airports [airports = "+airports+"]";
    }
}