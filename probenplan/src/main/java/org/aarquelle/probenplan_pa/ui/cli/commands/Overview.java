package org.aarquelle.probenplan_pa.ui.cli.commands;

import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.business.suggest.Analyzer;
import org.aarquelle.probenplan_pa.dto.RehearsalDTO;
import org.aarquelle.probenplan_pa.dto.SceneDTO;

import java.util.List;

import static org.aarquelle.probenplan_pa.ui.cli.out.Out.*;

public class Overview extends AbstractCommand {
    public Overview() {
        super("overview", "Zeigt an, wie gut eine Szene in einer bestimmten Probe geprobt werden kann.");
    }

    @Override
    public void execute(String[] args) throws BusinessException {
        List<RehearsalDTO> rehearsals = BasicService.getRehearsals();
        List<SceneDTO> scenes = BasicService.getScenes();
        Analyzer.runAnalysis();

        info("Nach Proben sortiert: ");
        for (RehearsalDTO r : rehearsals) {
            info("Probe am " + r.getDate() + ":");
            for (SceneDTO s : scenes) {
                prScene(s);
                infoPr("(");
                percentage(Analyzer.completenessScore(r, s));
                info(")");
            }
        }

        info("Nach Szenen sortiert: ");
        for (SceneDTO s : scenes) {
            info("Szene " + s.getName() + ":");
            for (RehearsalDTO r : rehearsals) {
                prRehearsal(r);
                infoPr("(");
                percentage(Analyzer.completenessScore(r, s));
                info(")");
            }
        }
    }
}
