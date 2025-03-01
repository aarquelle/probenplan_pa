package org.aarquelle.probenplan_pa.dto;

import java.util.*;

public class PlanDTO {
    private final Map<RehearsalDTO, List<SceneDTO>> plan = new HashMap<>();

    public void put(RehearsalDTO r, SceneDTO s) {
        if (!plan.containsKey(r)) {
            plan.put(r, new ArrayList<>(List.of(s)));
        } else {
            plan.get(r).add(s);
        }
    }

    public List<SceneDTO> get(RehearsalDTO r) {
        return plan.get(r);
    }

    public String verboseToString() {
        StringBuilder sb = new StringBuilder();
        List<RehearsalDTO> rehearsals = new ArrayList<>(plan.keySet());
        rehearsals.sort(Comparator.comparing(RehearsalDTO::getDate));


        for (RehearsalDTO r : rehearsals) {
            sb.append(r.getDate()).append(": ");
            List<SceneDTO> scenes = plan.get(r);
            scenes.sort(Comparator.comparing(SceneDTO::getPosition));
            for (SceneDTO scene : scenes) {
                sb.append(scene.getName()).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
            sb.append("\n");
        }
        return sb.toString();
    }
}
