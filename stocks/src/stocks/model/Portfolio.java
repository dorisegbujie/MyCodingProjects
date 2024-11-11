package stocks.model;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Includes methods to create a portfolio and calculate the value of the portfolio.
 * Supports calculating the performance, buying and selling stocks, getting the composition
 * and value distribution, saving to and loading from a file, and rebalancing of a portfolio.
 */
public interface Portfolio {

  /**
   * Calculates the value of the portfolio on the specified date.
   * @param date the specified date to find the value of the portfolio
   * @return the value of the portfolio on the specified date
   */
  public double calcVal(String date) throws IllegalArgumentException;

  /**
   * Gets the values of a portfolio between the specified days.
   * @param startDate date to start calculating portfolio values
   * @param endDate dare to stop calculating portfolio values
   * @return hashmap of dates and corresponding portfolio values
   * @throws ParseException if the date was entered incorrectly
   */
  public Map<String, Integer> portfolioPerformance(String startDate, String endDate)
          throws ParseException;

  /**
   * Allows a user to buy a stock to be added to the portfolio.
   * @param stocks specific stock of interest
   * @param quantity amount of stock
   * @param date to buy the stock on
   * @throws IllegalArgumentException if any fields are entered incorrectly
   */
  void buyStock(String name, Map<String, Stock> stocks, double quantity, String date)
          throws IllegalArgumentException, ParseException;

  /**
   * Allows a user to sell a specified amount of shares of a stock on a specified date.
   * @param stocks the stock to sell
   * @param quantity the amount of the stock to sell
   * @param date the date to sell the stock on
   * @throws IllegalArgumentException if any of the fields are entered incorrectly
   */
  void sellStock(String name, Map<String, Stock> stocks, double quantity, String date)
          throws IllegalArgumentException, ParseException;

  /**
   * Determines the composition of the portfolio on a specific date.
   * @param date the date to find the composition on
   * @return the list of stocks and amount of shares of each
   */
  Map<String, Double> getComposition(String date);

  /**
   * Determines the value of each stock in a portfolio on a specific date.
   * @param date the date to calculate the stock values in a portfolio value on
   * @return the value of each stock
   */
  Map<String, Double> getValueDistribution(String date);

  /**
   * Saves the portfolio to a file.
   * @param filename the specified file to save the portfolio to
   * @throws IOException if file does not exist
   */
  void saveToFile(String filename) throws IOException;

  /**
   * Gets a specified file.
   * @param filename the file to get
   * @throws IOException if file does not exist
   */
  void loadFromFile(String filename) throws IOException;

  /**
   * Balances a portfolio based on the given weights.
   * @param targetDistribution amount to balance by
   * @param date to balance
   * @throws IllegalArgumentException if any inputs are entered incorrectly
   */
  void rebalance(String filename, Map<String, Double> targetDistribution, String date)
          throws IllegalArgumentException, IOException, ParseException;

  /**
   * Returns a list of the stocks in the portfolio.
   * @return a list of the stocks in the portfolio
   */
  List<String> getStock();
}
