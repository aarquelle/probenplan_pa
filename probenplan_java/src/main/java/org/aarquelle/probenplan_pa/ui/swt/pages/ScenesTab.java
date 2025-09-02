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

package org.aarquelle.probenplan_pa.ui.swt.pages;

import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.entity.Role;
import org.aarquelle.probenplan_pa.entity.Scene;
import org.aarquelle.probenplan_pa.ui.swt.SwtGui;
import org.aarquelle.probenplan_pa.ui.swt.widgets.CustomGroups;
import org.aarquelle.probenplan_pa.ui.swt.widgets.DropdownMenu;
import org.aarquelle.probenplan_pa.ui.swt.widgets.OptionTable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.util.List;
import java.util.Optional;

public class ScenesTab extends Composite {

    OptionTable<Scene, Role> optionTable;

    public ScenesTab(Composite parent) {
        super(parent, SWT.NONE);

        setLayout(new GridLayout(2, false));
        Display d = Display.getCurrent();

        Group importRow = CustomGroups.createImportRow(this, "Import scenes",
                List.of("From Clipboard", "From File"),
                List.of(false, false),
                List.of(() -> System.out.println("Clipboard"), () -> System.out.println("file")));
        ((GridData) (importRow.getLayoutData())).horizontalSpan = 2;
        Group modColumn = new Group(this, 0);
        modColumn.setText("Modify table");
        modColumn.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, true));
        modColumn.setLayout(new RowLayout(SWT.VERTICAL));

        Button addSceneButton = new Button(modColumn, SWT.PUSH);
        addSceneButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Shell modal = new Shell(SwtGui.INSTANCE.getMainShell(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.RESIZE);
                modal.setText("Create new Scene");
                modal.setLayout(new GridLayout(2, true));
                Label nameLabel = new Label(modal, 0);
                nameLabel.setText("Name:");
                Text nameText = new Text(modal, SWT.SINGLE);
                Label lengthLabel = new Label(modal, 0);
                lengthLabel.setText("Scene length:");
                Text lengthText = new Text(modal, SWT.SINGLE);

                List<Scene> scenes = BasicService.getScenes().toList();

                Label predLabel = new Label(modal, 0);
                predLabel.setText("Predecessor scene:");
                DropdownMenu<Scene> pred = new DropdownMenu<>(modal,
                        scenes, "As first scene", Scene::displayName);

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
                    try {
                        String name = nameText.getText();
                        double length = Double.parseDouble(lengthText.getText());
                        double position;
                        Optional<Scene> predScene = pred.getSelected();
                        if (scenes.isEmpty()) {
                            position = 1;
                        }
                        else if (predScene.isPresent()) {
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


                        updateData();
                        modal.setVisible(false);
                        modal.dispose();
                    } catch (NumberFormatException ex) {
                        MessageBox errorBox = new MessageBox(modal, SWT.ICON_ERROR);
                        errorBox.setMessage("Length has to be a number.");
                        errorBox.open();
                    }
                });
                ok.setText("Confirm");

                modal.pack();
                modal.open();
            }
        });
        addSceneButton.setText("New scene");

        optionTable = new OptionTable<>(this,
                BasicService.getRoles(),
                BasicService.getScenes(),
                false,
                List.of("Kommt nicht vor.", "Kleine Rolle", "Große Rolle"),
                null, d.getSystemColor(SWT.COLOR_YELLOW), d.getSystemColor(SWT.COLOR_GREEN));


    }

    public void updateData() {
        optionTable.updateData();
    }
}
