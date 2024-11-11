package stocks.model;

import java.util.ArrayList;
import java.util.Map;

/**
 * Includes methods to be applied to stocks to learn abut the stocks.
 * Supports calculating gain or loss of stock, calculating x-day average, and
 * finding crossover days.
 */
public interface StockModel {

  /**
   * Calculates the gain or loss of the stock between specified dates.
   * @param stock the selected stock
   * @param startDate the start date
   * @param endDate the end date
   * @return the gain or loss of the stock between the specified dates
   */
  public double gainOrLoss(Map<String, Stock> stock, String startDate, String endDate);

  /**
   * Calculates the x-day moving average from a specified date for a chosen stock.
   * @param stock the selected stock
   * @param date the selected start date
   * @param x the amount of days from the selected date to calculate
   * @return the x-day moving average
   */
  public double xDayMovingAverage(Map<String, Stock> stock, String date, int x);

  /**
   * Finds the days between the start and end date that are x day crossovers.
   * @param stock the stock to be analyzed
   * @param startDate the start date
   * @param endDate the end date
   * @param x the value to be used to calculate x-day crossovers
   * @return the days that are x-day crossovers
   */
  public ArrayList<String> crossovers(Map<String, Stock> stock, String startDate,
                                      String endDate, int x);
}
