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
import org.aarquelle.probenplan_pa.business.InfoService;
import org.aarquelle.probenplan_pa.business.Mutator;
import org.aarquelle.probenplan_pa.business.Params;
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Scene;
import org.aarquelle.probenplan_pa.ui.swt.SwtGui;
import org.aarquelle.probenplan_pa.ui.swt.widgets.CustomElements;
import org.aarquelle.probenplan_pa.ui.swt.widgets.option_tables.OptionTable;
import org.aarquelle.probenplan_pa.ui.swt.widgets.option_tables.PlanTable;
import org.aarquelle.probenplan_pa.util.CsvUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;

import java.util.List;

public class PlanTab extends Composite {

    OptionTable<Rehearsal, Scene> optionTable;

    public PlanTab(Composite parent) {
        super(parent, SWT.NONE);

        setLayout(new GridLayout());
        Display d = Display.getCurrent();

        Group importRow = CustomElements.createImportRow(this, "",
                List.of("Generate", "Export"),
                List.of(false, false),
                List.of(this::generate, this::copyToClipboard));
        optionTable = new PlanTable(this);
    }

    public void updateData() {
        optionTable.updateData();
    }

    private void generate() {
        Mutator mutator = new Mutator(BasicService.getInitialSeed());
        mutator.mutate(Params.getDeadline());
        BasicService.setPlan(mutator.getPlan());
        redraw();
    }

    private void copyToClipboard() {
        CsvUtils.copyToClipboard(InfoService.csvPlan(BasicService.getPlan()));
        MessageBox box = new MessageBox(SwtGui.INSTANCE.getMainShell(), SWT.OK);
        box.setMessage("The plan has been copied to your clipboard. You can paste it into a spreadsheet program" +
                " like Google Sheets, LibreOffice Calc or Microsoft Excel.");
        box.open();
    }

    @Override
    public void redraw() {
        super.redraw();
        optionTable.redraw();
    }
}
