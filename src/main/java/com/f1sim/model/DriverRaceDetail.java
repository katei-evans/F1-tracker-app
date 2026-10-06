package com.f1sim.model;

public class DriverRaceDetail {
    private final Driver driver;
    private double totalTimeSeconds;
    private double fastestLapSeconds;
    private int pitStops;
    private int pointsEarned;
    private int finishPosition;

    public DriverRaceDetail(Driver driver) {
        this.driver = driver;
        this.totalTimeSeconds = 0.0;
        this.fastestLapSeconds = Double.MAX_VALUE;
        this.pitStops = 0;
        this.pointsEarned = 0;
    }

    public Driver getDriver() { return driver; }
    public double getTotalTimeSeconds() { return totalTimeSeconds; }
    public double getFastestLapSeconds() { return fastestLapSeconds; }
    public int getPitStops() { return pitStops; }
    public int getPointsEarned() { return pointsEarned; }
    public int getFinishPosition() { return finishPosition; }

    public void addLapTime(double lapTime) {
        this.totalTimeSeconds += lapTime;
        if (lapTime < this.fastestLapSeconds) {
            this.fastestLapSeconds = lapTime;
        }
    }

    public void addPitStopPenalty(double penaltySeconds) {
        this.totalTimeSeconds += penaltySeconds;
        this.pitStops++;
    }

    public void setFinishPosition(int finishPosition) { this.finishPosition = finishPosition; }
    public void setPointsEarned(int pointsEarned) { this.pointsEarned = pointsEarned; }
}