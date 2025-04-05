package org.aarquelle.probenplan_pa.business;

import org.aarquelle.probenplan_pa.data.dao.Transaction;
import org.aarquelle.probenplan_pa.dto.RehearsalDTO;
import org.aarquelle.probenplan_pa.dto.SceneDTO;
import org.aarquelle.probenplan_pa.util.Pair;

import java.util.List;
import java.util.Map;

public class BasicService {
    public static List<RehearsalDTO> getRehearsals() {
        try (Transaction t = new Transaction()) {
            return t.getReadDAO().getRehearsals();
        }
    }

    public static List<SceneDTO> getScenes() {
        try (Transaction t = new Transaction()) {
            return t.getReadDAO().getScenes();
        }
    }

    public static List<Pair<RehearsalDTO, SceneDTO>> getLockedScenes() {
        try (Transaction t = new Transaction()) {
            return t.getReadDAO().getLockedScenes();
        }
    }
}
