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
import org.aarquelle.probenplan_pa.entity.DataState;
import org.aarquelle.probenplan_pa.entity.Scene;

import java.util.List;

public class EnforceScene extends AbstractCommand {
    public EnforceScene() {
        super("enforce-scene", "Diese Szenen werden auf jeden Fall geprobt.");
    }

    @Override
    public void execute(String[] args) throws BusinessException {
        if (args.length == 0) {
            throw new BusinessException("Syntax: enforce-scene <scene-name>...");
        }
        else {
            Scene[] scenes = new Scene[args.length];
            for (int i = 0; i < args.length; i++) {
                scenes[i] = BasicService.getSceneByName(args[i]);
                if (scenes[i] == null) {
                    throw new BusinessException("No such scene name: " + args[i]);
                }
            }
            for (Scene scene : scenes) {
                DataState.getInstance().enforceScene(scene);
            }
        }
    }
}
