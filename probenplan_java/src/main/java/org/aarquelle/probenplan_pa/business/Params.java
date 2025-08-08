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

package org.aarquelle.probenplan_pa.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Params {
    static Map<String, Para<?>> paraMap = new HashMap<>();

    private Params() {}

    public static void init() {
        Para<Double> earliestDurchlaufprobe = new Para<>("earliestDurchlaufprobe", 0.0);
        earliestDurchlaufprobe.minValue = 0.0;
        earliestDurchlaufprobe.maxValue = 1.0;
        addPara(earliestDurchlaufprobe);

        Para<Double> latestDurchlaufprobe = new Para<>("latestDurchlaufprobe", 1.0);
        latestDurchlaufprobe.minValue = 0.0;
        latestDurchlaufprobe.maxValue = 1.0;
        addPara(latestDurchlaufprobe);

        Para<Double> averageRehearsalLength = new Para<>("averageRehearsalLength", 1.0);
        averageRehearsalLength.minValue = 0.01;
        averageRehearsalLength.maxValue = Double.MAX_VALUE;
        addPara(averageRehearsalLength);

        Para<Long> initialSeed = new Para<>("initialSeed", 0L);
        addPara(initialSeed);

        Para<Double> completenessWeight = new Para<>("completenessWeight", 3.0);
        completenessWeight.minValue = 0.0;
        completenessWeight.maxValue = Double.MAX_VALUE;
        addPara(completenessWeight);

        Para<Double> dlpCompletenessWeight = new Para<>("dlpCompletenessWeight", 1.0);
        dlpCompletenessWeight.minValue = 0.0;
        dlpCompletenessWeight.maxValue = Double.MAX_VALUE;
        addPara(dlpCompletenessWeight);

        Para<Double> completenessBeforeDLPWeight = new Para<>("completenessBeforeDLPWeight", 1.0);
        completenessBeforeDLPWeight.minValue = 0.0;
        completenessBeforeDLPWeight.maxValue = Double.MAX_VALUE;
        addPara(completenessBeforeDLPWeight);

        Para<Double> lumpinessWeight = new Para<>("lumpinessWeight", 1.0);
        lumpinessWeight.minValue = 0.0;
        lumpinessWeight.maxValue = Double.MAX_VALUE;
        addPara(lumpinessWeight);

        Para<Double> minimumRepeatsWeight = new Para<>("minimumRepeatsWeight", 3.0);
        minimumRepeatsWeight.minValue = 0.0;
        minimumRepeatsWeight.maxValue = Double.MAX_VALUE;
        addPara(minimumRepeatsWeight);

        Para<Double> medianRepeatsWeight = new Para<>("medianRepeatsWeight", 0.5);
        medianRepeatsWeight.minValue = 0.0;
        medianRepeatsWeight.maxValue = Double.MAX_VALUE;
        addPara(medianRepeatsWeight);

        Para<Double> averageRepeatsWeight = new Para<>("averageRepeatsWeight", 3.0);
        averageRepeatsWeight.minValue = 0.0;
        averageRepeatsWeight.maxValue = Double.MAX_VALUE;
        addPara(averageRepeatsWeight);

        Para<Double> overSizeWeight = new Para<>("overSizeWeight", 2.0);
        overSizeWeight.minValue = 0.0;
        overSizeWeight.maxValue = Double.MAX_VALUE;
        addPara(overSizeWeight);

        Para<Double> numberOfRolesWeight = new Para<>("numberOfRolesWeight", 2.0);
        numberOfRolesWeight.minValue = 0.0;
        numberOfRolesWeight.maxValue = Double.MAX_VALUE;
        addPara(numberOfRolesWeight);

        Para<Integer> deadline = new Para<>("deadline", 10000);
        deadline.description = "How many unsuccessful mutations are allowed before the algorithm stops.";
        deadline.minValue = 0;
        deadline.maxValue = Integer.MAX_VALUE;
        addPara(deadline);

        Para<Integer> optimalNumberOfActors = new Para<>("optimalNumberOfActors", 4);
        optimalNumberOfActors.description = "The program will try not have more actors in a rehearsal than this.";
        optimalNumberOfActors.minValue = 1;
        optimalNumberOfActors.maxValue = Integer.MAX_VALUE;
        addPara(optimalNumberOfActors);

    }

    public static List<Para<?>> getAllParams() {
        return paraMap.values().stream().sorted().toList();
    }

    public static void reset() {
        paraMap.values().forEach(Para::reset);
    }

    private static void addPara(Para<?> para) {
        if (para != null) {
            paraMap.put(para.name, para);
        } else {
            throw new IllegalArgumentException("Parameter cannot be null");
        }
    }

    public static Para<?> getPara(String name) {
        return paraMap.get(name);
    }

    public static Number getValue(String name) {
        Para<?> para = getPara(name);
        if (para != null) {
            return para.value;
        } else {
            throw new IllegalArgumentException("Parameter " + name + " not found");
        }
    }

    public static void setPara(String name, String value) throws BusinessException {
        Para<?> para = getPara(name);
        if (para == null) {
            throw new BusinessException("Parameter " + name + " not found");
        }
        para.setValue(value);
    }


    public static double getEarliestDurchlaufprobe() {
        return getValue("earliestDurchlaufprobe").doubleValue();
    }

    public static double getLatestDurchlaufprobe() {
        return getValue("latestDurchlaufprobe").doubleValue();
    }

    public static double getAverageRehearsalLength() {
        return getValue("averageRehearsalLength").doubleValue();
    }

    public static long getInitialSeed() {
        return getValue("initialSeed").longValue();
    }

    public static double getCompletenessWeight() {
        return getValue("completenessWeight").doubleValue();
    }

    public static double getDlpCompletenessWeight() {
        return getValue("dlpCompletenessWeight").doubleValue();
    }

    public static double getCompletenessBeforeDLPWeight() {
        return getValue("completenessBeforeDLPWeight").doubleValue();
    }

    public static double getLumpinessWeight() {
        return getValue("lumpinessWeight").doubleValue();
    }

    public static double getMinimumRepeatsWeight() {
        return getValue("minimumRepeatsWeight").doubleValue();
    }

    public static double getMedianRepeatsWeight() {
        return getValue("medianRepeatsWeight").doubleValue();
    }

    public static double getOverSizeWeight() {
        return getValue("overSizeWeight").doubleValue();
    }

    public static double getNumberOfRolesWeight() {
        return getValue("numberOfRolesWeight").doubleValue();
    }

    public static int getDeadline() {
        return getValue("deadline").intValue();
    }

    public static double getTotalWeight() {
        return getCompletenessWeight() + getDlpCompletenessWeight() + getCompletenessBeforeDLPWeight()
                + getLumpinessWeight() + getMinimumRepeatsWeight() + getMedianRepeatsWeight() + getOverSizeWeight()
                + getNumberOfRolesWeight() + getAverageRepeatsWeight();
    }

    public static String getValueFromString(String name) {
        return String.valueOf(getPara(name).value);
    }

    public static List<Para<?>> getParamNames() {
        return paraMap.values().stream().sorted().toList();
    }

    public static double getAverageRepeatsWeight() {
        return getValue("averageRepeatsWeight").doubleValue();
    }

    public static int getOptimalNumberOfActors() {
        return getValue("optimalNumberOfActors").intValue();
    }
}
