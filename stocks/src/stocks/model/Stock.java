package stocks.model;

/**
 * Represents a stock on a specific date, containing a name, price at close, and a volume.
 * Contains methods to get each of the fields.
 */
public interface Stock {

  /**
   * Gets the price of the stock at close.
   * @return price of the stock at close
   */
  public double getPriceAtClose();

  /**
   * Gets the total shares of the stock.
   * @return the total shares of the stock
   */
  public int getTotalShares();

  /**
   * Gets the name of this stock.
   * @return the name of this stock
   */
  public String getTicker();
}
