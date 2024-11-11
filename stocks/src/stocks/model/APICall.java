package stocks.model;

import java.util.Map;

/**
 * Allows multiple APIs to be supported and used to look up a variety of stocks.
 */
public interface APICall {
  /**
   * Gets the stocks from the api and turns them
   * into a hashmap of dates as keys and stocks as values.
   * @param name the chosen stock
   * @return a hashmap with dates as values and stocks as keys
   */
  public Map<String, Stock> lookUpStock(String name);
}
