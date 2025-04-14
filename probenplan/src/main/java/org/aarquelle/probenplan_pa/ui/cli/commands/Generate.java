package org.aarquelle.probenplan_pa.ui.cli.commands;

import org.aarquelle.probenplan_pa.Main;
import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.business.suggest.Generator;
import org.aarquelle.probenplan_pa.dto.ParamsDTO;
import org.aarquelle.probenplan_pa.dto.PlanDTO;
import org.aarquelle.probenplan_pa.ui.cli.out.Out;
import org.aarquelle.probenplan_pa.ui.cli.out.ProgressBar;

import java.text.DecimalFormat;

public class Generate extends AbstractCommand {

    public Generate() {
        super("generate", "Generiere Daten für die Anwendung. " +
                "Verwende diesen Befehl, um Testdaten zu generieren.");
    }

    @Override
    public void execute(String[] args) throws BusinessException {
        Out.info("Generiere Testdaten... Das kann einen kurzen Moment dauern, bitte warten.");
        long startTime = System.currentTimeMillis();
        ParamsDTO params = null;
        try {
            params = Main.params.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        if (args != null && args.length > 0) {
            params.setNumberOfIterations(Integer.parseInt(args[0]));
            if (args.length > 1) {
                params.setInitialSeed(Long.parseLong(args[1]));
            } else {
                params.setInitialSeed(System.currentTimeMillis());
            }
        }

        ProgressBar progressBar = new ProgressBar(params.getNumberOfIterations() / Main.NUMBER_OF_CORES);
        PlanDTO plan = Generator.generateBestPlan(params, progressBar::update);
        progressBar.finish();
        Out.info("Generierter Plan:");
        Out.plan(plan);

        DecimalFormat df = new DecimalFormat("#.###");
        String time = df.format((double)(System.currentTimeMillis() - startTime) / 1000);
        System.out.println("Found plan in " + time + " seconds.");

        Main.plan = plan;
    }
}
