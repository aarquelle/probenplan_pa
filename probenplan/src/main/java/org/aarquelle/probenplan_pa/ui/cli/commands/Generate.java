package org.aarquelle.probenplan_pa.ui.cli.commands;

import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.business.suggest.Generator;
import org.aarquelle.probenplan_pa.dto.ParamsDTO;
import org.aarquelle.probenplan_pa.dto.PlanDTO;
import org.aarquelle.probenplan_pa.ui.cli.out.Out;

public class Generate extends AbstractCommand {

    public Generate() {
        super("generate", "Generiere Daten für die Anwendung. " +
                "Verwende diesen Befehl, um Testdaten zu generieren.");
    }

    @Override
    public void execute(String[] args) throws BusinessException {
        Out.info("Generiere Testdaten... Das kann einen kurzen Moment dauern, bitte warten.");
        PlanDTO plan = Generator.generateBestPlan(new ParamsDTO());
        Out.info("Generierter Plan:");
        Out.info(plan.verboseToString());
    }
}
