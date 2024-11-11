package stocks.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Includes methods to be performed on stocks to learn about the stocks.
 */
public class StockModelImpl implements StockModel {

  /**
   * Creates an instance to use the methods on various stocks.
   */
  public StockModelImpl() {
    // creates instance to use methods.
  }

  @Override
  public double gainOrLoss(Map<String, Stock> stock, String startDate, String endDate)
          throws IllegalArgumentException {

    if (!stock.containsKey(startDate) || !stock.containsKey(endDate)) {
      throw new IllegalArgumentException("Date does not exist");
    }

    String[] keyArray = sortKeys(stock);
    int start = findIndex(keyArray, startDate);
    int end = findIndex(keyArray, endDate);

    if (start > end) {
      throw new IllegalArgumentException("Start date cannot be after end date");
    }
    double startPrice = stock.get(startDate).getPriceAtClose();
    double endPrice = stock.get(endDate).getPriceAtClose();
    return endPrice - startPrice;
  }

  @Override
  public double xDayMovingAverage(Map<String, Stock> stock, String date, int x)
          throws IllegalArgumentException {

    if (!stock.containsKey(date)) {
      throw new IllegalArgumentException("Date does not exist");
    }
    if (x < 0) {
      throw new IllegalArgumentException("X value cannot be negative");
    }

    String[] keyArray = sortKeys(stock);

    int startI = findIndex(keyArray, date);

    double[] values = sortValues(stock, keyArray);

    int i = 0;
    double sum = 0;
    while (i < x) {
      if (startI >= values.length || startI < 0) {
        x = i;
        break;
      }
      sum += values[startI];
      startI -= 1;
      i += 1;
    }
    return sum / x;
  }

  @Override
  public ArrayList<String> crossovers(Map<String, Stock> stocks, String startDate,
                                      String endDate, int x) throws IllegalArgumentException {

    if (!stocks.containsKey(startDate) || !stocks.containsKey(endDate)) {
      throw new IllegalArgumentException("Date does not exist");
    }

    String[] keyArray = sortKeys(stocks);

    int start = findIndex(keyArray, startDate);
    int end = findIndex(keyArray, endDate);

    if (start > end) {
      throw new IllegalArgumentException("Start date cannot be after end date");
    }

    int startI = findIndex(keyArray, startDate);

    int endI = findIndex(keyArray, endDate);

    double[] values = sortValues(stocks, keyArray);

    Map<String, Double>  tempStock = new HashMap<String, Double>();
    int i = 0;
    for (String k : keyArray) {
      tempStock.put(k, values[i]);
      i++;
    }

    ArrayList<String> days = new ArrayList<String>();

    while (startI <= endI) {
      double priceAtClose = tempStock.get(keyArray[startI]);
      double movingAvg = xDayMovingAverage(stocks, keyArray[startI], x);
      if (priceAtClose > movingAvg) {
        days.add(keyArray[startI]);
      }
      startI += 1;
    }

    return days;
  }

  /**
   * Finds the index of the selected value in the array.
   * @param arr the array to find the value in
   * @param t the value that the index is needed for
   * @return integer representing index of value
   */
  private int findIndex(String[] arr, String t) {

    int index = Arrays.binarySearch(arr, t);
    return (index < 0) ? -1 : index;
  }

  /**
   * Sorts the keys of a hashmap.
   * @param stock hashmap containing dates as keys and stock as value
   * @return
   */
  private String[] sortKeys(Map<String, Stock> stock) {
    String[] keyArray = stock.keySet().toArray(new String[stock.size()]);
    Arrays.sort(keyArray);
    return keyArray;
  }

  /**
   * Sorts the values in the array by the sorted keys.
   * @param stock hashmap containing dates and stocks
   * @param keyArray the keys in sorted order
   * @return sorted values from the hashmap
   */
  private double[] sortValues(Map<String, Stock> stock, String[] keyArray) {
    double[] values = new double[keyArray.length];
    int j = 0;
    for (String s : keyArray) {
      values[j] = stock.get(s).getPriceAtClose();
      j += 1;
    }
    return values;
  }

}
