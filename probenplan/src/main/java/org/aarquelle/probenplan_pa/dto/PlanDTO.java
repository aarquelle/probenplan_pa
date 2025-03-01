package org.aarquelle.probenplan_pa.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanDTO {
    private final Map<RehearsalDTO, List<SceneDTO>> plan = new HashMap<>();

    public void put(RehearsalDTO r, SceneDTO s) {
        if (!plan.containsKey(r)) {
            plan.put(r, List.of(s));
        } else {
            plan.get(r).add(s);
        }
    }

    public List<SceneDTO> get(RehearsalDTO r) {
        return plan.get(r);
    }
}
