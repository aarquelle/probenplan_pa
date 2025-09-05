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
import org.aarquelle.probenplan_pa.ui.swt.SwtGui;
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

    public static @NotNull AddEntityButton<Scene> createAddSceneButton(Composite parent) {
        List<String> inputNames = List.of("Name:", "Length:", "Add after:");
        List<InputType> inputTypes = List.of(InputType.STRING, InputType.DOUBLE, InputType.SCENE_SELECT);

        return new AddEntityButton<>(parent, inputNames, inputTypes, l -> {
            Scene s = BasicService.createScene();
            s.setName((String) (l.getFirst()));
            s.setLength((Double) (l.get(1)));
            s.setPosition(BasicService.getPosAfterScene((Scene) (l.get(2))));
            BasicService.getScenes().sort();//TODO eleganter
            return s;
        });
    }

    public static @NotNull AddEntityButton<Role> createAddRoleButton(Composite parent) {
        List<String> inputNames = List.of("Name", "Actor");
        List<InputType> inputTypes = List.of(InputType.STRING, InputType.ACTOR_SELECT);
        return new AddEntityButton<>(parent, inputNames, inputTypes, l -> {
            Role r = BasicService.createRole();
            r.setName((String)(l.getFirst()));
            r.setActor((Actor) (l.get(1)));
            BasicService.getRoles().sort();
            return r;
        });
    }

    public static @NotNull AddEntityButton<Actor> createAddActorButton(Composite parent) {
        List<String> inputNames = List.of("Name");
        List<InputType> inputTypes = List.of(InputType.STRING);
        return new AddEntityButton<>(parent, inputNames, inputTypes, l -> {
            Actor a = BasicService.createActor();
            a.setName((String)l.getFirst());
            BasicService.getActors().sort();
            return a;
        });
    }

    public static @NotNull AddEntityButton<Rehearsal> createAddRehearsalButton(Composite parent) {
        List<String> inputNames = List.of("Date");
        List<InputType> inputTypes = List.of(InputType.DATE);
        return new AddEntityButton<>(parent, inputNames, inputTypes, l -> {
            Rehearsal r = BasicService.createRehearsal();
            r.setDate((LocalDate) l.getFirst());
            BasicService.getRehearsals().sort();
            return r;
        });
    }
}
