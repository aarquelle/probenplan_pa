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

package org.aarquelle.probenplan_pa.ui.swt.widgets.input;

import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.entity.Actor;
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Role;
import org.aarquelle.probenplan_pa.entity.Scene;
import org.aarquelle.probenplan_pa.util.DateUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.time.LocalDate;

import static org.aarquelle.probenplan_pa.ui.swt.widgets.input.InputType.*;

public class InputWidget<T> extends Composite{
    private final Input<T> input;
    private final Label label;
    private final Control widget;

    public InputWidget(Composite parent, Input<T> input) {
        super(parent, 0);
        this.input = input;
        this.setLayout(new GridLayout(2, true));
        label = new Label(this, 0);
        label.setText(input.name() + ":");
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        widget = switch (input.type()) {
            case STRING, INT, DOUBLE, DATE -> new Text(this, SWT.SINGLE);
            case BOOL -> throw new IllegalArgumentException("NOT IMPLEMENTED"); //TODO Checkbox
            case SCENE_SELECT -> new DropdownMenu<>(this, BasicService.getScenes().toList(),
                    "Keine Szene", Scene::displayName);
            case ACTOR_SELECT -> new DropdownMenu<>(this, BasicService.getActors().toList(),
                    "Kein Schauspieler", Actor::displayName);
            case ROLE_SELECT -> new DropdownMenu<>(this, BasicService.getRoles().toList(),
                    "Keine Rolle", Role::displayName);
            case REHEARSAL_SELECT -> new DropdownMenu<>(this, BasicService.getRehearsals().toList(),
                    "Keine Probe", Rehearsal::displayName);
        };
        widget.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        if (input.initial() != null) {
            T initialValue = input.initial();
            switch (input.type()) {
                case STRING, INT, DOUBLE -> ((Text)widget).setText(String.valueOf(initialValue));
                case BOOL -> {//TODO
                }
                case DATE -> ((Text)widget).setText(DateUtils.getString((LocalDate) initialValue));
                case SCENE_SELECT -> ((DropdownMenu<Scene>)widget).select((Scene) initialValue);
                case ACTOR_SELECT -> ((DropdownMenu<Actor>)widget).select((Actor) initialValue);
                case ROLE_SELECT -> ((DropdownMenu<Role>)widget).select((Role) initialValue);
                case REHEARSAL_SELECT -> ((DropdownMenu<Rehearsal>)widget).select((Rehearsal) initialValue);
            }
        }
    }

    public String getName() {
        return input.name();
    }

    public String getString() {
        if (input.type() == InputType.STRING) {
            return ((Text)widget).getText();
        } else throw new IllegalArgumentException("Wrong type!");
    }

    public int getInt() {
        if (input.type() == INT) {
            return Integer.parseInt(((Text)widget).getText());
        } else throw new IllegalArgumentException("Wrong type!");
    }

    public double getDouble() {
        if (input.type() == DOUBLE) {
            return Double.parseDouble(((Text)widget).getText());
        } else throw new IllegalArgumentException("Wrong type!");
    }

    public boolean getBool() {
        if (input.type() == BOOL) {
            throw new IllegalArgumentException("NOT IMPLEMENTED");//TODO
        } else throw new IllegalArgumentException("Wrong type!");
    }

    public LocalDate getDate() {
        if (input.type() == DATE) {
            return DateUtils.getLocalDate(((Text)widget).getText());
        } else throw new IllegalArgumentException("Wrong type!");
    }

    public Scene getScene() {
        if (input.type() == SCENE_SELECT) {
            return ((DropdownMenu<Scene>)widget).getSelected();
        } else throw new IllegalArgumentException("Wrong type!");
    }

    public Actor getActor() {
        if (input.type() == ACTOR_SELECT) {
            return ((DropdownMenu<Actor>)widget).getSelected();
        } else throw new IllegalArgumentException("Wrong type!");
    }

    public Rehearsal getRehearsal() {
        if (input.type() == REHEARSAL_SELECT) {
            return ((DropdownMenu<Rehearsal>)widget).getSelected();
        } else throw new IllegalArgumentException("Wrong type!");
    }

    public Role getRole() {
        if (input.type() == ROLE_SELECT) {
            return ((DropdownMenu<Role>)widget).getSelected();
        } else throw new IllegalArgumentException("Wrong type!");
    }

    public void setText(String text) {
        if (input.type() == InputType.STRING) {
            ((Text)widget).setText(text);
        } else throw new IllegalArgumentException("Wrong type!");
    }

    @Override
    public void setToolTipText(String text) {
        super.setToolTipText(text);
        label.setToolTipText(text);
        widget.setToolTipText(text);
    }


}
