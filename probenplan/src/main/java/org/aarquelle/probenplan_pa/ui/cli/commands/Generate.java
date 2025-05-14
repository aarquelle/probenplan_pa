/*
 * Copyright (c) 2025, Aaron Prott
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.aarquelle.probenplan_pa.ui.cli.commands;

import org.aarquelle.probenplan_pa.Main;
import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.business.suggest.Generator;
import org.aarquelle.probenplan_pa.business.suggest.Mutator;
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
        ParamsDTO params;
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

        Mutator mutator = new Mutator(System.currentTimeMillis(), params);
        mutator.mutate();
        PlanDTO plan = mutator.getPlan();

        /*ProgressBar progressBar = new ProgressBar(params.getNumberOfIterations() / Main.NUMBER_OF_CORES);
        Out.hideCursor();
        PlanDTO plan = Generator.generateBestPlan(params, progressBar);
        progressBar.finish();
        Out.showCursor();*/
        Out.info("Generierter Plan:");
        Out.plan(plan);

        DecimalFormat df = new DecimalFormat("#.###");
        String time = df.format((double)(System.currentTimeMillis() - startTime) / 1000);
        Out.success("Found plan in " + time + " seconds.");

        Main.plan = plan;
    }
}
