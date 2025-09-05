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

import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.entity.Actor;
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.ui.swt.widgets.CustomElements;
import org.aarquelle.probenplan_pa.ui.swt.widgets.input.InputModal;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import java.util.List;

public class TimesTable extends InputTable<Rehearsal, Actor> {
    public TimesTable(Composite parent) {
        super(parent,
                BasicService.getActors(),
                BasicService.getRehearsals(),
                List.of("Hat Zeit", "Hat vielleicht Zeit", "Hat keine Zeit"),
                1,
                1,
                3,
                (actor,rehearsal,state) -> {
                    switch (state) {
                        case 0: {
                            rehearsal.removeMaybeActor(actor);
                            rehearsal.removeMissingActor(actor);
                            break;
                        }
                        case 1: {
                            rehearsal.removeMissingActor(actor);
                            rehearsal.addMaybeActor(actor);
                            break;
                        }
                        case 2: {
                            rehearsal.removeMaybeActor(actor);
                            rehearsal.addMissingActor(actor);
                            break;
                        }
                        default: {
                            throw new IllegalArgumentException("Undefined value " + state + " for relation between "
                                    + actor + " and " + rehearsal);
                        }
                    }
                },
                (a,r,s) -> CustomElements.modActorModal(a).open(),
                (a,r,s) -> CustomElements.modRehearsalModal(r).open(),
                Display.getCurrent().getSystemColor(SWT.COLOR_GREEN),
                Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW),
                Display.getCurrent().getSystemColor(SWT.COLOR_RED));
    }

    @Override
    protected void setInitialState(TableCell<Actor, Rehearsal> cell) {
        cell.setState(cell.colEntity.hasTimeOnRehearsal(cell.rowEntity).ordinal());
    }
}
