package com.f1sim.engine;

import com.f1sim.model.Driver;
import com.f1sim.model.DriverRaceDetail;
import com.f1sim.model.RaceResult;
import com.f1sim.model.Track;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class RaceEngine {
    private static final int[] FIA_POINTS = {25, 18, 15, 12, 10, 8, 6, 4, 2, 1};

    public RaceResult simulateRace(Track track, List<Driver> drivers) {
        Map<Driver, DriverRaceDetail> detailMap = new HashMap<>();
        for (Driver driver : drivers) {
            detailMap.put(driver, new DriverRaceDetail(driver));
        }

        // Lap by lap timing calculations
        for (int lap = 1; lap <= track.getLaps(); lap++) {
            for (Driver driver : drivers) {
                DriverRaceDetail detail = detailMap.get(driver);

                double driverFactor = (100.0 - driver.getSkillRating()) * 0.012;
                double carFactor = (100.0 - driver.getTeam().getCarPerformance()) * 0.018;
                double noise = ThreadLocalRandom.current().nextDouble(-0.35, 0.45);

                double lapTime = track.getBaseLapTimeSeconds() + driverFactor + carFactor + noise;
                detail.addLapTime(lapTime);

                // Pit stop simulation every 22 laps
                if (lap > 1 && lap % 22 == 0) {
                    double pitStopDuration = ThreadLocalRandom.current().nextDouble(2.1, 3.8) + 20.0;
                    detail.addPitStopPenalty(pitStopDuration);
                }
            }
        }

        // Sort by total race time
        List<DriverRaceDetail> sortedDetails = detailMap.values().stream()
                .sorted(Comparator.comparingDouble(DriverRaceDetail::getTotalTimeSeconds))
                .collect(Collectors.toList());

        // Find fastest lap driver
        DriverRaceDetail overallFastestLap = sortedDetails.stream()
                .min(Comparator.comparingDouble(DriverRaceDetail::getFastestLapSeconds))
                .orElse(sortedDetails.get(0));

        // Award FIA Championship points
        for (int i = 0; i < sortedDetails.size(); i++) {
            DriverRaceDetail detail = sortedDetails.get(i);
            detail.setFinishPosition(i + 1);

            int pts = (i < FIA_POINTS.length) ? FIA_POINTS[i] : 0;

            // +1 point for fastest lap if finished in top 10
            if (detail.getDriver().equals(overallFastestLap.getDriver()) && i < 10) {
                pts += 1;
            }

            detail.setPointsEarned(pts);
            detail.getDriver().addPoints(pts);
            detail.getDriver().getTeam().addPoints(pts);

            if (i == 0) {
                detail.getDriver().incrementWins();
            }
            if (i < 3) {
                detail.getDriver().incrementPodiums();
            }
        }

        return new RaceResult(track, sortedDetails, overallFastestLap);
    }
}