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

package org.aarquelle.probenplan_pa.ui.swt.widgets.option_tables;

import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.entity.Entity;
import org.aarquelle.probenplan_pa.util.SortedUniqueList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class OptionTable<ROW extends Entity & Comparable<ROW>, COL extends Entity & Comparable<COL>>
        extends ScrolledComposite {

    private long staleness;

    int columnWidth = 100;
    int rowHeight = 30;

    Point virtualSize;

    private final SortedUniqueList<COL> syncedColEntities;
    private final SortedUniqueList<ROW> syncedRowEntities;

    /*
      These lists are used for easy access by index, they do not remain synced to the data state.
     */
    protected List<COL> colEntities;
    protected List<ROW> rowEntities;

    protected final String[] tooltips;
    protected final Color[] colors;

    protected final int numberOfMarginRows;
    protected final int numberOfMarginCols;

    private final List<List<TableCell<COL, ROW>>> tableCells = new ArrayList<>();

    private final Canvas canvas;

    public OptionTable(Composite parent, SortedUniqueList<COL> syncedColEntities,
                       SortedUniqueList<ROW> syncedRowEntities,
                       List<String> tooltips, int numberOfMarginRows, int numberOfMarginCols, Color... colors) {
        super(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);

        this.syncedColEntities = syncedColEntities;
        this.syncedRowEntities = syncedRowEntities;
        this.tooltips = tooltips.toArray(new String[]{});
        this.numberOfMarginRows = numberOfMarginRows;
        this.numberOfMarginCols = numberOfMarginCols;
        this.colors = colors;

        updateData();

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        setLayoutData(gridData);

        setExpandHorizontal(true);
        setExpandVertical(true);


        canvas = new Canvas(this, SWT.NONE);

        canvas.addPaintListener(e -> {
            updateData();

            GC gc = e.gc;

            for (int i = 0; i < rowEntities.size() + numberOfMarginRows; i++) {
                for (int j = 0; j < colEntities.size() + numberOfMarginCols; j++) {
                    if (i >= numberOfMarginRows && j >= numberOfMarginCols) {
                        drawContent(getCell(j, i), gc);
                    } else {
                        drawMargin(getCell(j, i), gc);
                    }
                }
            }
            gc.setLineWidth(1);
            for (int i = 1; i < colEntities.size() + 2; i++) {
                gc.drawLine(i * columnWidth, 0, i * columnWidth, virtualSize.y);
            }

            for (int i = 1; i < rowEntities.size() + 2; i++) {
                gc.drawLine(0, i * rowHeight, virtualSize.x, i * rowHeight);
            }
        });

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                updateData();
                super.mouseDown(e);
                getCell(e).ifPresent(f -> {
                    f.click();
                    canvas.redraw();
                });
            }
        });

        canvas.addMouseMoveListener(e -> {
            updateData();
            getCell(e).ifPresent(f -> canvas.setToolTipText(f.getTooltip()));
        });

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

    /**
     * Draw a non-content cell on the margins.
     */
    protected abstract void drawMargin(TableCell<COL, ROW> cell, GC gc);

    /**
     * Draw something in a content cell;
     */
    protected abstract void drawContent(TableCell<COL, ROW> cell, GC gc);

    /**
     * The tooltip consists of the display names of col and row entity, the result of this method
     * and a state-dependent String saved in the TableCell.
     */
    protected abstract String additionalCommonTooltip(COL colEntity, ROW rowEntity);

    /**
     * Set the initial state of a content cell.
     */
    protected abstract void setInitialState(TableCell<COL, ROW> cell);

    /**
     * Return the entity associated with a specific row.
     *
     * @param row The number of the row in the table. The count includes non-entity rows, like title rows.
     */
    protected ROW getRowEntity(int row) {
        return rowEntities.get(row - numberOfMarginRows);
    }

    /**
     * Return the entity associated with a specific column.
     *
     * @param col The number of the column in the table. The count includes non-entity column, like title columns.
     */
    protected COL getColEntity(int col) {
        return colEntities.get(col - numberOfMarginCols);
    }

    /**
     * Returns a possible array of tooltips for content cells.
     * There is no requirement for implementations to actually use this method.
     */
    protected String[] getTooltips(COL col, ROW row) {
        String common = col.displayName() + "\n" + row.displayName() + "\n"
                + additionalCommonTooltip(col, row);
        String[] result = new String[tooltips.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = common + tooltips[i];
        }
        return result;
    }

    /**
     * Create a new TableCell.
     */
    protected abstract TableCell<COL, ROW> createContentCell(int col, int row);

    protected abstract TableCell<COL, ROW> createMarginCell(int col, int row);

    protected void fillBackground(int col, int row, Color color, GC gc) {
        if (color != null) {
            gc.setBackground(color);
            gc.fillRectangle(col * columnWidth, row * rowHeight, columnWidth, rowHeight);
        }
    }

    protected void fillBackground(TableCell<COL, ROW> cell, Color color, GC gc) {
        fillBackground(cell.col, cell.row, color, gc);
    }

    protected void drawString(String s, TableCell<COL, ROW> cell, GC gc) {
        int x = cell.col;
        int y = cell.row;
        Point extent = gc.stringExtent(s);
        int yOffset = rowHeight / 2 - extent.y / 2;

        gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

        if (extent.x + 10 > columnWidth * 0.9) {
            while (gc.stringExtent(s + '…').x + 10 > columnWidth * 0.9) {
                s = s.substring(0, s.length() - 1);
            }
            s += "…";
        }
        gc.drawString(s, x * columnWidth + 10, y * rowHeight + yOffset);
    }

    protected void markCell(int col, int row, GC gc) {
        gc.setLineWidth(2);
        gc.drawRectangle(col * columnWidth, row * rowHeight, columnWidth, rowHeight);
        gc.drawLine(col * columnWidth, row * rowHeight,
                (col + 1) * columnWidth, (row + 1) * rowHeight);
        gc.drawLine((col + 1) * columnWidth, row * rowHeight,
                col * columnWidth, (row + 1) * rowHeight);
    }

    protected void heavyMarkCell(int col, int row, GC gc) {
        markCell(col, row, gc);
        gc.drawLine((int)((col + 0.5) * columnWidth), row * rowHeight,
                (int)((col + 0.5) * columnWidth),
                (row + 1) * rowHeight);
        gc.drawLine(col * columnWidth, (int)((row + 0.5) * rowHeight),
                (col + 1) * columnWidth, (int)((row + 0.5) * rowHeight));
    }

    protected Optional<TableCell<COL, ROW>> getCell(MouseEvent e) {
        int x = e.x / columnWidth;
        int y = e.y / rowHeight;
        if (x >= 0 && y >= 0 && x <= syncedColEntities.size() && y <= syncedRowEntities.size()) {
            return Optional.of(getCell(x, y));
        } else {
            return Optional.empty();
        }
    }

    protected TableCell<COL, ROW> getCell(int x, int y) {
        return tableCells.get(y).get(x);
    }

    public void updateData() {
        if (BasicService.getFreshness() != staleness) {
            tableCells.clear();

            rowEntities = syncedRowEntities.stream().toList();
            colEntities = syncedColEntities.stream().toList();

            for (int i = 0; i < rowEntities.size() + numberOfMarginRows; i++) {
                List<TableCell<COL, ROW>> row = new ArrayList<>(colEntities.size());
                tableCells.add(row);
                for (int j = 0; j < colEntities.size() + numberOfMarginCols; j++) {
                    if (j >= numberOfMarginCols && i >= numberOfMarginRows) {
                        row.add(createContentCell(j, i));
                        setInitialState(getCell(j, i));
                    } else {
                        row.add(createMarginCell(j, i));
                    }
                }
            }
            virtualSize = new Point(columnWidth * (syncedColEntities.size() + 1), rowHeight * (syncedRowEntities.size() + 1));
            setMinSize(virtualSize);
            /*if (canvas != null) {
                canvas.redraw();
                canvas.update();
            }*/
            staleness = BasicService.getFreshness();
        }
    }

    @Override
    public void redraw() {
        super.redraw();
        canvas.redraw();
    }

    @Override
    public void update() {
        super.update();
        canvas.update();
    }
}
