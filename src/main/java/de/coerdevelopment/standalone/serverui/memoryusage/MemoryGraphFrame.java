package de.coerdevelopment.standalone.serverui.memoryusage;

import com.github.lgooddatepicker.components.DateTimePicker;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MemoryGraphFrame {

    private Map<Long, Integer> memoryUsage;

    private DateTimePicker startTimePicker;
    private DateTimePicker endTimePicker;

    private ChartPanel chartPanel;
    private TimeSeries series;
    private JFrame frame;

    public MemoryGraphFrame(Map<Long, Integer> memoryUsage) {
        this.memoryUsage = memoryUsage;

        startTimePicker = new DateTimePicker();
        startTimePicker.setDateTimePermissive(LocalDateTime.now().minusMinutes(10));
        endTimePicker = new DateTimePicker();
        endTimePicker.setDateTimePermissive(LocalDateTime.now().plusDays(1));
    }


    public JFrame createMemoryGraphFrame() {
        frame = new JFrame();

        frame.setMinimumSize(new Dimension(750, 550));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("Memory Usage");

        chartPanel = getChartPanel(getDateByTimePicker(startTimePicker).getTime(), getDateByTimePicker(endTimePicker).getTime());

        if (chartPanel == null) {
            return null;
        }

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout());

        JButton refreshButton = new JButton("refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshSeries();
            }
        });

        headerPanel.add(new JLabel("Start time:"));
        headerPanel.add(startTimePicker);
        headerPanel.add(new JLabel("End Time"));
        headerPanel.add(endTimePicker);
        headerPanel.add(refreshButton);

        frame.getContentPane().add(headerPanel, BorderLayout.NORTH);
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);

        return frame;
    }

    public ChartPanel getChartPanel(long startTime, long endTime) {

        series = new TimeSeries("Memory (MB)");

        TimeSeriesCollection dataset = new TimeSeriesCollection();

        dataset.addSeries(series);

        DateAxis dateAxis = new DateAxis("Time");
        NumberAxis numberAxis = new NumberAxis("Memory (MB)");

        XYLineAndShapeRenderer xYLineAndShapeRenderer = new XYLineAndShapeRenderer(true, false);
        xYLineAndShapeRenderer.setSeriesPaint(0, Color.RED);
        xYLineAndShapeRenderer.setSeriesStroke(0, new BasicStroke(3.0F, 0, 2));

        XYPlot plot = new XYPlot((XYDataset) dataset, (ValueAxis) dateAxis, (ValueAxis) numberAxis, (XYItemRenderer) xYLineAndShapeRenderer);

        JFreeChart chart = new JFreeChart("Memory Usage", new Font("SansSerif", 1, 24), (Plot)plot, true);
        ChartUtils.applyCurrentTheme(chart);

        ChartPanel chartPanel = new ChartPanel(chart);

        return chartPanel;
    }

    private Map<Long, Integer> getMemoryUsagebyTimeframe(long startTime, long endTime) {
        if (memoryUsage == null || memoryUsage.isEmpty()) {
            return null;
        }

        Map<Long, Integer> memoryUsageByTime = new HashMap<>();

        for (Map.Entry<Long, Integer> pair : memoryUsage.entrySet()) {
            if (pair.getKey() >= startTime && pair.getKey() <= endTime) {
                memoryUsageByTime.put(pair.getKey(), pair.getValue());
            }
        }

        return memoryUsageByTime;
    }

    public void refreshSeries() {
        Map<Long, Integer> memoryUsageByTime = getMemoryUsagebyTimeframe(getDateByTimePicker(startTimePicker).getTime(), getDateByTimePicker(endTimePicker).getTime());

        series.clear();

        if (memoryUsageByTime == null || memoryUsageByTime.isEmpty()) {
            return;
        }

        for (Map.Entry<Long, Integer> pair : memoryUsageByTime.entrySet()) {
            series.add((RegularTimePeriod) new Millisecond(new Date(pair.getKey())), pair.getValue());
        }

        series.fireSeriesChanged();
    }

    private Date getDateByTimePicker(DateTimePicker dateTimePicker) {
        int month = dateTimePicker.getDatePicker().getDate().getMonth().getValue() - 1;
        int year = dateTimePicker.getDatePicker().getDate().getYear() - 1900;
        int day = dateTimePicker.getDatePicker().getDate().getDayOfMonth();
        int hour = dateTimePicker.getTimePicker().getTime().getHour();
        int minutes = dateTimePicker.getTimePicker().getTime().getMinute();
        Date javaDate = new java.util.Date(year, month, day, hour, minutes);

        return javaDate;
    }

}
