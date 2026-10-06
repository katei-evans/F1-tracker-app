package com.f1sim.model;

public class Track {
    private final String name;
    private final String country;
    private final int laps;
    private final double baseLapTimeSeconds;

    public Track(String name, String country, int laps, double baseLapTimeSeconds) {
        this.name = name;
        this.country = country;
        this.laps = laps;
        this.baseLapTimeSeconds = baseLapTimeSeconds;
    }

    public String getName() { return name; }
    public String getCountry() { return country; }
    public int getLaps() { return laps; }
    public double getBaseLapTimeSeconds() { return baseLapTimeSeconds; }
}