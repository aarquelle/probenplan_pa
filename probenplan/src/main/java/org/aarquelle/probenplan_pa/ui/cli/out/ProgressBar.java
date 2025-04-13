package org.aarquelle.probenplan_pa.ui.cli.out;


public class ProgressBar extends Out {
    int width = getTerminalWidth() - 10;
    int max;
    int currentProgress = 0;
    public ProgressBar(int max) {
        this.max = max;
        line("");
        pr("["+" ".repeat(Math.max(0, width)) + "]\u001b[2G");
    }

    public void update(int value) {
        int progress = (int)((double)value / max * width);
        if (progress > currentProgress) {
            currentProgress = progress;
            pr("=");
        }
    }

    public void finish() {
        pr("=]\n");
        try {
            System.out.flush();
        } catch (Exception e) {
            // Handle exception if needed
        }
    }
}
