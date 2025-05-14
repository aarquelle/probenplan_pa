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

package org.aarquelle.probenplan_pa.business.suggest;

import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.dto.ParamsDTO;
import org.aarquelle.probenplan_pa.dto.PlanDTO;
import org.aarquelle.probenplan_pa.dto.RehearsalDTO;
import org.aarquelle.probenplan_pa.dto.SceneDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mutator {
    Random rand;
    ParamsDTO params;
    PlanDTO plan;
    RehearsalDTO dlp;
    RehearsalDTO potentialDlp;
    List<RehearsalDTO> allRehearsals;
    List<SceneDTO> allScenes;

    double evaluation;

    /**
     * All rehearsals that are not the DLP.
     */
    List<RehearsalDTO> freeRehearsals;

    public Mutator(long seed, ParamsDTO params) {
        rand = new Random(seed);
        this.params = params;
        this.plan = new PlanDTO();

        allRehearsals = BasicService.getRehearsals();
        allScenes = BasicService.getScenes();
        freeRehearsals = new ArrayList<>(allRehearsals);
    }


    /**
     * Adds a new random scene to a random rehearsal.
     * @return The mutated plan, or null if there would be no change.
     */
    public PlanDTO addScene() {
        RehearsalDTO r = randomRehearsal();
        SceneDTO s = randomScene();
        if (plan.hasScene(r, s)) {
            return null;
        } else {
            PlanDTO mutant = plan.copy();
            mutant.put(r, s);
            return mutant;
        }
    }

    /**
     * Removes a random scene from a random rehearsal.
     * @return The mutated plan, or null, if
     */
    public PlanDTO removeScene() {
        RehearsalDTO r = randomRehearsal();
        SceneDTO s = randomSceneFromPlan(r);
        if (s == null) {
            return null;
        } else {
            PlanDTO mutant = plan.copy();
            mutant.remove(r, s);
            return mutant;
        }
    }

    /**
     * Moves a random scene from one rehearsal to another.
     * @return The mutated plan.
     */
    public PlanDTO moveScene() {
        RehearsalDTO source = randomRehearsal();
        RehearsalDTO target = randomRehearsal();
        if (source.equals(target)) {
            return null;
        }
        SceneDTO s = randomSceneFromPlan(source);
        if (s == null || plan.hasScene(target, s)) {
            return null;
        }
        PlanDTO mutant = plan.copy();
        mutant.remove(source, s);
        mutant.put(target, s);
        return mutant;
    }

    /**
     * Exchanges a random scene in a random rehearsal with a new random scene.
     */
    public PlanDTO exchangeScene() {
        RehearsalDTO r = randomRehearsal();
        SceneDTO remove = randomSceneFromPlan(r);
        if (remove == null) {
            return null;
        }
        SceneDTO add = randomScene();
        if (plan.hasScene(r, add) || remove.equals(add)) {
            return null;
        }
        PlanDTO mutant = plan.copy();
        mutant.remove(r, remove);
        mutant.put(r, add);
        return mutant;
    }

    /**
     * Swaps two randoms scenes from two random rehearsals.
     */
    public PlanDTO swapScenes() {
        RehearsalDTO r1 = randomRehearsal();
        RehearsalDTO r2 = randomRehearsal();
        if (r1.equals(r2)) {
            return null;
        }
        SceneDTO s1 = randomSceneFromPlan(r1);
        SceneDTO s2 = randomSceneFromPlan(r2);
        if (s1 == null || s2 == null || s1.equals(s2)) {
            return null;
        }
        PlanDTO mutant = plan.copy();
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
    public PlanDTO setDLP() {
        RehearsalDTO target = randomRehearsal();
        if (target == dlp) {
            return null;
        }
        List<SceneDTO> oldScenes = new ArrayList<>(plan.get(target));
        PlanDTO mutant = plan.copy();
        addDLP(mutant, target);
        for (SceneDTO s : oldScenes) {
            RehearsalDTO rehearsal = randomRehearsal();
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
    public PlanDTO forceDLP() {
        RehearsalDTO target = randomRehearsal();
        if (target == dlp) {
            return null;
        }
        PlanDTO mutant = plan.copy();
        addDLP(mutant, target);
        potentialDlp = target;
        return mutant;
    }

    private RehearsalDTO randomRehearsal() {
        return freeRehearsals.get(rand.nextInt(freeRehearsals.size()));
    }

    private SceneDTO randomScene() {
        return allScenes.get(rand.nextInt(allScenes.size()));
    }

    private SceneDTO randomSceneFromPlan( RehearsalDTO rehearsal) {
        List<SceneDTO> scenes = plan.get(rehearsal);
        if (scenes.isEmpty()) {
            return null;
        } else {
            return scenes.get(rand.nextInt(scenes.size()));
        }
    }

    private void addDLP(PlanDTO plan, RehearsalDTO target) {
        plan.get(dlp).clear();
        plan.get(target).clear();
        for (SceneDTO s : allScenes) {
            plan.put(target, s);
        }
    }

    public void mutate() {
        int deadline = 0;
        int limit = 10000;
        Analyzer.runAnalysis();
        //plan = forceDLP();
        //dlp = potentialDlp;
        //potentialDlp = null;
        RehearsalDTO hardcodedDlp = allRehearsals.get(13);
        dlp = hardcodedDlp;
        addDLP(plan, hardcodedDlp);
        freeRehearsals.remove(dlp);
        while (deadline < limit) {
            deadline++;
            int choice = rand.nextInt(5);
            PlanDTO mutant;
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

            double newEvaluation = new Evaluator(mutant, params).evaluate();
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

    public PlanDTO getPlan() {
        return plan;
    }


}
