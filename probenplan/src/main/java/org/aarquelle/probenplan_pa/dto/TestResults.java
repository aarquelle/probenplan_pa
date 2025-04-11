package org.aarquelle.probenplan_pa.dto;

public record TestResults(double totalCompleteness,
                          double dlpCompleteness,
                          double completenessBeforeDLP,
                          double lumpiness,
                          double minimumRepeats,
                          double medianRepeats,
                          double overSize,
                          double expectedNumberOfRepeats) {
    public String toString() {
        return "totalCompleteness: " + totalCompleteness
                + "\ndlpCompleteness: " + dlpCompleteness
                + "\ncompletenessBeforeDLP: " + completenessBeforeDLP
                + "\nlumpiness: " + lumpiness
                + "\nminimumRepeats: " + minimumRepeats
                + "\nmedianRepeats: " + medianRepeats
                + "\noverSize: " + overSize
                + "\nexpectedNumberOfRepeats: " + expectedNumberOfRepeats;
    }
}
