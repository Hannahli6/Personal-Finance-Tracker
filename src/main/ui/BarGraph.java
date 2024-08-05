package ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


// The BarGraph class extends JPanel and overrides the paintComponent method to draw the bars.
// This is used for drawing bar graph to show the expense amount fallen under each category
public class BarGraph extends JPanel {

    private ArrayList<Double> values;
    private ArrayList<String> categories;
    private Color[] colors;

    //REQUIRES: no empty colours, no empty values, no empty categories
    // EFFECTS: initialize the Bar graph to have colours, categories as x labels and values
    // for the proportion of bar height
    public BarGraph(ArrayList<Double> values, ArrayList<String> categories, Color[] colors) {
        this.values = values;
        this.categories = categories;
        this.colors = colors;
    }

    // EFFECTS: setting the bar graph canvas to calculate the height of the bars
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();
        int padding = 25;
        int labelPadding = 25;
        int barWidth = (width - 2 * padding) / values.size();

        double maxValue = Integer.MIN_VALUE;
        for (double value : values) {
            maxValue = Math.max(maxValue, value);
        }
        double yscale = (height - 2 * padding - labelPadding) / maxValue;
        renderEachBar(g2d, height, padding, labelPadding, barWidth, yscale);
    }

    //EFFECTS: draw the graphics for each bar and print the labels for each category
    private void renderEachBar(Graphics2D g2d, int height, int padding, int labelPadding, int barWidth, double yscale) {
        for (int i = 0; i < values.size(); i++) {
            int barHeight = (int) Math.round(values.get(i) * yscale);
            int x = padding + i * barWidth;
            int y = height - padding - labelPadding - barHeight;

            g2d.setColor(colors[i]);
            g2d.fillRect(x, y, barWidth - 10, barHeight);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawRect(x, y, barWidth - 10, barHeight);

            // x axis labels
            FontMetrics metrics = g2d.getFontMetrics();
            int labelWidth = metrics.stringWidth(categories.get(i));
            g2d.drawString(categories.get(i) + "\n$" + values.get(i), x + (barWidth - 10 - labelWidth) / 2,
                    height - padding);
        }
    }
}
