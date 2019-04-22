package uk.ac.nottingham.main;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;

/**
 * Provides two static methods to create {@link NumberAxis} and {@link StackedAreaChart} with
 * predefined properties.
 *
 * <p>{@link NumberAxis} - sets the label and disables animation. {@link StackedAreaChart} - sets
 * title, disables legend and symbols, loads css stylesheet.
 */
public class GraphManager {

  private GraphManager() {}

  public static NumberAxis getNewNumberAxis(String label) {
    NumberAxis newAxis = new NumberAxis();
    newAxis.setLabel(label);
    newAxis.setAnimated(false);
    return newAxis;
  }

  public static StackedAreaChart<Number, Number> getNewLineChart(
      NumberAxis x, NumberAxis y, String title) {
    StackedAreaChart<Number, Number> chart = new StackedAreaChart<>(x, y);
    chart.setTitle(title);
    chart.setLegendVisible(false);
    chart.setCreateSymbols(false);
    chart.getStylesheets().add("/uk/ac/nottingham/resources/css/GraphStylesheet.css");

    return chart;
  }
}
