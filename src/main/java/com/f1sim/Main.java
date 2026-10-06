package com.f1sim;

import com.f1sim.ui.F1SimulatorGUI;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            F1SimulatorGUI gui = new F1SimulatorGUI();
            gui.setVisible(true);
        });
    }
}