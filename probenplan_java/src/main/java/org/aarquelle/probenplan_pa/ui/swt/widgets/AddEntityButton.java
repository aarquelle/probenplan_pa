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
import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.entity.Actor;
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Role;
import org.aarquelle.probenplan_pa.entity.Scene;
import org.aarquelle.probenplan_pa.ui.swt.SwtGui;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class AddEntityButton<E> {
    Button mainButton;

    public AddEntityButton(Composite parent, List<String> inputNames, List<InputType> inputTypes, Runnable update,
                           Function<List<Object>, E> constructFunction) {
        assert(inputTypes.size() == inputNames.size());

        mainButton = new Button(parent, SWT.PUSH);
        mainButton.addListener(SWT.Selection, ev -> {
            Shell modal = new Shell(SwtGui.INSTANCE.getMainShell(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.RESIZE);
            modal.setText("Create new Scene");
            modal.setLayout(new GridLayout(2, true));

            List<InputWidget> inputWidgets = new ArrayList<>();

            for (int i = 0; i < inputNames.size(); i++) {

                Label label = new Label(modal, 0);
                label.setText(inputNames.get(i));

                InputWidget input;
                switch (inputTypes.get(i)) {
                    case INT, STRING, DOUBLE -> {
                        input = new InputText(modal, SWT.SINGLE);
                    }
                    case BOOL -> {
                        //TODO checkbox oder so
                        input = null;
                    }
                    case DATE -> {
                        //TODO Date selector
                        input = null;
                    }
                    case SCENE_SELECT -> {
                        input = new DropdownMenu<>(modal, BasicService.getScenes().toList(),
                                "No scene", Scene::displayName);
                    }
                    case ROLE_SELECT -> {
                        input = new DropdownMenu<>(modal, BasicService.getRoles().toList(),
                                "No role", Role::displayName);
                    }
                    case ACTOR_SELECT -> {
                        input = new DropdownMenu<>(modal, BasicService.getActors().toList(),
                                "No actor", Actor::displayName);
                    }
                    case REHEARSAL_SELECT -> {
                        input = new DropdownMenu<>(modal, BasicService.getRehearsals().toList(),
                                "No rehearsal", Rehearsal::displayName);
                    }
                    default -> throw new IllegalArgumentException("Unknown inputType: " + inputTypes.get(i));
                }
                inputWidgets.add(input);
            }


            /*Label nameLabel = new Label(modal, 0);
            nameLabel.setText("Name:");
            Text nameText = new Text(modal, SWT.SINGLE);
            Label lengthLabel = new Label(modal, 0);
            lengthLabel.setText("Scene length:");
            Text lengthText = new Text(modal, SWT.SINGLE);

            List<Scene> scenes = BasicService.getScenes().toList();

            Label predLabel = new Label(modal, 0);
            predLabel.setText("Predecessor scene:");
            DropdownMenu<Scene> pred = new DropdownMenu<>(modal,
                    scenes, "As first scene", Scene::displayName);*/

            Composite bottomRow = new Composite(modal, 0);
            bottomRow.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
            ((GridData) (bottomRow.getLayoutData())).horizontalSpan = 2;
            bottomRow.setLayout(new RowLayout());
            Button cancel = new Button(bottomRow, SWT.PUSH);
            cancel.setText("Cancel");
            cancel.addListener(SWT.Selection, f -> {
                modal.setVisible(false);
                modal.dispose();
            });

            Button ok = new Button(bottomRow, SWT.PUSH);
            ok.addListener(SWT.Selection, f -> {
                List<Object> inputs = new ArrayList<>();
                for (int i = 0; i < inputTypes.size(); i++) {
                    InputType type = inputTypes.get(i);
                    Object input = inputWidgets.get(i).getInput();
                    if (type == InputType.INT) {
                        try {
                            input = Integer.parseInt((String) input);
                        } catch (NumberFormatException e) {
                            throw new BusinessException(inputNames.get(i) + " needs to be a whole number!", e);
                        }
                    } else if (type == InputType.DOUBLE) {
                        try {
                            input = Double.parseDouble((String) input);
                        } catch (NumberFormatException e) {
                            throw new BusinessException(inputNames.get(i) + " needs to be a number!", e);
                        }
                    }
                    inputs.add(input);
                }
                constructFunction.apply(inputs);
                update.run();
                modal.setVisible(false);
                modal.dispose();


                /*try {
                    String name = nameText.getText();
                    double length = Double.parseDouble(lengthText.getText());
                    double position;
                    Optional<Scene> predScene = pred.getSelected();
                    if (scenes.isEmpty()) {
                        position = 1;
                    } else if (predScene.isPresent()) {
                        if (predScene.get().equals(scenes.getLast())) {
                            position = predScene.get().getPosition() + 1;
                        } else {
                            position = (predScene.get().getPosition()
                                    + scenes.get(scenes.indexOf(predScene.get()) + 1).getPosition())
                                    / 2;
                        }
                    } else {
                        position = scenes.getFirst().getPosition() - 1;
                    }

                    Scene toCreate = BasicService.createScene();
                    toCreate.setName(name);
                    toCreate.setLength(length);
                    toCreate.setPosition(position);
                    BasicService.getScenes().sort(); //TODO Eleganter lösen


                    update.run();
                    modal.setVisible(false);
                    modal.dispose();
                } catch (NumberFormatException ex) {
                    MessageBox errorBox = new MessageBox(modal, SWT.ICON_ERROR);
                    errorBox.setMessage("Length has to be a number.");
                    errorBox.open();
                }*/
            });
            ok.setText("Confirm");

            modal.pack();
            modal.open();

        });
        //mainButton.setText("New scene");
    }

    public void setText(String s) {
        mainButton.setText(s);
    }
}
