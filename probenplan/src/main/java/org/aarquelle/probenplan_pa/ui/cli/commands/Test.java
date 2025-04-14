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

        put();
        put();
        put(0,1,2,5);
        put(1,3,4);
        put(3,4,5);
        put(1);
        put(0,1,2,3,4,5);
        put(1,2,3);
        put(0,2,5);
        put(2);
        put(4,5);

        System.out.println(new Evaluator(plan, new ParamsDTO()).evaluate());
        Out.plan(plan);

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

        Out.plan(plan);
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
