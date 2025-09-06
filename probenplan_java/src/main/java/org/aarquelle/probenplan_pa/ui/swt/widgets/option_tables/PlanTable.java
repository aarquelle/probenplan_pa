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

package org.aarquelle.probenplan_pa.ui.swt.widgets.option_tables;

import org.aarquelle.probenplan_pa.business.Analyzer;
import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.business.InfoService;
import org.aarquelle.probenplan_pa.entity.Plan;
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Role;
import org.aarquelle.probenplan_pa.entity.Scene;
import org.aarquelle.probenplan_pa.ui.swt.ResourceHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import java.util.List;

public class PlanTable extends OptionTable<Rehearsal, Scene> {

    public PlanTable(Composite parent) {
        super(parent,
                BasicService.getScenes(),
                BasicService.getRehearsals(),
                List.of("Not planned.", "Planned.", "Planned and locked"),
                1,
                2,
                Display.getCurrent().getSystemColor(SWT.COLOR_RED),
                Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW),
                Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
    }

    @Override
    protected void drawMargin(TableCell<Scene, Rehearsal> cell, GC gc) {
        if (cell.row == 0) {
            if (cell.col > 1) {
                drawString(cell.colEntity.displayName(), cell, gc);
            }
        } else if (cell.col == 0) {
            drawString(cell.rowEntity.displayName(), cell, gc);
        } else if (cell.col == 1) {
            drawString(String.valueOf(BasicService.getPlan().lengthOfRehearsal(cell.rowEntity)), cell, gc);
        } else {
            throw new IllegalArgumentException("This is not in the margins: col: " + cell.col + ", row: " + cell.row);
        }
    }

    @Override
    protected void drawContent(TableCell<Scene, Rehearsal> cell, GC gc) {
        Color fillColor = ResourceHandler.getColor(colors[0], colors[1], colors[2],
                2 * Analyzer.completenessScore(cell.rowEntity, cell.colEntity) - 1);
        fillBackground(cell.col, cell.row, fillColor, gc);
        if (cell.getState() == 1) {
            markCell(cell.col, cell.row, gc);
        } else if (cell.getState() == 2) {
            heavyMarkCell(cell.col, cell.row, gc);
        }
    }

    @Override
    protected String additionalCommonTooltip(Scene colEntity, Rehearsal rowEntity) {
        return String.format("Value: %.2f\n", Analyzer.completenessScore(rowEntity, colEntity));
    }

    @Override
    protected void setInitialState(TableCell<Scene, Rehearsal> cell) {
        Plan plan = BasicService.getPlan();
        if (plan == null) {
            cell.setState(0);
        } else {
            //cell.setState(plan.hasScene(cell.rowEntity, cell.colEntity) ? 1 : 0);
            if (plan.hasScene(cell.rowEntity, cell.colEntity)) {
                cell.setState(1);
            }
            if (cell.rowEntity.getLockedScenes().contains(cell.colEntity)) {
                cell.setState(2);
            }
        }
    }

    @Override
    protected TableCell<Scene, Rehearsal> createContentCell(int col, int row) {
        Scene s = getColEntity(col);
        Rehearsal r = getRowEntity(row);
        return new TableCell<>(s, r, col, row, 3, getTooltips(s, r),
                (scene, rehearsal, state) -> {
                    Plan plan = BasicService.getPlan();
                    if (plan == null) {
                        return;
                    }
                    switch (state) {
                        case 0 -> {
                            rehearsal.removeLockedScene(scene);
                            plan.remove(rehearsal, scene);
                        }
                        case 1 -> {
                            rehearsal.removeLockedScene(scene);
                            plan.put(rehearsal, scene);
                        }
                        case 2 -> {
                            plan.put(rehearsal, scene);
                            rehearsal.addLockedScene(scene);
                        }
                        default ->
                                throw new IllegalArgumentException("Undefined value " + state + " for relation between "
                                        + scene + " and " + rehearsal);
                    }
                },
                colors);
    }

    @Override
    protected TableCell<Scene, Rehearsal> createMarginCell(int col, int row) {
        if (col == 0) {
            if (row == 0) {
                return new TableCell<>(col, row);
            } else {
                return new TableCell<>(null, getRowEntity(row), col, row, 1, null,
                        (a, b, c) -> {
                            System.out.println("Lock Rehearsal");
                        });
            }
        } else if (col == 1) {
            if (row == 0) {
                return new TableCell<>(col, row);
            } else {
                return new TableCell<>(null, getRowEntity(row), col, row, 1, null);
            }
        } else if (row == 0) {
            return new TableCell<>(getColEntity(col), null, col, row, 1, null);
        } else {
            throw new IllegalArgumentException("This is not in the margins: col: " + col + ", row: " + row);
        }
    }

    @Override
    protected String[] getTooltips(Scene scene, Rehearsal rehearsal) {
        String[] result = super.getTooltips(scene, rehearsal);
        for (int i = 0; i < result.length; i++) {
            List<Role> missingRoles = InfoService.missingRoles(scene, rehearsal);
            StringBuilder sb = new StringBuilder("\n");
            if (missingRoles.isEmpty()) {
                sb.append("All actors are available.");
            } else {
                sb.append("The actors for the following roles are maybe or definitely missing:");
                for (Role role : missingRoles) {
                    sb.append("\n").append(role.displayName());
                }
            }

            result[i]+= sb.toString();
        }
        return result;
    }

    @Override
    public void updateData() {
        Analyzer.runAnalysis();
        super.updateData();
    }
}
