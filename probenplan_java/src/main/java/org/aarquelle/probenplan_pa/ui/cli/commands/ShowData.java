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
import org.aarquelle.probenplan_pa.entity.Actor;
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Role;
import org.aarquelle.probenplan_pa.entity.Scene;

import java.util.List;

import static org.aarquelle.probenplan_pa.ui.cli.out.Out.*;

public class ShowData extends AbstractCommand {

    public ShowData() {
        super("show-data", "Zeige alle Daten in der Datenbank an.");
    }

    @Override
    public void execute(String[] args) throws BusinessException {
        List<Actor> actors = BasicService.getActors().stream().sorted().toList();
        List<Role> roles = BasicService.getRoles().stream().sorted().toList();
        List<Scene> scenes = BasicService.getScenes().stream().sorted().toList();
        List<Rehearsal> rehearsals = BasicService.getRehearsals().stream().sorted().toList();

        info("Schauspielende:");
        for (Actor actor : actors) {
            prActor(actor);
            info(" mit Rollen:");
            for (Role role : actor.getRoles()) {
                pr("\t");
                prRole(role);
                line("");
            }
        }
        line("");
        info("Rollen:");
        for (Role role : roles) {
            prRole(role);
            infoPr(" gespielt von ");
            prActor(role.getActor());
            line("");
        }
        line("");
        info("Szenen:");
        for (Scene scene : scenes) {
            prScene(scene);
            infoPr(", Länge: ");
            pr(scene.getLength() + "");
            info(", Rollen:");
            for (Role r : scene.getBigRoles()) {
                pr("\t");
                prRole(r);
                line("");
            }
            for (Role r : scene.getSmallRoles()) {
                pr("\t");
                prRole(r);
                pr(" (klein)");
                line("");
            }
        }

        line("");
        info("Proben:");
        for (Rehearsal rehearsal : rehearsals) {
            prRehearsal(rehearsal);
            if (rehearsal.isFullLocked()) {
                info(" (voll festgelegt)");
            }
            if (!rehearsal.getMaybeActors().isEmpty() || !rehearsal.getMissingActors().isEmpty()) {
                info(" mit fehlenden Schauspielenden: ");
                for (Actor actor : rehearsal.getMissingActors()) {
                    pr("\t");
                    prActor(actor);
                    line("");
                }
                for (Actor actor : rehearsal.getMaybeActors()) {
                    pr("\t");
                    prActor(actor);
                    pr(" (vielleicht)");
                    line("");
                }
            }
            if (!rehearsal.getLockedScenes().isEmpty()) {
                info(" mit festgelegten Szenen: ");
                for (Scene scene : rehearsal.getLockedScenes()) {
                    pr("\t");
                    prScene(scene);
                    line("");
                }
            }
            if (rehearsal.getLockedScenes().isEmpty()
                    && rehearsal.getMaybeActors().isEmpty()
                    && rehearsal.getMissingActors().isEmpty()) {
                line("");
            }
        }
    }
}
