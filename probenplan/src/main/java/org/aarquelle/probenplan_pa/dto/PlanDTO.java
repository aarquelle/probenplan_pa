package org.aarquelle.probenplan_pa.dto;

import org.aarquelle.probenplan_pa.util.Pair;

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
        List<SceneDTO> scenes = plan.get(r);
        if (scenes == null) {
            return Collections.emptyList();
        } else {
            scenes.sort(Comparator.comparing(SceneDTO::getPosition));
            return scenes;
        }
    }

    public double totalLength() {
        double length = 0;
        for (List<SceneDTO> sceneList : plan.values()) {
            for (SceneDTO scene : sceneList) {
                length += scene.getLength();
            }
        }
        return length;
    }

    public List<RehearsalDTO> getRehearsals() {
        List<RehearsalDTO> rehearsals = new ArrayList<>(plan.keySet());
        rehearsals.sort(Comparator.comparing(RehearsalDTO::getDate));
        return rehearsals;
    }

    public List<SceneDTO> getScenes() {
        Set<SceneDTO> scenes = new HashSet<>();
        for (List<SceneDTO> sceneList : plan.values()) {
            scenes.addAll(sceneList);
        }
        List<SceneDTO> l = new ArrayList<>(scenes);
        l.sort(Comparator.comparing(SceneDTO::getPosition));
        return l;
    }

    public String verboseToString() {
        StringBuilder sb = new StringBuilder();
        List<RehearsalDTO> rehearsals = getRehearsals();

        for (RehearsalDTO r : rehearsals) {
            sb.append(r.getDate()).append(": ");
            List<SceneDTO> scenes = get(r);
            for (SceneDTO scene : scenes) {
                sb.append(scene.getName()).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
            sb.append("\n");
        }
        return sb.toString();
    }

    public List<Pair<RehearsalDTO, SceneDTO>> getAllPairs() {
        List<Pair<RehearsalDTO, SceneDTO>> pairs = new ArrayList<>();
        plan.forEach((r, value) -> value.forEach(s -> pairs.add(new Pair<>(r, s))));
        return pairs;
    }
}
