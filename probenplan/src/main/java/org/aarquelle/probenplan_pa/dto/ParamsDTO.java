/*
 * Copyright (c) 2025, Aaron Prott
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.aarquelle.probenplan_pa.dto;

public class ParamsDTO implements Cloneable {
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

    public String getValueFromString(String name) {
        return switch (name) {
            case "earliestDurchlaufprobe" -> String.valueOf(earliestDurchlaufprobe);
            case "latestDurchlaufprobe" -> String.valueOf(latestDurchlaufprobe);
            case "averageRehearsalLength" -> String.valueOf(averageRehearsalLength);
            case "numberOfIterations" -> String.valueOf(numberOfIterations);
            case "initialSeed" -> String.valueOf(initialSeed);
            case "completenessWeight" -> String.valueOf(completenessWeight);
            case "dlpCompletenessWeight" -> String.valueOf(dlpCompletenessWeight);
            case "completenessBeforeDLPWeight" -> String.valueOf(completenessBeforeDLPWeight);
            case "lumpinessWeight" -> String.valueOf(lumpinessWeight);
            case "minimumRepeatsWeight" -> String.valueOf(minimumRepeatsWeight);
            case "medianRepeatsWeight" -> String.valueOf(medianRepeatsWeight);
            case "overSizeWeight" -> String.valueOf(overSizeWeight);
            default -> throw new IllegalArgumentException("Invalid parameter name: " + name);
        };
    }

    public void setValueFromString(String name, String value) {
        switch (name) {
            case "earliestDurchlaufprobe" -> earliestDurchlaufprobe = Double.parseDouble(value);
            case "latestDurchlaufprobe" -> latestDurchlaufprobe = Double.parseDouble(value);
            case "averageRehearsalLength" -> averageRehearsalLength = Double.parseDouble(value);
            case "numberOfIterations" -> numberOfIterations = Integer.parseInt(value);
            case "initialSeed" -> initialSeed = Long.parseLong(value);
            case "completenessWeight" -> completenessWeight = Double.parseDouble(value);
            case "dlpCompletenessWeight" -> dlpCompletenessWeight = Double.parseDouble(value);
            case "completenessBeforeDLPWeight" -> completenessBeforeDLPWeight = Double.parseDouble(value);
            case "lumpinessWeight" -> lumpinessWeight = Double.parseDouble(value);
            case "minimumRepeatsWeight" -> minimumRepeatsWeight = Double.parseDouble(value);
            case "medianRepeatsWeight" -> medianRepeatsWeight = Double.parseDouble(value);
            case "overSizeWeight" -> overSizeWeight = Double.parseDouble(value);
            default -> throw new IllegalArgumentException("Invalid parameter name: " + name);
        }
    }

    public String[] getParamNames() {
        return new String[]{
                "earliestDurchlaufprobe",
                "latestDurchlaufprobe",
                "averageRehearsalLength",
                "numberOfIterations",
                "initialSeed",
                "completenessWeight",
                "dlpCompletenessWeight",
                "completenessBeforeDLPWeight",
                "lumpinessWeight",
                "minimumRepeatsWeight",
                "medianRepeatsWeight",
                "overSizeWeight"
        };
    }

    public ParamsDTO clone() throws CloneNotSupportedException {
        ParamsDTO cloned = (ParamsDTO) super.clone();
        return cloned;
        /*ParamsDTO clone = new ParamsDTO();
        clone.earliestDurchlaufprobe = this.earliestDurchlaufprobe;
        clone.latestDurchlaufprobe = this.latestDurchlaufprobe;
        clone.averageRehearsalLength = this.averageRehearsalLength;
        clone.numberOfIterations = this.numberOfIterations;
        clone.initialSeed = this.initialSeed;
        clone.completenessWeight = this.completenessWeight;
        clone.dlpCompletenessWeight = this.dlpCompletenessWeight;
        clone.completenessBeforeDLPWeight = this.completenessBeforeDLPWeight;
        clone.lumpinessWeight = this.lumpinessWeight;
        clone.minimumRepeatsWeight = this.minimumRepeatsWeight;
        clone.medianRepeatsWeight = this.medianRepeatsWeight;
        clone.overSizeWeight = this.overSizeWeight;
        return clone;*/
    }
}
