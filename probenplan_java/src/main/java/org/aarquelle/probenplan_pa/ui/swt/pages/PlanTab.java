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

package org.aarquelle.probenplan_pa.ui.swt.pages;

import org.aarquelle.probenplan_pa.business.Analyzer;
import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.business.Mutator;
import org.aarquelle.probenplan_pa.business.Params;
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Scene;
import org.aarquelle.probenplan_pa.ui.swt.widgets.CustomElements;
import org.aarquelle.probenplan_pa.ui.swt.widgets.option_tables.OptionTable;
import org.aarquelle.probenplan_pa.ui.swt.widgets.option_tables.PlanTable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

import java.util.List;

public class PlanTab extends Composite {

    OptionTable<Rehearsal, Scene> optionTable;

    public PlanTab(Composite parent) {
        super(parent, SWT.NONE);

        setLayout(new GridLayout());
        Display d = Display.getCurrent();

        Group importRow = CustomElements.createImportRow(this, "",
                List.of("Run analysis", "Generate"),
                List.of(false, false),
                List.of(() -> {
                    Analyzer.runAnalysis();
                    updateData();
                }, this::generate));
        optionTable = new PlanTable(this,
                BasicService.getScenes(),
                BasicService.getRehearsals(),
                List.of("Nicht geplant.", "Geplant"),
                d.getSystemColor(SWT.COLOR_RED),
                d.getSystemColor(SWT.COLOR_YELLOW),
                d.getSystemColor(SWT.COLOR_GREEN));
    }

    public void updateData() {
        optionTable.updateData();
        redraw();
        update();
    }

    private void generate() {
        Mutator mutator = new Mutator(System.currentTimeMillis());
        mutator.mutate(Params.getDeadline());
        BasicService.setPlan(mutator.getPlan());
        updateData();
    }
}
