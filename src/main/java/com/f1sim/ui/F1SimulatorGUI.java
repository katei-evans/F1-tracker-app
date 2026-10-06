package com.f1sim.ui;

import com.f1sim.model.Driver;
import com.f1sim.model.DriverRaceDetail;
import com.f1sim.model.RaceResult;
import com.f1sim.model.Team;
import com.f1sim.model.Track;
import com.f1sim.service.SeasonManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class F1SimulatorGUI extends JFrame {
    private final SeasonManager seasonManager = new SeasonManager();

    private DefaultTableModel driverTableModel;
    private DefaultTableModel constructorTableModel;
    private JTextArea logArea;
    private JLabel statusLabel;
    private JButton btnSimulateNext;
    private JButton btnSimulateSeason;
    private JButton btnReset;

    public F1SimulatorGUI() {
        setTitle("F1 Season Simulator & Championship Manager");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        refreshTables();
        updateStatus();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        controlPanel.setBackground(new Color(240, 240, 240));

        btnSimulateNext = new JButton("Simulate Next Race");
        btnSimulateSeason = new JButton("Simulate Full Season");
        btnReset = new JButton("Reset Season");
        statusLabel = new JLabel("Status: Ready");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        btnSimulateNext.addActionListener(e -> simulateNextRace());
        btnSimulateSeason.addActionListener(e -> simulateFullSeason());
        btnReset.addActionListener(e -> resetSeason());

        controlPanel.add(btnSimulateNext);
        controlPanel.add(btnSimulateSeason);
        controlPanel.add(btnReset);
        controlPanel.add(new JSeparator(SwingConstants.VERTICAL));
        controlPanel.add(statusLabel);

        add(controlPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        String[] driverCols = {"Pos", "Driver", "Constructor", "Points", "Wins", "Podiums"};
        driverTableModel = new DefaultTableModel(driverCols, 0);
        JTable driverTable = new JTable(driverTableModel);
        tabbedPane.addTab("Drivers' Championship", new JScrollPane(driverTable));

        String[] constructorCols = {"Pos", "Constructor", "Points"};
        constructorTableModel = new DefaultTableModel(constructorCols, 0);
        JTable constructorTable = new JTable(constructorTableModel);
        tabbedPane.addTab("Constructors' Championship", new JScrollPane(constructorTable));

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane logScrollPane = new JScrollPane(logArea);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane, logScrollPane);
        mainSplit.setDividerLocation(600);

        add(mainSplit, BorderLayout.CENTER);
    }

    private void simulateNextRace() {
        Optional<RaceResult> resultOpt = seasonManager.simulateNextRace();
        if (resultOpt.isPresent()) {
            appendRaceLog(resultOpt.get());
            refreshTables();
            updateStatus();
        } else {
            JOptionPane.showMessageDialog(this, "The season has concluded! Reset to start a new season.");
        }
    }

    private void simulateFullSeason() {
        while (!seasonManager.isSeasonFinished()) {
            Optional<RaceResult> resultOpt = seasonManager.simulateNextRace();
            resultOpt.ifPresent(this::appendRaceLog);
        }
        refreshTables();
        updateStatus();
    }

    private void resetSeason() {
        seasonManager.resetSeason();
        logArea.setText("");
        refreshTables();
        updateStatus();
    }

    private void refreshTables() {
        driverTableModel.setRowCount(0);
        List<Driver> drivers = seasonManager.getDriverStandings();
        for (int i = 0; i < drivers.size(); i++) {
            Driver d = drivers.get(i);
            driverTableModel.addRow(new Object[]{
                    i + 1, d.getName(), d.getTeam().getName(), d.getPoints(), d.getWins(), d.getPodiums()
            });
        }

        constructorTableModel.setRowCount(0);
        List<Team> teams = seasonManager.getConstructorStandings();
        for (int i = 0; i < teams.size(); i++) {
            Team t = teams.get(i);
            constructorTableModel.addRow(new Object[]{
                    i + 1, t.getName(), t.getPoints()
            });
        }
    }

    private void updateStatus() {
        if (seasonManager.isSeasonFinished()) {
            Driver champion = seasonManager.getDriverStandings().get(0);
            statusLabel.setText("Season Finished! Champion: " + champion.getName());
            btnSimulateNext.setEnabled(false);
            btnSimulateSeason.setEnabled(false);
        } else {
            Track nextTrack = seasonManager.getNextTrack();
            statusLabel.setText(String.format("Round %d/%d: %s (%s)",
                    seasonManager.getCurrentRound(),
                    seasonManager.getTotalRounds(),
                    nextTrack.getName(),
                    nextTrack.getCountry()));
            btnSimulateNext.setEnabled(true);
            btnSimulateSeason.setEnabled(true);
        }
    }

    private void appendRaceLog(RaceResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("====================================================\n");
        sb.append(String.format(" RACE RESULTS: %s (%s)\n", result.getTrack().getName(), result.getTrack().getCountry()));
        sb.append("====================================================\n");
        sb.append(String.format("%-4s %-20s %-18s %-12s %-6s\n", "Pos", "Driver", "Team", "Time", "Pts"));
        sb.append("----------------------------------------------------\n");

        double winnerTime = result.getDriverDetails().get(0).getTotalTimeSeconds();

        for (DriverRaceDetail detail : result.getDriverDetails()) {
            String timeStr;
            if (detail.getFinishPosition() == 1) {
                timeStr = formatSeconds(detail.getTotalTimeSeconds());
            } else {
                double gap = detail.getTotalTimeSeconds() - winnerTime;
                timeStr = String.format("+%.3fs", gap);
            }

            sb.append(String.format("%-4d %-20s %-18s %-12s +%-5d\n",
                    detail.getFinishPosition(),
                    detail.getDriver().getName(),
                    detail.getDriver().getTeam().getName(),
                    timeStr,
                    detail.getPointsEarned()));
        }

        sb.append("\nFastest Lap: ")
                .append(result.getFastestLapDriver().getDriver().getName())
                .append(" (")
                .append(String.format("%.3fs", result.getFastestLapDriver().getFastestLapSeconds()))
                .append(")\n\n");

        logArea.append(sb.toString());
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private String formatSeconds(double totalSecs) {
        int mins = (int) (totalSecs / 60);
        double secs = totalSecs % 60;
        return String.format("%d:%06.3f", mins, secs);
    }
}