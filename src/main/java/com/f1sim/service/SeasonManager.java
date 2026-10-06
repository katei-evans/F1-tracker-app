package com.f1sim.service;

import com.f1sim.engine.RaceEngine;
import com.f1sim.model.Driver;
import com.f1sim.model.RaceResult;
import com.f1sim.model.Team;
import com.f1sim.model.Track;

import java.util.*;
import java.util.stream.Collectors;

public class SeasonManager {
    private final List<Team> teams = new ArrayList<>();
    private final List<Driver> drivers = new ArrayList<>();
    private final List<Track> tracks = new ArrayList<>();
    private final List<RaceResult> raceHistory = new ArrayList<>();
    private final RaceEngine engine = new RaceEngine();
    private int currentTrackIndex = 0;

    public SeasonManager() {
        seedInitialData();
    }

    private void seedInitialData() {
        Team redBull = new Team("Red Bull Racing", 89);
        Team ferrari = new Team("Ferrari", 91);
        Team mercedes = new Team("Mercedes", 94);
        Team mclaren = new Team("McLaren", 90);
        Team aston = new Team("Aston Martin", 84);

        teams.addAll(List.of(redBull, ferrari, mercedes, mclaren, aston));

        drivers.add(new Driver("Max Verstappen", redBull, 90));
        drivers.add(new Driver("Isack hadjar", redBull, 86));
        drivers.add(new Driver("Charles Leclerc", ferrari, 92));
        drivers.add(new Driver("Lewis Hamilton", ferrari, 89));
        drivers.add(new Driver("Kimi Antonelli", mercedes, 95));
        drivers.add(new Driver("George Russell", mercedes, 90));
        drivers.add(new Driver("Lando Norris", mclaren, 94));
        drivers.add(new Driver("Oscar Piastri", mclaren, 89));
        drivers.add(new Driver("Fernando Alonso", aston, 93));
        drivers.add(new Driver("Lance Stroll", aston, 80));

        tracks.add(new Track("Bahrain GP", "Bahrain", 57, 91.5));
        tracks.add(new Track("Monaco GP", "Monaco", 78, 74.2));
        tracks.add(new Track("Silverstone GP", "United Kingdom", 52, 87.0));
        tracks.add(new Track("Spa-Francorchamps", "Belgium", 44, 105.4));
        tracks.add(new Track("Monza GP", "Italy", 53, 81.0));
        tracks.add(new Track("Suzuka GP", "Japan", 53, 89.2));
    }

    public Optional<RaceResult> simulateNextRace() {
        if (currentTrackIndex >= tracks.size()) {
            return Optional.empty();
        }
        Track track = tracks.get(currentTrackIndex++);
        RaceResult result = engine.simulateRace(track, drivers);
        raceHistory.add(result);
        return Optional.of(result);
    }

    public void resetSeason() {
        currentTrackIndex = 0;
        raceHistory.clear();
        drivers.forEach(Driver::reset);
        teams.forEach(Team::reset);
    }

    public List<Driver> getDriverStandings() {
        return drivers.stream()
                .sorted(Comparator.comparingInt(Driver::getPoints)
                        .thenComparingInt(Driver::getWins)
                        .thenComparingInt(Driver::getPodiums)
                        .reversed())
                .collect(Collectors.toList());
    }

    public List<Team> getConstructorStandings() {
        return teams.stream()
                .sorted(Comparator.comparingInt(Team::getPoints).reversed())
                .collect(Collectors.toList());
    }

    public boolean isSeasonFinished() {
        return currentTrackIndex >= tracks.size();
    }

    public Track getNextTrack() {
        return isSeasonFinished() ? null : tracks.get(currentTrackIndex);
    }

    public int getCurrentRound() { return currentTrackIndex + 1; }
    public int getTotalRounds() { return tracks.size(); }
}