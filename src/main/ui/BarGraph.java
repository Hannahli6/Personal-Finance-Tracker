package ui;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;


public class BarGraph extends JPanel{

    private ArrayList<Double> values;
    private ArrayList<String> categories;
    private Color[] colors;

    public BarGraph(ArrayList<Double> values, ArrayList<String> categories, Color[] colors) {
        this.values = values;
        this.categories = categories;
        this.colors = colors;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();
        int padding = 25;
        int labelPadding = 25;
        int barWidth = (width - 2 * padding)/values.size();

        double maxValue = Integer.MIN_VALUE;
        for (double value : values) {
            maxValue = Math.max(maxValue, value);
        }

        double yScale = (height - 2 * padding - labelPadding) / maxValue;

        for (int i = 0; i < values.size(); i++) {
            int barHeight = (int) Math.round( values.get(i) * yScale);
            int x = padding + i * barWidth;
            int y = height - padding - labelPadding - barHeight;

            g2d.setColor(colors[i]);
            g2d.fillRect(x, y, barWidth - 10, barHeight);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawRect(x, y, barWidth - 10, barHeight);

            //x axis labels
            FontMetrics metrics = g2d.getFontMetrics();
            int labelWidth = metrics.stringWidth(categories.get(i));
            g2d.drawString(categories.get(i) +  "\n$" +  values.get(i), x + (barWidth - 10 - labelWidth) / 2, height - padding);
        }
    }
}
