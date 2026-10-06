package com.f1sim.model;

import java.util.List;

public class RaceResult {
    private final Track track;
    private final List<DriverRaceDetail> driverDetails;
    private final DriverRaceDetail fastestLapDriver;

    public RaceResult(Track track, List<DriverRaceDetail> driverDetails, DriverRaceDetail fastestLapDriver) {
        this.track = track;
        this.driverDetails = driverDetails;
        this.fastestLapDriver = fastestLapDriver;
    }

    public Track getTrack() { return track; }
    public List<DriverRaceDetail> getDriverDetails() { return driverDetails; }
    public DriverRaceDetail getFastestLapDriver() { return fastestLapDriver; }
}