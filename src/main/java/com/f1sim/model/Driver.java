package com.f1sim.model;

public class Driver {
    private final String name;
    private final Team team;
    private final int skillRating; // 1 to 100
    private int points;
    private int wins;
    private int podiums;

    public Driver(String name, Team team, int skillRating) {
        this.name = name;
        this.team = team;
        this.skillRating = skillRating;
        this.points = 0;
        this.wins = 0;
        this.podiums = 0;
    }

    public String getName() { return name; }
    public Team getTeam() { return team; }
    public int getSkillRating() { return skillRating; }
    public int getPoints() { return points; }
    public int getWins() { return wins; }
    public int getPodiums() { return podiums; }

    public void addPoints(int pts) { this.points += pts; }
    public void incrementWins() { this.wins++; }
    public void incrementPodiums() { this.podiums++; }

    public void reset() {
        this.points = 0;
        this.wins = 0;
        this.podiums = 0;
    }
}