package org.aarquelle.probenplan_pa.dto;

public class ParamsDTO {
    private double earliestDurchlaufprobe = 0.33;
    private double latestDurchlaufprobe = 0.66;
    private double averageRehearsalLength = 1;
    private int numberOfIterations = 50000;
    private int initialSeed = 0;

    public double getEarliestDurchlaufprobe() {
        return earliestDurchlaufprobe;
    }

    public void setEarliestDurchlaufprobe(double earliestDurchlaufprobe) {
        this.earliestDurchlaufprobe = earliestDurchlaufprobe;
    }

    public double getLatestDurchlaufprobe() {
        return latestDurchlaufprobe;
    }

    public void setLatestDurchlaufprobe(double latestDurchlaufprobe) {
        this.latestDurchlaufprobe = latestDurchlaufprobe;
    }

    public double getAverageRehearsalLength() {
        return averageRehearsalLength;
    }

    public void setAverageRehearsalLength(double averageRehearsalLength) {
        this.averageRehearsalLength = averageRehearsalLength;
    }

    public int getNumberOfIterations() {
        return numberOfIterations;
    }

    public void setNumberOfIterations(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    public int getInitialSeed() {
        return initialSeed;
    }

    public void setInitialSeed(int initialSeed) {
        this.initialSeed = initialSeed;
    }
}
