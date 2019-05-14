package uk.ac.nottingham.managers;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;

/**
 * Provides two static methods to create {@link NumberAxis} and {@link StackedAreaChart} with
 * predefined properties.
 */
public class GraphManager {

  /** Private constructor, so that this class can't be instantiated. */
  private GraphManager() {}

  /**
   * Create new {@link NumberAxis}. Sets the label and disables the animation.
   *
   * @param label text for the label
   * @return newly created {@link NumberAxis}
   */
  public static NumberAxis getNewNumberAxis(String label) {
    NumberAxis newAxis = new NumberAxis();
    newAxis.setLabel(label);
    newAxis.setAnimated(false);
    return newAxis;
  }

  /**
   * Create new {@link NumberAxis}. Sets label, disables animation and auto ranging. Upper bound set
   * to 25, lower bound set to 25 and tick units to 5.
   *
   * @param label text for the label
   * @return newly created {@link NumberAxis}
   */
  public static NumberAxis getTimeXAxis(String label) {
    NumberAxis newAxis = getNewNumberAxis(label);
    newAxis.setAutoRanging(false);
    newAxis.setLowerBound(0);
    newAxis.setUpperBound(50);
    newAxis.setTickUnit(10);
    return newAxis;
  }

  /**
   * Create new {@link StackedAreaChart}. Sets the title, disables legend and line symbols. Also
   * loads a css stylesheet to set the look.
   *
   * @param x {@link NumberAxis} X
   * @param y {@link NumberAxis} Y
   * @param title text to set as the title
   * @return newly created {@link StackedAreaChart}
   */
  public static StackedAreaChart<Number, Number> getChart(
      NumberAxis x, NumberAxis y, String title) {
    StackedAreaChart<Number, Number> chart = new StackedAreaChart<>(x, y);
    chart.setTitle(title);
    chart.setLegendVisible(false);
    chart.setCreateSymbols(false);
    chart.getStylesheets().add("/uk/ac/nottingham/css/GraphStylesheet.css");

    return chart;
  }
}
