package com.project.hackathon.dealport.model;

import java.util.List;

/**
 * Created by natalyablanco on 17.06.17.
 */

public class Airports {
    private List<Airport> airports;

    public List<Airport> getAirports ()
    {
        return airports;
    }

    public void setAirports (List<Airport> airports)
    {
        this.airports = airports;
    }

    @Override
    public String toString()
    {
        return "Airports [airports = "+airports+"]";
    }
}