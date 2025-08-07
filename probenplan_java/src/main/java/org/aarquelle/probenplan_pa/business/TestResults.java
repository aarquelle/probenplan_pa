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

import org.jetbrains.annotations.NotNull;

public record TestResults(
        double totalScore,
        double totalCompleteness,
        double dlpCompleteness,
        double completenessBeforeDLP,
        double lumpiness,
        double minimumRepeats,
        double medianRepeats,
        double averageRepeats,
        double overSize,
        double expectedNumberOfRepeats,
        double numberOfRoles,
        double enforcedScenes) {
    public @NotNull String toString() {
        return  "totalScore: " + totalScore
                + "\ntotalCompleteness: " + totalCompleteness
                + "\ndlpCompleteness: " + dlpCompleteness
                + "\ncompletenessBeforeDLP: " + completenessBeforeDLP
                + "\nlumpiness: " + lumpiness
                + "\nminimumRepeats: " + minimumRepeats
                + "\nmedianRepeats: " + medianRepeats
                + "\naverageRepeats: " + averageRepeats
                + "\noverSize: " + overSize
                + "\nexpectedNumberOfRepeats: " + expectedNumberOfRepeats
                + "\nnumberOfRolesScore: " + numberOfRoles
                + "\nenforcedScenes: " + enforcedScenes
                + "\n";
    }
}
