package org.aarquelle.probenplan_pa.dto;

public class ParamsDTO {
    private double earliestDurchlaufprobe = 0.33;
    private double latestDurchlaufprobe = 0.66;
    private double averageNumberOfRepeats = 2;

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

    public double getAverageNumberOfRepeats() {
        return averageNumberOfRepeats;
    }

    public void setAverageNumberOfRepeats(double averageNumberOfRepeats) {
        this.averageNumberOfRepeats = averageNumberOfRepeats;
    }
}
