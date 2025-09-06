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

package org.aarquelle.probenplan_pa.business;

import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Role;
import org.aarquelle.probenplan_pa.entity.Scene;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The infos calculated here are intended for human consumption, not for the evaluator.
 */
public class InfoService {
    public static List<Role> missingRoles(Scene s, Rehearsal rehearsal) {
        Set<Role> l = new HashSet<>(s.getSmallRoles());
        l.addAll(s.getBigRoles());
        return l.stream().filter(role -> rehearsal.getMissingActors().contains(role.getActor()) ||
                rehearsal.getMaybeActors().contains(role.getActor())).toList();
    }
}
