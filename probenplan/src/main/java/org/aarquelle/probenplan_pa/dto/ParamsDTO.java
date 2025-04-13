package org.aarquelle.probenplan_pa.dto;

public class ParamsDTO {
    private double earliestDurchlaufprobe = 0;
    private double latestDurchlaufprobe = 1;
    private double averageRehearsalLength = 1;
    private int numberOfIterations = 100000;
    private long initialSeed = 1;

    //Evaluation weights
    private double completenessWeight = 4;
    private double dlpCompletenessWeight = 1;
    private double completenessBeforeDLPWeight = 1;
    private double lumpinessWeight = 1;
    private double minimumRepeatsWeight = 1;
    private double medianRepeatsWeight = 0.5;
    private double overSizeWeight = 2;

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

    public long getInitialSeed() {
        return initialSeed;
    }

    public void setInitialSeed(long initialSeed) {
        this.initialSeed = initialSeed;
    }

    public double getCompletenessWeight() {
        return completenessWeight;
    }

    public void setCompletenessWeight(double completenessWeight) {
        this.completenessWeight = completenessWeight;
    }

    public double getDlpCompletenessWeight() {
        return dlpCompletenessWeight;
    }

    public void setDlpCompletenessWeight(double dlpCompletenessWeight) {
        this.dlpCompletenessWeight = dlpCompletenessWeight;
    }

    public double getCompletenessBeforeDLPWeight() {
        return completenessBeforeDLPWeight;
    }

    public void setCompletenessBeforeDLPWeight(double completenessBeforeDLPWeight) {
        this.completenessBeforeDLPWeight = completenessBeforeDLPWeight;
    }

    public double getLumpinessWeight() {
        return lumpinessWeight;
    }

    public void setLumpinessWeight(double lumpinessWeight) {
        this.lumpinessWeight = lumpinessWeight;
    }

    public double getMinimumRepeatsWeight() {
        return minimumRepeatsWeight;
    }

    public void setMinimumRepeatsWeight(double minimumRepeatsWeight) {
        this.minimumRepeatsWeight = minimumRepeatsWeight;
    }

    public double getMedianRepeatsWeight() {
        return medianRepeatsWeight;
    }

    public void setMedianRepeatsWeight(double medianRepeatsWeight) {
        this.medianRepeatsWeight = medianRepeatsWeight;
    }

    public double getOverSizeWeight() {
        return overSizeWeight;
    }

    public void setOverSizeWeight(double overSizeWeight) {
        this.overSizeWeight = overSizeWeight;
    }

    public double getTotalWeight() {
        return completenessWeight + dlpCompletenessWeight + completenessBeforeDLPWeight
                + lumpinessWeight + minimumRepeatsWeight + medianRepeatsWeight + overSizeWeight;
    }
}
