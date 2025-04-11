package org.aarquelle.probenplan_pa.ui.cli.commands;

import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.business.suggest.Analyzer;
import org.aarquelle.probenplan_pa.dto.ActorDTO;
import org.aarquelle.probenplan_pa.dto.RehearsalDTO;
import org.aarquelle.probenplan_pa.dto.RoleDTO;
import org.aarquelle.probenplan_pa.dto.SceneDTO;
import org.aarquelle.probenplan_pa.util.Pair;

import java.util.List;

import static org.aarquelle.probenplan_pa.ui.cli.out.Out.*;

public class ShowData extends AbstractCommand {

    public ShowData() {
        super("show-data", "Zeige alle Daten in der Datenbank an.");
    }

    @Override
    public void execute(String[] args) throws BusinessException {
        Analyzer.runAnalysis();
        List<ActorDTO> actors = BasicService.getActors();
        List<RoleDTO> roles = BasicService.getRoles();
        List<SceneDTO> scenes = BasicService.getScenes();
        List<RehearsalDTO> rehearsals = BasicService.getRehearsals();

        info("Schauspielende:");
        for (ActorDTO actor : actors) {
            prActor(actor);
            info(" mit Rollen:");
            for (RoleDTO role : roles) {
                if (role.getActor().equals(actor)) {
                    pr("\t");
                    prRole(role);
                    line("");
                }
            }
        }
        line("");
        info("Rollen:");
        for (RoleDTO role : roles) {
            prRole(role);
            infoPr(" gespielt von ");
            prActor(role.getActor());
            line("");
        }
        line("");
        info("Szenen:");
        for (SceneDTO scene : scenes) {
            prScene(scene);
            infoPr(", Länge: " );
            pr(scene.getLength()+"");
            info(", Rollen:");
            List<Pair<RoleDTO, Boolean>> roleMapList = Analyzer.rolesForScene.get(scene);
            for (Pair<RoleDTO, Boolean> roleDTOBooleanPair : roleMapList) {
                pr("\t");
                prRole(roleDTOBooleanPair.first());
                if (roleDTOBooleanPair.second()) {
                    pr(" (klein)");
                }
                line("");
            }
        }

        line("");
        info("Proben:");
        for (RehearsalDTO rehearsal : rehearsals) {
            prRehearsal(rehearsal);
            if (Analyzer.missingActorsForRehearsal.containsKey(rehearsal)) {
                info(" mit fehlenden Schauspielenden: ");
                for (Pair<ActorDTO, Boolean> pair : Analyzer.missingActorsForRehearsal.get(rehearsal)) {
                    pr("\t");
                    prActor(pair.first());
                    if (pair.second()) {
                        pr(" (vielleicht)");
                    }
                    line("");
                }
            } else {
                line("");
            }
        }
    }
}
