package org.aarquelle.probenplan_pa.ui.cli.commands;

import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.business.suggest.Analyzer;
import org.aarquelle.probenplan_pa.business.suggest.Evaluator;
import org.aarquelle.probenplan_pa.dto.ParamsDTO;
import org.aarquelle.probenplan_pa.dto.PlanDTO;
import org.aarquelle.probenplan_pa.dto.RehearsalDTO;
import org.aarquelle.probenplan_pa.dto.SceneDTO;
import org.aarquelle.probenplan_pa.ui.cli.out.Out;

import java.util.List;

public class Test extends AbstractCommand{
    int rehearsalCount = 0;
    PlanDTO plan;
    List<SceneDTO> ss = BasicService.getScenes();
    List<RehearsalDTO> rs = BasicService.getRehearsals();

    public Test() {
        super("test", "Test");
    }

    @Override
    public void execute(String[] args) throws BusinessException {
        rehearsalCount = 0;
        plan = new PlanDTO();
        Analyzer.runAnalysis();

        put(1,2);
        put(0,4);
        put(0,2,4,5);
        put(1,3);
        put(0,4);
        put(4);
        put(0,1,2,3,4,5);
        put(0,1,2,5);
        put(5);
        put(2);
        put(1,3);

        for (RehearsalDTO r : BasicService.getRehearsals()) {
            Out.prRehearsal(r);
            Out.infoPr(": ");
            plan.get(r).forEach(s -> {
                Out.prScene(s);
                Out.infoPr("(" + Analyzer.completenessScore(r, s) + ")");
                Out.infoPr(", ");
            });
            Out.line("");
        }
        System.out.println(new Evaluator(plan, new ParamsDTO()).evaluate());
        System.out.println(plan.getTestResults());

        rehearsalCount = 0;
        plan = new PlanDTO();
        Analyzer.runAnalysis();

        put(1,2);
        put(0,4);
        put(0,2,4,5);
        put(1,3);
        put(0,4,5);
        put(4);
        put(0,1,2,3,4,5);
        put(0,1,2,5);
        put();
        put(2);
        put(1,3);

        for (RehearsalDTO r : BasicService.getRehearsals()) {
            Out.prRehearsal(r);
            Out.infoPr(": ");
            plan.get(r).forEach(s -> {
                Out.prScene(s);
                Out.infoPr("(" + Analyzer.completenessScore(r, s) + ")");
                Out.infoPr(", ");
            });
            Out.line("");
        }
        System.out.println(new Evaluator(plan, new ParamsDTO()).evaluate());
        System.out.println(plan.getTestResults());
    }

    private void put(int... scene) {
        for (int s : scene) {
            plan.put(rs.get(rehearsalCount), ss.get(s));
        }
        rehearsalCount++;
    }
}
