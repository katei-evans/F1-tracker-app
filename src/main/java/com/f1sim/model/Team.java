package com.f1sim.model;

public class Team {
    private final String name;
    private final int carPerformance; // 1 to 100
    private int points;

    public Team(String name, int carPerformance) {
        this.name = name;
        this.carPerformance = carPerformance;
        this.points = 0;
    }

    public String getName() { return name; }
    public int getCarPerformance() { return carPerformance; }
    public int getPoints() { return points; }
    public void addPoints(int pts) { this.points += pts; }
    public void reset() { this.points = 0; }
}