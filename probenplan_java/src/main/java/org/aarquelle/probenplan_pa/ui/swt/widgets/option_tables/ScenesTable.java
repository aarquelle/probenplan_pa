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
import org.aarquelle.probenplan_pa.entity.Role;
import org.aarquelle.probenplan_pa.entity.Scene;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import java.util.List;

public class ScenesTable extends InputTable<Scene, Role> {
    public ScenesTable(Composite parent) {
        super(parent,
                BasicService.getRoles(),
                BasicService.getScenes(),
                List.of("Kommt nicht vor.", "Kleine Rolle", "Große Rolle"),
                1,
                1,
                3,
                (role, scene, state) -> {
                    switch (state) {
                        case 0: {
                            role.removeBigScene(scene);
                            role.removeSmallScene(scene);
                            break;
                        }
                        case 1: {
                            role.removeBigScene(scene);
                            role.addSmallScene(scene);
                            break;
                        }
                        case 2: {
                            role.removeSmallScene(scene);
                            role.addBigScene(scene);
                            break;
                        }
                        default: {
                            throw new IllegalArgumentException("Undefined value " + state + " for relation between "
                                    + scene + " and " + role);
                        }
                    }
                },
                null,
                Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW),
                Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
    }

    @Override
    protected void setInitialState(TableCell<Role, Scene> cell) {
        cell.setState(cell.colEntity.sizeOfScene(cell.rowEntity).ordinal());
    }
}
