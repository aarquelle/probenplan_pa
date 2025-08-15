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

import org.aarquelle.probenplan_pa.entity.Entity;
import org.aarquelle.probenplan_pa.ui.API;
import org.aarquelle.probenplan_pa.util.SortedUniqueList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OptionTable<ROW extends Entity & Comparable<ROW>, COL extends Entity & Comparable<COL>> extends ScrolledComposite {

    int columnWidth = 100;
    int rowHeight = 30;

    Point virtualSize;

    private final SortedUniqueList<COL> syncedColEntities;
    private final SortedUniqueList<ROW> syncedRowEntities;

    /*
      These lists are used for easy access by index, they do not remain synced to the data state.
     */
    private List<COL> colEntities;
    private List<ROW> rowEntities;

    private final String[] tooltips;
    private final Color[] colors;

    private final List<List<TableCell>> tableCells = new ArrayList<>();

    public OptionTable(Composite parent, SortedUniqueList<COL> syncedColEntities,
                       SortedUniqueList<ROW> syncedRowEntities,
                       List<String> tooltips, Color... colors) {
        super(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);

        this.syncedColEntities = syncedColEntities;
        this.syncedRowEntities = syncedRowEntities;
        this.tooltips = tooltips.toArray(new String[]{});
        this.colors = colors;

        updateData();

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        setLayoutData(gridData);

        setExpandHorizontal(true);
        setExpandVertical(true);



        Canvas canvas = new Canvas(this, SWT.NONE);

        canvas.addPaintListener(new PaintListener() {
            private void drawString(GC gc, String s, int x, int y) {
                Point extent = gc.stringExtent(s);
                int yOffset = rowHeight / 2 - extent.y / 2;

                if (extent.x + 10 > columnWidth * 0.9) {
                    while (gc.stringExtent(s + '…').x + 10 > columnWidth * 0.9) {
                        s = s.substring(0, s.length() - 1);
                    }
                    s += "…";
                }
                gc.drawString(s, x * columnWidth + 10, y * rowHeight + yOffset);
            }

            @Override
            public void paintControl(PaintEvent e) {
                GC gc = e.gc;


                for (int i = 0; i < colEntities.size(); i++) {
                    drawString(gc, colEntities.get(i).displayName(), i + 1, 0);
                }

                for (int i = 0; i < rowEntities.size(); i++) {
                    drawString(gc, rowEntities.get(i).displayName(), 0, i + 1);
                }

                for (int i = 1; i < rowEntities.size() + 1; i++) {
                    for (int j = 1; j < colEntities.size() + 1; j++) {
                        Color fillColor = getCell(j, i).getColor();
                        if (fillColor != null) {
                            gc.setBackground(fillColor);
                            gc.fillRectangle(j * columnWidth, i * rowHeight, columnWidth, rowHeight);
                        }
                    }
                }

                for (int i = 1; i < colEntities.size() + 2; i++) {
                    gc.drawLine(i * columnWidth, 0, i * columnWidth, virtualSize.y);
                }

                for (int i = 1; i < rowEntities.size() + 2; i++) {
                    gc.drawLine(0, i * rowHeight, virtualSize.x, i * rowHeight);
                }
            }
        });

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                super.mouseDown(e);
                getCell(e).ifPresent(f -> {
                    f.click();
                    canvas.redraw();
                });
            }
        });

        canvas.addMouseMoveListener(e -> getCell(e).ifPresent(f -> canvas.setToolTipText(f.getTooltip())));

        setContent(canvas);
        setMinSize(virtualSize);

        addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(ControlEvent e) {
                Rectangle scSize = getClientArea();

                int width = Math.max(virtualSize.x, scSize.width);
                int height = Math.max(virtualSize.y, scSize.height);

                canvas.setSize(width, height);
            }
        });
    }

    private Optional<TableCell> getCell(MouseEvent e) {
        int x = e.x / columnWidth;
        int y = e.y / rowHeight;
        if (x >= 0 && y >= 0 && x <= syncedColEntities.size() && y <= syncedRowEntities.size()) {
            return Optional.of(getCell(x, y));
        } else {
            return Optional.empty();
        }
    }

    private TableCell getCell(int x, int y) {
        return tableCells.get(y).get(x);
    }

    public void updateData() {
        tableCells.clear();

        rowEntities = syncedRowEntities.stream().toList();
        colEntities = syncedColEntities.stream().toList();

        for (int i = 0; i < rowEntities.size() + 1; i++) {
            List<TableCell> row = new ArrayList<>(colEntities.size());
            tableCells.add(row);
            Entity rowEntity = null;
            if (i > 0) {
                rowEntity = rowEntities.get(i - 1);
            }
            for (int j = 0; j < colEntities.size() + 1; j++) {
                if (j > 0 && i > 0) {
                    Entity colEntity = colEntities.get(j - 1);
                    String common = rowEntity.displayName() + "\n" + colEntity.displayName() + "\n";
                    String[] combinedTooltips = new String[tooltips.length];
                    for (int k = 0; k < tooltips.length; k++) {
                        combinedTooltips[k] = common + tooltips[k];
                    }
                    TableCell cell = new TableCell(rowEntity, colEntity, combinedTooltips, colors);
                    cell.setState(API.getRelation(rowEntity, colEntity));
                    row.add(cell);
                } else {
                    row.add(new TableCell(null, null, null, (Color) null));
                }
            }
        }
        virtualSize = new Point(columnWidth * (syncedColEntities.size() + 1), rowHeight * (syncedRowEntities.size() + 1));
        setMinSize(virtualSize);
    }
}
