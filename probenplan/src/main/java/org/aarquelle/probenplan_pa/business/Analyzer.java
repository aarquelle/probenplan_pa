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

import org.aarquelle.probenplan_pa.entity.Actor;
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Role;
import org.aarquelle.probenplan_pa.entity.Scene;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Analyzer {

    static RehearsalSceneTable<Integer> allMissing;
    static RehearsalSceneTable<Integer> majorMissing;
    static RehearsalSceneTable<Integer> allUncertain;
    static RehearsalSceneTable<Integer> majorUncertain;

    static RehearsalSceneTable<Double> scoreTable;

    static List<Scene> allScenes;
    static int numberOfRoles;
    static double lengthOfPlay;


    public static void runAnalysis() {
        allScenes = getAllScenes();
        lengthOfPlay = calculateLengthOfPlay();

        allMissing = missingActors();
        majorMissing = majorMissingActors();
        allUncertain = uncertainActors();
        majorUncertain = majorUncertainActors();
        scoreTable = completenessScores();
        numberOfRoles = getTotalNumberOfRoles();
    }

    public static List<Scene> getAllScenes() {
        if (allScenes == null) {
            allScenes = BasicService.getScenes().stream().sorted().toList();
        }
        return allScenes;
    }

    public static double calculateLengthOfPlay() {
        double result = 0;
        for (Scene scene : allScenes) {
            result += scene.getLength();
        }
        return result;
    }

    public static int getNumberOfMissingActorsForScene(Rehearsal rehearsal, Scene scene, boolean maybe, boolean major) {
        Set<Actor> unavailableActors = new HashSet<>();
        if (maybe) {
            unavailableActors.addAll(rehearsal.getMaybeActors());
        } else {
            unavailableActors.addAll(rehearsal.getMissingActors());
        }
        Set<Role> neededRoles = new HashSet<>(scene.getBigRoles());
        if (!major) {
            neededRoles.addAll(scene.getSmallRoles());
        }
        Set<Actor> neededActors = neededRoles.stream().map(Role::getActor).collect(Collectors.toSet());
        unavailableActors.retainAll(neededActors);
        return unavailableActors.size();
    }

    public static int getNumberOfMinorMissingActorsForScene(Rehearsal rehearsal, Scene scene) {
        Set<Actor> unavailableActors = new HashSet<>(rehearsal.getMissingActors());
        Set<Actor> minorActors = scene.getSmallRoles().stream().map(Role::getActor).collect(Collectors.toSet());
        unavailableActors.retainAll(minorActors);
        return unavailableActors.size();
    }

    /**
     * Returns a table with the number of missing actors for each scene in each rehearsal. This only counts actors
     * who definitely don't have time, not including those who maybe have time.
     */
    public static RehearsalSceneTable<Integer> missingActors() {
        return mapFunctionToSceneTable((rehearsal, scene)
                -> getNumberOfMissingActorsForScene(rehearsal, scene, false, false));
    }

    /**
     * Returns a table with the number of actors who maybe have time for each scene in each rehearsal.
     */
    public static RehearsalSceneTable<Integer> uncertainActors() {
        return mapFunctionToSceneTable((rehearsal, scene)
                -> getNumberOfMissingActorsForScene(rehearsal, scene, true, false));
    }

    public static RehearsalSceneTable<Integer> majorMissingActors() {
        return mapFunctionToSceneTable(
                (rehearsal, scene) -> getNumberOfMissingActorsForScene(rehearsal, scene, false, true));
    }

    public static RehearsalSceneTable<Integer> majorUncertainActors() {
        return mapFunctionToSceneTable(
                (rehearsal, scene) -> getNumberOfMissingActorsForScene(rehearsal, scene, true, true));
    }

    public static RehearsalSceneTable<Double> completenessScores() {
        return mapFunctionToSceneTable(
                (Analyzer::calculateCompletenessScore)
        );
    }

    public static double completenessScore(Rehearsal rehearsal, Scene scene) {
        return scoreTable.get(rehearsal, scene);
    }

    /**
     * Returns a value between 0 and 1 that represents how many actors are present in the rehearsal.
     * 1 means that all actors are definitely present, 0 means that no actors are present.
     */
    private static double calculateCompletenessScore(Rehearsal rehearsal, Scene scene) {
        int numberOfTotalRoles = scene.getBigRoles().size() + scene.getSmallRoles().size();
        int numberOfMajorRoles = scene.getBigRoles().size();
        int numberOfMissing = allMissing.get(rehearsal, scene);
        int numberOfUncertain = allUncertain.get(rehearsal, scene);
        int numberOfMajorMissing = majorMissing.get(rehearsal, scene);
        int numberOfMajorUncertain = majorUncertain.get(rehearsal, scene);

        int numberOfMinorRoles = numberOfTotalRoles - numberOfMajorRoles;
        int numberOfMinorMissing = numberOfMissing - numberOfMajorMissing;
        int numberOfMinorUncertain = numberOfUncertain - numberOfMajorUncertain;

        double majorPoints = ratio(2 * numberOfMajorRoles, 2 * numberOfMajorRoles + numberOfMinorRoles);
        double minorPoints = 1 - majorPoints;

        double score = ratio(numberOfMajorMissing, numberOfMajorRoles) * majorPoints
                + ratio(numberOfMinorMissing, numberOfMinorRoles) * minorPoints
                + ratio(numberOfMajorUncertain, numberOfTotalRoles) * 0.5 * majorPoints
                + ratio(numberOfMinorUncertain, numberOfTotalRoles) * 0.5 * minorPoints;

        return 1 - score;
    }

    public static boolean isNextScene(Scene first, Scene second) {
        return allScenes.indexOf(first) + 1 == allScenes.indexOf(second);
    }

    public static int getNumberOfLumps(Scene... scenes) {
        Arrays.sort(scenes);
        int result = 1;
        for (int i = 0; i < scenes.length - 1; i++) {
            if (!isNextScene(scenes[i], scenes[i + 1])) {
                result++;
            }
        }
        return result;
    }

    private static double ratio (int a, int b) {
        if (b != 0) {
            return a / (double) b;
        } else {
            if (a == 0) {
                return 0;
            } else {
                throw new IllegalArgumentException("Dividing " + a + " by 0");
            }
        }
    }

    private static <N> RehearsalSceneTable<N> mapFunctionToSceneTable(
            BiFunction<Rehearsal, Scene, N> function) {
        Set<Scene> scenes = BasicService.getScenes();
        Set<Rehearsal> rehearsals = BasicService.getRehearsals();

        RehearsalSceneTable<N> table = new RehearsalSceneTable<>();
        for (Rehearsal rehearsal : rehearsals) {
            for (Scene scene : scenes) {
                table.set(rehearsal, scene, function.apply(rehearsal, scene));
            }
        }
        return table;
    }

    private static int getTotalNumberOfRoles() {
        return BasicService.getRoles().size();
    }

}
