//JESZCZE NIE DZIAŁA

package pl.edu.agh.iet.gui.gantt;

import java.awt.Color;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.gantt.GanttCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import pl.edu.agh.iet.bo.fa.FireflySolution;

public class GanttCharts extends ApplicationFrame {

	private static final long serialVersionUID = 1L;

	public GanttCharts(String title, GanttCategoryDataset dataset) {
		super(title);
		ChartPanel chartPanel = new ChartPanel(createChart(dataset));
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		chartPanel.setMouseZoomable(true, false);
		setContentPane(chartPanel);
		pack();
		setVisible(true);
	}

	private static JFreeChart createChart(GanttCategoryDataset dataset) {

		JFreeChart chart = ChartFactory.createGanttChart("Example", "Machines",
				"Date", dataset, true, true, false);

		chart.setBackgroundPaint(Color.white);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);

		return chart;

	}

	private static GanttCategoryDataset createDataset(int[] order, int[][] times, int[][] endTimes) {
		TaskSeriesCollection dataset = new TaskSeriesCollection();
		TaskSeries[] tasks = new TaskSeries[order.length];
		Task[][] machines = new Task[order.length][endTimes.length];
		long begin, end;

		for (int i = 0, n = order.length; i < n; ++i) {
			tasks[i] = new TaskSeries("Task " + (i + 1));

			for (int j = 1, m = endTimes.length; j < m; ++j) {
				end = endTimes[j][i];
				begin = end - times[j][i];
						
				machines[i][j] = new Task("Machine " + (j + 1), 
						new SimpleTimePeriod(new Date(begin), new Date(end)));
			}

			dataset.add(tasks[i]);
		}

		return dataset;
	}

	public static void main(String[] args) {
		long time = System.nanoTime();
		FireflySolution.main(new String[0]);
		time = System.nanoTime() - time;
		GanttCharts chart = new GanttCharts("GanttChart: " + time,
				createDataset(FireflySolution.bestPermutation,
						FireflySolution.times, FireflySolution.endTimes));
		RefineryUtilities.centerFrameOnScreen(chart);

	}

}
