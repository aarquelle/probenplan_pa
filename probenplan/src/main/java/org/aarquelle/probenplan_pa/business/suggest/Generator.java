package org.aarquelle.probenplan_pa.business.suggest;

import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.dto.ParamsDTO;
import org.aarquelle.probenplan_pa.dto.PlanDTO;
import org.aarquelle.probenplan_pa.dto.RehearsalDTO;
import org.aarquelle.probenplan_pa.dto.SceneDTO;
import org.aarquelle.probenplan_pa.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Generator {
    Random random;
    List<RehearsalDTO> rehearsals;
    List<SceneDTO> scenes;
    ParamsDTO params;


    public Generator(long seed, ParamsDTO params) {
        scenes = BasicService.getScenes();
        rehearsals = BasicService.getRehearsals();
        this.params = params;

        validateParams();

        random = new Random(seed);
    }

    private void validateParams() {
        if (params.getAverageRehearsalLength() < 0) {
            throw new IllegalArgumentException("Average number of repeats must be positive");
        }

        if (params.getEarliestDurchlaufprobe() < 0 || params.getEarliestDurchlaufprobe() > 1) {
            throw new IllegalArgumentException("Earliest Durchlaufprobe must be between 0 and 1");
        }

        if (params.getLatestDurchlaufprobe() < 0 || params.getLatestDurchlaufprobe() > 1) {
            throw new IllegalArgumentException("Latest Durchlaufprobe must be between 0 and 1");
        }

        if (params.getEarliestDurchlaufprobe() > params.getLatestDurchlaufprobe()) {
            throw new IllegalArgumentException("Earliest Durchlaufprobe must be less than or equal to " +
                    "Latest Durchlaufprobe");
        }
    }

    public PlanDTO generatePlan() {
        PlanDTO result = new PlanDTO();
        RehearsalDTO durchlaufprobe = chooseRandomRehearsal(
                params.getEarliestDurchlaufprobe(), params.getLatestDurchlaufprobe());
        addDurchlaufprobe(result, durchlaufprobe);


        double lengthOfPlay = scenes.stream()
                .mapToDouble(SceneDTO::getLength)
                .sum();
        int amountOfAllScenes = (int)((params.getAverageRehearsalLength() * scenes.size() * rehearsals.size())
                / lengthOfPlay);

        List<Pair<RehearsalDTO, SceneDTO>> lockedScenes = BasicService.getLockedScenes();
        for (Pair<RehearsalDTO, SceneDTO> pair : lockedScenes) {
            result.put(pair.first(), pair.second());
        }

        for (int i = 0; i < amountOfAllScenes - lockedScenes.size(); i++) {
            RehearsalDTO rehearsal = durchlaufprobe;
            while (rehearsal == durchlaufprobe) {
                rehearsal = rehearsals.get(random.nextInt(rehearsals.size()));
            }
            SceneDTO scene = scenes.get(random.nextInt(scenes.size()));
            result.put(rehearsal, scene);
        }

        /*List<SceneDTO> sceneCards = new ArrayList<>();
        List<RehearsalDTO> rehearsalCards = new ArrayList<>();

        for (int i = 0; i < params.getAverageRehearsalLength(); i++) { //TODO check for non-integer values
            sceneCards.addAll(scenes);
            Collections.shuffle(sceneCards, random);
            while (!sceneCards.isEmpty()) {
                if (rehearsalCards.isEmpty()) {
                    rehearsalCards.addAll(rehearsals);
                    rehearsalCards.remove(durchlaufprobe); // TODO kann optimiert werden
                    Collections.shuffle(rehearsalCards, random);
                }
                RehearsalDTO resultRehearsal = rehearsalCards.getFirst();
                SceneDTO resultScene = sceneCards.getFirst();
                if (result.get(resultRehearsal) != null && result.get(resultRehearsal).contains(resultScene)) {
                    rehearsalCards.removeFirst();
                    continue;
                }
                result.put(resultRehearsal, resultScene);
                rehearsalCards.removeFirst();
                sceneCards.removeFirst();
            }
        }*/

        return result;
    }

    /**
     * Chooses a random rehearsal from the list of rehearsals. Earliest and latest refer to the proportional position
     * of the rehearsal in the chain of rehearsals. For example, with earliest=0.0 and latest=0.5, the rehearsal will be
     * chosen from the first half of the rehearsals.
     */
    private RehearsalDTO chooseRandomRehearsal(double earliest, double latest) {
        int start = (int) (rehearsals.size() * earliest);
        int end = (int) (rehearsals.size() * latest);
        int result = random.nextInt(start, end);
        return rehearsals.get(result);
    }

    private void addDurchlaufprobe(PlanDTO plan, RehearsalDTO rehearsal) {
        for (SceneDTO scene : scenes) {
            plan.put(rehearsal, scene);
        }
    }
}
