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

import org.aarquelle.probenplan_pa.business.Para;
import org.aarquelle.probenplan_pa.business.Params;
import org.aarquelle.probenplan_pa.ui.swt.widgets.input.Input;
import org.aarquelle.probenplan_pa.ui.swt.widgets.input.InputType;
import org.aarquelle.probenplan_pa.ui.swt.widgets.input.InputWidget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import java.util.ArrayList;
import java.util.List;

public class ParamTab extends Composite {

    private final List<InputWidget<String>> inputs;
    private final Composite contentComp;

    public ParamTab(Composite parent) {
        super(parent, 0);
        setLayout(new GridLayout(1, true));
        Group saveGroup = new Group(this, 0);
        saveGroup.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        saveGroup.setLayout(new RowLayout());
        Button saveButton = new Button(saveGroup, SWT.PUSH);
        saveButton.setText("Save changes");
        Button cancelButton = new Button(saveGroup, SWT.PUSH);
        cancelButton.setText("Cancel changes");
        Button resetButton = new Button(saveGroup, 0);
        resetButton.setText("Reset to default values");
        contentComp = new Composite(this, 0);
        contentComp.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        contentComp.setLayout(new GridLayout(4, true));

        inputs = new ArrayList<>();

        for (Para<?> para : Params.getAllParams()) {
            Input<String> i = new Input<>(para.getName(), InputType.STRING, String.valueOf(para.getValue()));
            InputWidget<String> widget = new InputWidget<>(contentComp, i);
            widget.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
            widget.setToolTipText(para.getDescription());
            inputs.add(widget);
        }

        saveButton.addListener(SWT.Selection, e -> {
            for (InputWidget<String> widget : inputs) {
                Params.setPara(widget.getName(), widget.getString());
            }
        });
        cancelButton.addListener(SWT.Selection, e -> resetInputs());
        resetButton.addListener(SWT.Selection, e -> {
            Params.reset();
            resetInputs();
        });

    }

    public void resetInputs() {
        for (InputWidget<String> widget : inputs) {
            widget.setText(Params.getValueFromString(widget.getName()));
        }
        contentComp.redraw();
    }
}
