package stocks.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;

/**
 * Represents a portfolio, containing multiple stocks with amounts of each stock.
 * Supports buying and selling stocks, calculating portfolio value, getting the performance,
 * saving to and loading from a file, getting the compositionq and value distribution
 * and rebalancing a portfolio.
 */
public class PortfolioImpl implements Portfolio {
  private Map<Map<String, Stock>, Double> quant;

  /**
   * Creates an instance of the portfolio, starting with no stocks.
   */
  public PortfolioImpl() {
    this.quant = new HashMap<>();
  }

  /**
   * Adds a stock to the portfolio.
   *
   * @param stocks   the stock to be added
   * @param quantity the amount of the stock to be added
   * @throws IllegalArgumentException if the amount added is less than 0
   */
  private void addStock(Map<String, Stock> stocks, int quantity)
          throws IllegalArgumentException {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity cannot be negative.");
    }
    quant.put(stocks, (double) quantity);
  }

  @Override
  public double calcVal(String date) throws IllegalArgumentException {
    double valOfStock = 0;
    int count = 0;
    for (Map.Entry<Map<String, Stock>, Double> entry : quant.entrySet()) {
      Map<String, Stock> oneStock = entry.getKey();
      if (oneStock.containsKey(date)) {
        Stock stock = oneStock.get(date);
        double currVal = stock.getPriceAtClose();
        valOfStock += currVal * entry.getValue();
        count += 1;
      }
    }
    if (count == 0) {
      throw new IllegalArgumentException("Date does not exist.");
    }
    return valOfStock;
  }

  @Override
  public Map<String, Integer> portfolioPerformance(String startDate, String endDate)
          throws ParseException {
    String timeStamp = new SimpleDateFormat("yyyy-MM-dd")
            .format(Calendar.getInstance().getTime());

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    if (startDate.length() == 7) {
      startDate = startDate + "-01";
    } else if (startDate.length() == 4) {
      startDate = startDate + "-01-01";
    } else if (startDate.length() != 10 && endDate.length() != 9) {
      throw new IllegalArgumentException("Incorrect start date date format.");
    }
    if (endDate.length() == 7) {
      LocalDate localDate = LocalDate.of(Integer.parseInt(endDate.substring(0, 4)),
              Integer.parseInt(endDate.substring(5, 7)), 12);
      int length = localDate.lengthOfMonth();
      endDate = endDate + "-" + Integer.toString(length);
    } else if (endDate.length() == 4) {
      endDate = endDate + "-12-31";
    } else if (endDate.length() != 10 && endDate.length() != 9) {
      throw new IllegalArgumentException("Incorrect end date date format.");
    }

    Date current;
    try {
      current = sdf.parse(startDate);
    } catch (ParseException e) {
      throw new IllegalArgumentException("Incorrect date");
    }

    Date end;
    try {
      end = sdf.parse(endDate);
    } catch (ParseException e) {
      throw new IllegalArgumentException("Incorrect date");
    }

    if (sdf.parse(timeStamp).before(current) || sdf.parse(timeStamp).before(end)) {
      throw new IllegalArgumentException("Date does not exist");
    }
    if (end.before(current)) {
      throw new IllegalArgumentException("Start date cannot be after end date.");
    }

    Map<String, Integer> portfolioVal = portfolioValues(current, endDate, sdf);

    startDate = checkForDate(portfolioVal, startDate, sdf, 1);
    endDate = checkForDate(portfolioVal, endDate, sdf, -1);

    int endDateNum = getDateNum(portfolioVal, endDate);
    int startDateNum = getDateNum(portfolioVal, startDate);

    portfolioVal = removeVals(portfolioVal);

    portfolioVal = addStartEnd(portfolioVal, startDate, startDateNum);
    portfolioVal = addStartEnd(portfolioVal, endDate, endDateNum);

    int divide = commonFactor(portfolioVal);

    for (Map.Entry<String, Integer> entry : portfolioVal.entrySet()) {
      int num = portfolioVal.get(entry.getKey()) / divide;
      portfolioVal.replace(entry.getKey(), num);
    }

    portfolioVal.put("Scale", divide);

    return portfolioVal;
  }

  /**
   * Removes the element at the given index from the array.
   *
   * @param index to remove element at
   * @param array to remove element from
   * @return updated array
   */
  private String[] removeAtIndex(int index, String[] array) {
    if (array == null || index < 0 || index >= array.length) {
      return array;
    }

    String[] anotherArr = new String[array.length - 1];

    for (int i = 0, k = 0; i < array.length; i++) {
      if (i == index) {
        continue;
      }
      anotherArr[k++] = array[i];
    }
    return anotherArr;
  }

  /**
   * Calculates the portfolio values between the chosen dates.
   *
   * @param current starting date
   * @param endDate ending date
   * @param sdf     date format
   * @return portfolio values for each date between the chosen dates
   * @throws ParseException if unable to create a date from the string
   */
  private Map<String, Integer> portfolioValues(Date current, String endDate,
                                               SimpleDateFormat sdf) throws ParseException {
    Map<String, Integer> portfolioVal = new HashMap<>();
    Calendar ca = Calendar.getInstance();
    try {
      ca.setTime(sdf.parse(endDate));
    } catch (ParseException e) {
      throw new IllegalArgumentException("Incorrect format.");
    }
    ca.add(Calendar.DATE, 1);  // number of days to add
    Date newEnd = ca.getTime();

    while (current.before(newEnd)) {
      try {
        double calcVal = calcVal(sdf.format(current));
        portfolioVal.put(sdf.format(current), (int) Math.round(calcVal));
      } catch (IllegalArgumentException ignored) {

      }
      Calendar c = Calendar.getInstance();
      c.setTime(current);
      c.add(Calendar.DATE, 1);  // number of days to add
      current = c.getTime();
    }
    return portfolioVal;
  }

  /**
   * Checks if the date exists in the hashmap.
   *
   * @param portfolioVal the hashmap containing dates and portfolio values
   * @param date         to ensure it is in hashmap
   * @param sdf          date format
   * @param val          amount to increase or decrease date by
   * @return the updated date if the date did not exist in the hashmap
   * @throws ParseException if the date string is unable to be converted into a date
   */
  private String checkForDate(Map<String, Integer> portfolioVal, String date,
                              SimpleDateFormat sdf, int val) throws ParseException {
    Date current;
    while (!portfolioVal.containsKey(date)) {
      Calendar c = Calendar.getInstance();
      c.setTime(sdf.parse(date));
      c.add(Calendar.DATE, val);  // number of days to add
      current = c.getTime();
      date = sdf.format(current);
    }
    return date;
  }

  /**
   * Gets the value of the portfolio on the date if it exists in the portfolio.
   *
   * @param portfolioVal containing dates and values of portfolio
   * @param date         to be searched for in hashmap
   * @return the updated value of the portfolio on the date
   */
  private int getDateNum(Map<String, Integer> portfolioVal, String date) {
    int dateNum = 0;
    if (portfolioVal.containsKey(date)) {
      dateNum = portfolioVal.get(date);
    }
    return dateNum;
  }

  /**
   * Removes every other value from the hashmap until there are no more than 30 values in the map.
   *
   * @param portfolioVal hashmap to be shrunk
   * @return updated hashmap containing dates and portfolio values
   */
  private Map<String, Integer> removeVals(Map<String, Integer> portfolioVal) {
    int count = 1;
    String[] keyArray = portfolioVal.keySet().toArray(new String[portfolioVal.size()]);
    Arrays.sort(keyArray);

    while (portfolioVal.size() > 28) {
      for (String key : keyArray) {
        if (count % 2 == 0 && count != 0 && count != portfolioVal.size()) {
          portfolioVal.remove(key);
          int index = Arrays.binarySearch(keyArray, key);
          keyArray = removeAtIndex(index, keyArray);
        }
        if (portfolioVal.size() <= 28) {
          break;
        }
        count += 1;
      }
    }
    return portfolioVal;
  }

  /**
   * Adds a date and value of portfolio to the hashmap if not already in map.
   *
   * @param portfolioVal portfolio to be searched through
   * @param date         to be added
   * @param dateNum      value of portfolio on date to be added
   * @return the updated hashmap
   */
  Map<String, Integer> addStartEnd(Map<String, Integer> portfolioVal, String date, int dateNum) {
    if (!portfolioVal.containsKey(date) && dateNum != 0) {
      portfolioVal.put(date, dateNum);
    }
    return portfolioVal;
  }

  /**
   * Finds the common factor between each value of the portfolio.
   *
   * @param portfolioVal contains values with common factors
   * @return common factor
   */
  private int commonFactor(Map<String, Integer> portfolioVal) {
    int divide = 1;
    for (Map.Entry<String, Integer> entry : portfolioVal.entrySet()) {
      int num = portfolioVal.get(entry.getKey());
      int dividedVal = num / divide;
      while (dividedVal > 50) {
        divide += 1;
        dividedVal = num / divide;
      }
    }
    return divide;
  }

  @Override
  public void buyStock(String name, Map<String, Stock> stocks, double quantity, String date)
          throws IllegalArgumentException, ParseException {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity cannot be negative.");
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    if (!stocks.containsKey(date)) {
      throw new IllegalArgumentException("Date does not exist.");
    }

    // Iterate through existing stocks to find the matching date
    boolean dateExists = false;
    for (Map.Entry<String, Stock> entry : stocks.entrySet()) {
      String d = entry.getKey();
      if (d.equals(date) || sdf.parse(date).before(sdf.parse(d))) {
        dateExists = true;
        double currentQuantity = 0;
        Map<String, Stock> stockMap = new HashMap<String, Stock>();
        if (entry.getValue().getTicker().equals(name)) {
          stockMap.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<Map<String, Stock>, Double> entry1 : quant.entrySet()) {
          for (Map.Entry<String, Stock> entry3 : entry1.getKey().entrySet()) {
            if (entry3.getKey().equals(d) && entry3.getValue().getTicker().equals(name)) {
              currentQuantity = entry1.getValue();
            }
          }
        }
        quant.put(stockMap, currentQuantity + quantity);
      }
    }
    // If no matching date is found, add a new entry
    if (!dateExists) {
      throw new IllegalArgumentException("No stocks found for the given date.");
    }
  }

  @Override
  public void sellStock(String name, Map<String, Stock> stocks, double quantity, String date)
          throws IllegalArgumentException, ParseException {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity cannot be negative.");
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    if (!stocks.containsKey(date)) {
      throw new IllegalArgumentException("Date does not exist.");
    }

    // Iterate through existing stocks to find the matching date
    boolean dateExists = false;
    for (Map.Entry<Map<String, Stock>, Double> entry1 : quant.entrySet()) {
      double currentQuantity = 0;
      for (Map.Entry<String, Stock> entry3 : entry1.getKey().entrySet()) {
        Map<String, Stock> stockMap = new HashMap<String, Stock>();
        String d = entry3.getKey();
        if (d.equals(date) || sdf.parse(date).before(sdf.parse(d))) {
          dateExists = true;
          if (entry3.getValue().getTicker().equals(name)) {
            stockMap.put(entry3.getKey(), entry3.getValue());
            currentQuantity = entry1.getValue();
            if (quantity > currentQuantity) {
              quant.put(stockMap, 0.0);
            } else {
              quant.put(stockMap, currentQuantity - quantity);
            }
          }
        }
      }
    }
    if (!dateExists) {
      throw new IllegalArgumentException("No stocks found for the given date.");
    }
  }

  @Override
  public Map<String, Double> getComposition(String date) throws IllegalArgumentException {
    if (checkDateFormat(date)) {
      throw new IllegalArgumentException("Invalid date.");
    }

    Map<String, Double> composition = new HashMap<>();
    boolean checkDateExists = false;
    for (Map.Entry<Map<String, Stock>, Double> entry : quant.entrySet()) {
      Map<String, Stock> stockMap = entry.getKey();
      if (stockMap.containsKey(date)) {
        Stock stock = stockMap.get(date);
        String ticker = stock.getTicker();
        composition.put(ticker, composition.getOrDefault(ticker, 0.0)
                + entry.getValue());
        checkDateExists = true;
      }
    }
    if (checkDateExists) {
      return composition;
    } else {
      throw new IllegalArgumentException("Date does not exist.");
    }
  }

  /**
   * Checks that the date is in the correct format.
   * @param date the date entered by the user
   * @return whether or no the date has been entered correctly
   */
  private boolean checkDateFormat(String date) {
    return date.split("-").length != 3 || date.split("-")[0].length() != 4
            || date.split("-")[1].length() != 2
            || date.split("-")[2].length() != 2;
  }

  @Override
  public Map<String, Double> getValueDistribution(String date) throws IllegalArgumentException {
    if (checkDateFormat(date)) {
      throw new IllegalArgumentException("Invalid date.");
    }
    Map<String, Double> distribution = new HashMap<>();
    for (Map.Entry<Map<String, Stock>, Double> entry : quant.entrySet()) {
      Map<String, Stock> oneStock = entry.getKey();
      if (oneStock.containsKey(date)) {
        Stock stock = oneStock.get(date);
        double stockValue = stock.getPriceAtClose() * entry.getValue();
        distribution.put(stock.getTicker(), stockValue);
      }
    }
    return distribution;
  }

  @Override
  public void saveToFile(String filename) throws IOException {
    if (filename.length() <= 4 || !filename.substring(filename.length() - 4)
            .equals(".txt")) {
      filename = filename + ".txt";
    }
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
      for (Map.Entry<Map<String, Stock>, Double> entry : quant.entrySet()) {
        Map<String, Stock> stockMap = entry.getKey();
        int count = 0;
        for (Map.Entry<String, Stock> stockEntry : stockMap.entrySet()) {
          if (count == 0) {
            String stockName = stockEntry.getValue().getTicker();
            writer.write("Stock: " + stockName + "\n");
            count += 1;
          }
          String ticker = stockEntry.getKey();
          Stock stock = stockEntry.getValue();
          writer.write(ticker + "," + entry.getValue() + "," + stock.getPriceAtClose() + ","
                  + stock.getTotalShares() + "\n");
        }
      }
    }
  }


  @Override
  public void loadFromFile(String filename) throws IOException {
    if (filename.length() <= 4
            || !filename.substring(filename.length() - 4).equals(".txt")) {
      throw new IllegalArgumentException("Incorrect file format.");
    }
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String line;
      String stockName = "";
      while ((line = reader.readLine()) != null) {
        if (line.startsWith("Stock: ")) {
          stockName = line.substring(7, line.length());
        } else if (line.isEmpty()) {
          break;
        } else {
          String[] parts = line.split(",");
          String ticker = parts[0];
          double quantity = Double.parseDouble(parts[1]);
          double priceAtClose = Double.parseDouble(parts[2]);
          int totalShares = Integer.parseInt(parts[3]);
          Stock stock = new StockImpl(stockName, priceAtClose, totalShares);
          Map<String, Stock> stockMap = new HashMap<>();
          stockMap.put(ticker, stock);
          quant.put(stockMap, quantity);
        }
      }
    }
  }

  @Override
  public void rebalance(String filename, Map<String, Double> targetDistribution, String date)
          throws IllegalArgumentException, IOException, ParseException {
    double totalValue = calcVal(date);
    if (totalValue == 0) {
      throw new IllegalArgumentException("Cannot rebalance an empty portfolio.");
    }
    Collection<Double> weights = targetDistribution.values();
    for (Double w : weights) {
      if (w < 0 || w > 1) {
        throw new IllegalArgumentException("Incorrect weight value.");
      }
    }
    Map<String, Double> currentComposition = getComposition(date);
    Map<String, Double> currentValues = getValueDistribution(date);

    for (Map.Entry<String, Double> entry : targetDistribution.entrySet()) {
      String ticker = entry.getKey();
      double targetPercentage = entry.getValue();
      double targetValue = totalValue * targetPercentage;

      double currentValue = currentValues.getOrDefault(ticker, 0.0);
      double currentShares = currentComposition.getOrDefault(ticker, 0.0);

      if (currentValue < targetValue) {
        // Buy more shares
        double amountToInvest = targetValue - currentValue;
        Stock stock = null;
        for (Map<String, Stock> stockMap : quant.keySet()) {
          if (stockMap.containsKey(date)) {
            stock = stockMap.get(date);
            if (stock.getTicker().equals(ticker)) {
              stock = stockMap.get(date);
              break;
            }
          }
        }
        if (stock == null) {
          throw new IllegalArgumentException("Stock not found: " + ticker);
        }
        double sharesToBuy = amountToInvest / stock.getPriceAtClose();
        if (sharesToBuy > 0) {
          Map<String, Stock> stocksToBuy = new HashMap<>();
          //stocksToBuy.put(date, stock);
          for (Map<String, Stock> stocksRN : quant.keySet()) {
            for (String stockDate : stocksRN.keySet()) {
              if (stocksRN.get(stockDate).getTicker().equals(ticker)) {
                stocksToBuy.put(stockDate, stocksRN.get(stockDate));
              }
            }
          }
          buyStock(stock.getTicker(), stocksToBuy, sharesToBuy, date);
        }
      } else if (currentValue > targetValue) {
        // Sell shares
        double amountToSell = currentValue - targetValue;
        Stock stock = null;
        for (Map<String, Stock> stockMap : quant.keySet()) {
          if (stockMap.containsKey(date)) {
            stock = stockMap.get(date);
            if (stock.getTicker().equals(ticker)) {
              stock = stockMap.get(date);
              break;
            }
          }
        }
        if (stock == null) {
          throw new IllegalArgumentException("Stock not found: " + ticker);
        }
        double sharesToSell = amountToSell / stock.getPriceAtClose();
        if (sharesToSell > 0) {
          Map<String, Stock> stocksToSell = new HashMap<>();
          stocksToSell.put(date, stock);
          sellStock(stock.getTicker(), stocksToSell, sharesToSell, date);
        }
      }
      this.saveToFile(filename);
    }
  }

  @Override
  public List<String> getStock() {
    List<String> list = new ArrayList<String>();
    for (Map.Entry<Map<String, Stock>, Double> entry : quant.entrySet()) {
      Map<String, Stock> oneStock = entry.getKey();
      Stock stock = null;
      for (Map.Entry<String, Stock> s : oneStock.entrySet()) {
        stock = s.getValue();
      }
      if (stock != null) {
        list.add(stock.getTicker());
      }
    }
    return list;
  }
}
