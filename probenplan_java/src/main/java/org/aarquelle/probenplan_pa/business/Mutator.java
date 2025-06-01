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

import org.aarquelle.probenplan_pa.entity.Plan;
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Mutator {
    Random rand;
    Plan plan;
    Rehearsal dlp;
    Rehearsal potentialDlp;
    List<Rehearsal> allRehearsals;
    List<Scene> allScenes;

    double evaluation = Double.NEGATIVE_INFINITY;

    /**
     * All rehearsals that are not the DLP.
     */
    List<Rehearsal> freeRehearsals;
    List<Rehearsal> dlpCandidates;

    public Mutator(long seed) {
        rand = new Random(seed);
        this.plan = new Plan();

        allRehearsals = BasicService.getRehearsals().stream().sorted().toList();
        allScenes = BasicService.getScenes().stream().sorted().toList();
        freeRehearsals = new ArrayList<>();
        freeRehearsals.addAll(allRehearsals.stream().filter(r -> !r.isFullLocked()).toList());
    }


    /**
     * Adds a new random scene to a random rehearsal.
     *
     * @return The mutated plan, or null if there would be no change.
     */
    public Plan addScene() {
        Rehearsal r = randomRehearsal();
        Scene s = randomScene();
        if (plan.hasScene(r, s)) {
            return null;
        } else {
            Plan mutant = plan.copy();
            mutant.put(r, s);
            return mutant;
        }
    }

    /**
     * Removes a random scene from a random rehearsal.
     *
     * @return The mutated plan, or null, if
     */
    public Plan removeScene() {
        Rehearsal r = randomRehearsal();
        Scene s = randomSceneFromPlan(r);
        if (s == null || r.getLockedScenes().contains(s)) {
            return null;
        } else {
            Plan mutant = plan.copy();
            mutant.remove(r, s);
            return mutant;
        }
    }

    /**
     * Moves a random scene from one rehearsal to another.
     *
     * @return The mutated plan.
     */
    public Plan moveScene() {
        Rehearsal source = randomRehearsal();
        Rehearsal target = randomRehearsal();
        if (source.equals(target)) {
            return null;
        }
        Scene s = randomSceneFromPlan(source);
        if (s == null || plan.hasScene(target, s) || source.getLockedScenes().contains(s)) {
            return null;
        }
        Plan mutant = plan.copy();
        mutant.remove(source, s);
        mutant.put(target, s);
        return mutant;
    }

    /**
     * Exchanges a random scene in a random rehearsal with a new random scene.
     */
    public Plan exchangeScene() {
        Rehearsal r = randomRehearsal();
        Scene remove = randomSceneFromPlan(r);
        if (remove == null || r.getLockedScenes().contains(remove)) {
            return null;
        }
        Scene add = randomScene();
        if (plan.hasScene(r, add) || remove.equals(add)) {
            return null;
        }
        Plan mutant = plan.copy();
        mutant.remove(r, remove);
        mutant.put(r, add);
        return mutant;
    }

    /**
     * Swaps two randoms scenes from two random rehearsals.
     */
    public Plan swapScenes() {
        Rehearsal r1 = randomRehearsal();
        Rehearsal r2 = randomRehearsal();
        if (r1.equals(r2)) {
            return null;
        }
        Scene s1 = randomSceneFromPlan(r1);
        Scene s2 = randomSceneFromPlan(r2);
        if (s1 == null || s2 == null || s1.equals(s2) || plan.hasScene(r1, s2) || plan.hasScene(r2, s1)
                || r1.getLockedScenes().contains(s1) || r2.getLockedScenes().contains(s2)) {
            return null;
        }
        Plan mutant = plan.copy();
        mutant.remove(r1, s1);
        mutant.remove(r2, s2);
        mutant.put(r1, s2);
        mutant.put(r2, s1);
        return mutant;
    }

    /**
     * Sets one rehearsal as Durchlaufprobe. All scenes from the existing DLP are removed. The scenes already present
     * in the new DLP are randomly scattered in other rehearsals. The dlp field is not changed.
     */
    public Plan setDLP() {
        Rehearsal target = randomDlpCandidate();
        if (target == dlp) {
            return null;
        }
        List<Scene> oldScenes = new ArrayList<>(plan.get(target));
        Plan mutant = plan.copy();
        addDLP(mutant, target);
        for (Scene s : oldScenes) {
            Rehearsal rehearsal = randomRehearsal();
            int timeout = 0;
            while (timeout < 20) {
                if (rehearsal.equals(target) || mutant.hasScene(rehearsal, s)) {
                    rehearsal = randomRehearsal();
                    timeout++;
                } else {
                    mutant.put(rehearsal, s);
                    break;
                }
            }
        }
        potentialDlp = target;
        return mutant;
    }

    /**
     * Sets the rehearsal as Durchlaufprobe. All scenes from the existing DLP are removed. The scenes already present
     * are ignored and not moved. The dlp field is not changed.
     */
    public Plan forceDLP() {
        Rehearsal target = randomDlpCandidate();
        if (target == dlp) {
            return null;
        }
        Plan mutant = plan.copy();
        addDLP(mutant, target);
        potentialDlp = target;
        return mutant;
    }

    private Rehearsal randomRehearsal() {
        return freeRehearsals.get(rand.nextInt(freeRehearsals.size()));
    }

    private Rehearsal randomDlpCandidate() {
        return dlpCandidates.get(rand.nextInt(dlpCandidates.size()));
    }

    private Scene randomScene() {
        return allScenes.get(rand.nextInt(allScenes.size()));
    }

    private Scene randomSceneFromPlan(Rehearsal rehearsal) {
        Set<Scene> scenes = plan.get(rehearsal);
        if (scenes.isEmpty()) {
            return null;
        } else {
            int i = rand.nextInt(scenes.size());
            for (Scene s : scenes) {
                if (i == 0) {
                    return s;
                }
                i--;
            }
        }
        throw new IllegalStateException("This should have returned earlier.");
    }

    private void findDlpCandidates() {
        dlpCandidates = new ArrayList<>();
        double[] evals = new double[freeRehearsals.size()];
        double maxEval = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < freeRehearsals.size(); i++) {
            Plan mutant = plan.copy();
            addDLP(mutant, freeRehearsals.get(i));
            evals[i] = new Evaluator(mutant).evaluate();
            if (evals[i] > maxEval) {
                maxEval = evals[i];
            }
        }

        for (int i = 0; i < evals.length; i++) {
            if (evals[i] == maxEval) {
                dlpCandidates.add(freeRehearsals.get(i));
            }
        }
    }

    private void buildLocks() {
        for (Rehearsal r : allRehearsals) {
            for (Scene s : r.getLockedScenes()) {
                plan.put(r, s);
            }
        }
    }

    private void addDLP(Plan plan, Rehearsal target) {
        if (dlp != null) {
            plan.get(dlp).retainAll(dlp.getLockedScenes());
        }
        plan.get(target).clear();
        for (Scene s : allScenes) {
            plan.put(target, s);
        }
    }

    public void mutate(int limit) {
        int deadline = 0;
        Analyzer.runAnalysis();
        findDlpCandidates();
        plan = forceDLP();
        buildLocks();
        dlp = potentialDlp;
        potentialDlp = null;
        freeRehearsals.remove(dlp);
        while (deadline < limit) {
            deadline++;
            int choice = rand.nextInt(7);
            Plan mutant;
            switch (choice) {
                case 0 -> mutant = addScene();
                case 1 -> mutant = removeScene();
                case 2 -> mutant = moveScene();
                case 3 -> mutant = exchangeScene();
                case 4 -> mutant = swapScenes();
                case 5 -> mutant = setDLP();
                case 6 -> mutant = forceDLP();
                default -> throw new IllegalStateException("Unexpected value: " + choice);
            }
            if (mutant == null) {
                continue;
            }

            double newEvaluation = new Evaluator(mutant).evaluate();
            if (newEvaluation > evaluation) {
                plan = mutant;
                evaluation = newEvaluation;
                if (potentialDlp != null) {
                    if (dlp != null) {
                        freeRehearsals.add(dlp);
                    }
                    dlp = potentialDlp;
                    freeRehearsals.remove(potentialDlp);
                    potentialDlp = null;
                }
                System.out.println("Evaluation: " + evaluation + ", deadline: " + deadline + ", choice: " + choice);
                deadline = 0;
            } else {
                potentialDlp = null;
            }
        }
    }

    public Plan getPlan() {
        return plan;
    }


}
