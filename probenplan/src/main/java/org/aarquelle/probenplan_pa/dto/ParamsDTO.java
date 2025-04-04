package org.aarquelle.probenplan_pa.dto;

public class ParamsDTO {
    private double earliestDurchlaufprobe = 0.33;
    private double latestDurchlaufprobe = 0.66;
    private double averageRehearsalLength = 2;

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
}
