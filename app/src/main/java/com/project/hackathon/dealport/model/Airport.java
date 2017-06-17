package com.project.hackathon.dealport.model;

/**
 * Created by natalyablanco on 17.06.17.
 */

public class Airport {
    private String timezone;

    private String IATACode;

    private String name;

    private String ICAOCode;

    private String lng;

    private String lat;

    private String city;

    private String country;

    public String getTimezone ()
    {
        return timezone;
    }

    public void setTimezone (String timezone)
    {
        this.timezone = timezone;
    }

    public String getIATACode ()
    {
        return IATACode;
    }

    public void setIATACode (String IATACode)
    {
        this.IATACode = IATACode;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getICAOCode ()
    {
        return ICAOCode;
    }

    public void setICAOCode (String ICAOCode)
    {
        this.ICAOCode = ICAOCode;
    }

    public String getLng ()
    {
        return lng;
    }

    public void setLng (String lng)
    {
        this.lng = lng;
    }

    public String getLat ()
    {
        return lat;
    }

    public void setLat (String lat)
    {
        this.lat = lat;
    }

    public String getCity ()
    {
        return city;
    }

    public void setCity (String city)
    {
        this.city = city;
    }

    public String getCountry ()
    {
        return country;
    }

    public void setCountry (String country)
    {
        this.country = country;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [timezone = "+timezone+", IATACode = "+IATACode+", name = "+name+", ICAOCode = "+ICAOCode+", lng = "+lng+", lat = "+lat+", city = "+city+", country = "+country+"]";
    }
}