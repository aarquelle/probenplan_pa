package org.aarquelle.probenplan_pa.business;

import org.aarquelle.probenplan_pa.data.dao.Transaction;
import org.aarquelle.probenplan_pa.dto.RehearsalDTO;
import org.aarquelle.probenplan_pa.dto.SceneDTO;

import java.util.List;

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
}
