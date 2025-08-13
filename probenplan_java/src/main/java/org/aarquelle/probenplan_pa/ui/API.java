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
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Role;
import org.aarquelle.probenplan_pa.entity.Scene;
import org.aarquelle.probenplan_pa.util.DateUtils;

import java.util.List;

public class API {
    public static Actor getActor(int i) {
        return BasicService.getActors().stream().sorted().toList().get(i);
    }

    public static Scene getScene(int i) {
        return BasicService.getScenes().stream().sorted().toList().get(i);
    }

    public static Rehearsal getRehearsal(int i) {
        return BasicService.getRehearsals().stream().sorted().toList().get(i);
    }

    public static Role getRole(int i) {
        return BasicService.getRoles().stream().sorted().toList().get(i);
    }

    public static List<String> getActorNames() {
        return BasicService.getActors().stream().map(Actor::getName).sorted().toList();
    }

    public static List<String> getRoleNames() {
        return BasicService.getRoles().stream().map(Role::getName).sorted().toList();
    }
    public static List<String> getSceneNames() {
        return BasicService.getScenes().stream().map(Scene::getName).sorted().toList();
    }

    public static List<String> getRehearsalNames() {
        return BasicService.getRehearsals().stream().sorted().map(r -> DateUtils.getString(r.getDate())).toList();
    }


    public static int getRoleSize(int role, int scene) {
        return getRole(role).sizeOfScene(getScene(scene)).ordinal();
    }

    public static int hasTime(int actor, int rehearsal) {
        return getActor(actor).hasTimeOnRehearsal(getRehearsal(rehearsal)).ordinal();
    }
}
