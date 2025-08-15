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

package org.aarquelle.probenplan_pa.ui;

import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.entity.Actor;
import org.aarquelle.probenplan_pa.entity.Entity;
import org.aarquelle.probenplan_pa.entity.Plan;
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Role;
import org.aarquelle.probenplan_pa.entity.Scene;

public class API {
    /**
     * Returns a value that represents the relationship between two {@link Entity}s.
     * Intended for graphical use, where such values can be mapped to different colors.
     * The order should not matter.
     * @throws IllegalArgumentException If no relationship is defined.
     */
    public static int relation(Entity a, Entity b) {
        if (a instanceof Actor actor) {
            if (b instanceof Role role) {
                return role.getActor() == actor ? 1 : 0;
            }
            if (b instanceof Rehearsal rehearsal) {
                return actor.hasTimeOnRehearsal(rehearsal).ordinal();
            }
        } else if (a instanceof Role role) {
            if (b instanceof Actor) {
                return relation(b, a);
            }
            if (b instanceof Scene scene) {
                return role.sizeOfScene(scene).ordinal();
            }
        } else if (a instanceof Scene scene) {
            if (b instanceof Role) {
                return relation(b, a);
            }
            if (b instanceof Rehearsal rehearsal) {
                Plan plan = BasicService.getPlan();
                if (plan == null) {
                    return 0;
                } else {
                    return plan.hasScene(rehearsal, scene) ? 1 : 0;
                }
            }
        } else if (a instanceof Rehearsal) {
            if (! (b instanceof Rehearsal)) {
                return relation(b, a);
            }
        }


        throw new IllegalArgumentException("Undefined relation between " + a + " and " + b);

    }
}
