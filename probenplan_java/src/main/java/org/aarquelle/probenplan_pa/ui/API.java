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
     *
     * @throws IllegalArgumentException If no relationship is defined.
     */
    public static int getRelation(Entity a, Entity b) {
        if (a instanceof Actor actor) {
            if (b instanceof Rehearsal rehearsal) {
                return actor.hasTimeOnRehearsal(rehearsal).ordinal();
            }
        } else if (a instanceof Role role) {
            if (b instanceof Scene scene) {
                return role.sizeOfScene(scene).ordinal();
            }
        } else if (a instanceof Scene scene) {
            if (b instanceof Role) {
                return getRelation(b, a);
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
            if (!(b instanceof Rehearsal)) {
                return getRelation(b, a);
            }
        }

        throw new IllegalArgumentException("Undefined relation between " + a + " and " + b);
    }

    public static void setRelation(Entity a, Entity b, int value) {
        if (a == null && b == null) {//TODO: Namen ändern
            return;
        }

        if (a instanceof Actor actor) {
            if (b instanceof Rehearsal rehearsal) {
                switch (value) {
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
                        throw new IllegalArgumentException("Undefined value " + value + " for relation between "
                                + a + " and " + b);
                    }
                }
            }
        } else if (a instanceof Role role) {
            if (b instanceof Scene scene) {
                switch (value) {
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
                        throw new IllegalArgumentException("Undefined value " + value + " for relation between "
                                + a + " and " + b);
                    }
                }
            }
        } else if (a instanceof Scene scene) {
            if (b instanceof Role) {
                setRelation(b, a, value);
            }
            if (b instanceof Rehearsal rehearsal) {
                Plan plan = BasicService.getPlan();
                if (plan == null) {
                    return;
                }
                if (value == 1) {
                    plan.put(rehearsal, scene);
                } else if (value == 0) {
                    plan.remove(rehearsal, scene);
                } else {
                    throw new IllegalArgumentException("Undefined value " + value + " for relation between "
                            + a + " and " + b);
                }
            }
        } else if (a instanceof Rehearsal) {
            if (!(b instanceof Rehearsal)) {
                setRelation(b, a, value);
            }
        } else {
            throw new IllegalArgumentException("Undefined relation between " + a + " and " + b);
        }
    }
}
