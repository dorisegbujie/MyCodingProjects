package stocks.model;

/**
 * Represents a single stock on a specific date.
 * Contains the price at open, highest and lowest prices, and price at close on the date.
 */
public class StockImpl implements Stock {
  String name;
  private double priceAtClose;
  private int vol;

  /**
   * Creates an instance of the stock on one day.
   * @param priceAtClose price of stock at close
   * @param vol volume of stock
   */
  public StockImpl(String name, double priceAtClose, int vol) {
    this.name = name;
    this.priceAtClose = priceAtClose;
    this.vol = vol;
  }

  @Override
  public double getPriceAtClose() {
    return priceAtClose;
  }

  @Override
  public int getTotalShares() {
    return vol;
  }

  @Override
  public String getTicker() {
    return this.name;
  }
}
