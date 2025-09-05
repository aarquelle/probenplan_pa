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

package org.aarquelle.probenplan_pa.ui.swt.widgets;

import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.entity.Actor;
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Role;
import org.aarquelle.probenplan_pa.entity.Scene;
import org.aarquelle.probenplan_pa.ui.swt.widgets.input.Input;
import org.aarquelle.probenplan_pa.ui.swt.widgets.input.InputModal;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;

import static org.aarquelle.probenplan_pa.ui.swt.widgets.input.InputType.*;

public class CustomElements {
    public static Group createImportRow(Composite parent, String title, List<String> buttonNames,
                                        List<Boolean> withText,
                                        List<Runnable> functions) {
        if (buttonNames.size() != withText.size() || buttonNames.size() != functions.size()) {
            throw new IllegalArgumentException("Wrong number of arguments in List: "
                    + buttonNames + "; " + withText + "; " + functions.size());
        }
        Group g = new Group(parent, SWT.BORDER);
        g.setLayoutData(new GridData(SWT.FILL, SWT.END, true, false));

        int numCols = 0;
        for (Boolean b : withText) {
            numCols += b ? 2 : 1;
        }

        g.setLayout(new GridLayout(numCols, true));
        if (title != null) {
            g.setText(title);
        }

        for (int i = 0; i < buttonNames.size(); i++) {
            Button button;
            if (withText.get(i)) {
                Composite textComp = new Composite(g, SWT.BORDER);
                textComp.setLayoutData(new GridData(SWT.FILL, SWT.END, true, false, 2, 1));
                GridLayout textLayout = new GridLayout(2, false);
                textComp.setLayout(textLayout);
                Text text = new Text(textComp, SWT.BORDER);
                text.setLayoutData(new GridData(SWT.FILL, SWT.END, true, false));
                button = new Button(textComp, SWT.PUSH);
            } else {
                button = new Button(g, SWT.PUSH);
                button.setLayoutData(new GridData((i < buttonNames.size() - 1) ? SWT.BEGINNING : SWT.END, SWT.CENTER,
                        false, false));
            }
            button.setText(buttonNames.get(i));
            Runnable r = functions.get(i);
            button.addListener(SWT.Selection, e -> r.run());
        }
        return g;
    }

    public static @NotNull Button createAddSceneButton(Composite parent) {
        List<Input<?>> inputs = List.of(new Input<>("Name", STRING), new Input<>("Length", DOUBLE),
                new Input<>("Add after", SCENE_SELECT));

        Button b = new Button(parent, SWT.PUSH);
        b.setText("Add a new scene");
        b.addListener(SWT.Selection, e -> new InputModal("Add new scene",
                inputs,
                l -> {
                    Scene s = BasicService.createScene();
                    s.setName(l.getFirst().getString());
                    s.setLength(l.get(2).getDouble());
                    s.setPosition(BasicService.getPosAfterScene(l.get(3).getScene()));
                    BasicService.getScenes().sort();//TODO eleganter
                }).open());
        return b;
    }

    public static @NotNull Button createAddRoleButton(Composite parent) {
        List<Input<?>> inputs = List.of(new Input<>("Name", STRING), new Input<>("Actor", ACTOR_SELECT));
        Button b = new Button(parent, SWT.PUSH);
        b.setText("Add a new role");
        b.addListener(SWT.Selection, e -> new InputModal("Add new role",
                inputs, l -> {
            Role r = BasicService.createRole();
            r.setName(l.getFirst().getString());
            r.setActor(l.get(1).getActor());
            BasicService.getRoles().sort();
        }).open());
        return b;
    }

    public static @NotNull Button createAddActorButton(Composite parent) {
        List<Input<?>> inputs = List.of(new Input<>("Name", STRING));
        Button b = new Button(parent, SWT.PUSH);
        b.setText("Add a new actor");
        b.addListener(SWT.Selection, e -> new InputModal("Add new actor",
                inputs, l -> {
            Actor a = BasicService.createActor();
            a.setName(l.getFirst().getString());
            BasicService.getActors().sort();
        }).open());
        return b;
    }

    public static @NotNull Button createAddRehearsalButton(Composite parent) {
        List<Input<?>> inputs = List.of(new Input<>("Date", DATE));
        Button b = new Button(parent, SWT.PUSH);
        b.setText("Add a new rehearsal");
        b.addListener(SWT.Selection, e -> new InputModal("Add new rehearsal",
                inputs, l -> {
            Rehearsal r = BasicService.createRehearsal();
            r.setDate(l.getFirst().getDate());
            BasicService.getRehearsals().sort();
        }).open());
        return b;
    }

    public static @NotNull InputModal modActorModal(Actor a) {
        List<Input<?>> inputs = List.of(new Input<>("Name", STRING, a.getName()));
        return new InputModal("Modify " + a.displayName(),
                inputs, l -> {
            a.setName(l.getFirst().getString());
            BasicService.getActors().sort();
        }).addButton("Delete actor",
                true, "Are you sure you want to delete actor "
                        + a.displayName() + "?",
                l -> BasicService.removeActor(a));
    }

    public static @NotNull InputModal modRehearsalModal(Rehearsal r) {
        List<Input<?>> inputs = List.of(new Input<>("Date", DATE, r.getDate()));
        return new InputModal("Modify " + r.displayName(),
                inputs, l -> {
            r.setDate(l.getFirst().getDate());
            BasicService.getRehearsals().sort();
        }).addButton("Delete rehearsal",
                true, "Are you sure you want to delete rehearsal "
                        + r.displayName() + "?",
                l -> BasicService.removeRehearsal(r));
    }

    public static @NotNull InputModal modSceneModal(Scene s) {
        List<Input<?>> inputs = List.of(new Input<>("Name", STRING, s.getName()),
                new Input<>("Length", DOUBLE, s.getLength()),
                new Input<>("Add after", SCENE_SELECT, BasicService.getPredecessor(s)));
        return new InputModal("Modify " + s.displayName(), inputs,
                l -> {
                    s.setName(l.getFirst().getString());
                    s.setLength(l.get(1).getDouble());
                    s.setPosition(BasicService.getPosAfterScene(l.get(2).getScene()));
                    BasicService.getScenes().sort();
                })
                .addButton("Delete Scene",
                        true, "Are you sure you want to delete scene "
                                + s.displayName() + "?",
                        l -> BasicService.removeScene(s));
    }

    public static @NotNull InputModal modRoleModal(Role r) {
        List<Input<?>> inputs = List.of(new Input<>("Name", STRING, r.getName()),
                new Input<>("Actor", ACTOR_SELECT, r.getActor()));
        return new InputModal("Modify " + r.displayName(), inputs,
                l -> {
                    r.setName(l.getFirst().getString());
                    r.setActor(l.get(1).getActor());
                    BasicService.getRoles().sort();
                })
                .addButton("Delete role",
                        true, "Are you sure you want to delete role "
                                + r.displayName() + "?",
                        l -> BasicService.removeRole(r));
    }
}
