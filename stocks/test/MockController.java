import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import stocks.model.Stock;
import stocks.model.StockModel;

/**
 * Creates a version of the controller to test the inputs.
 */
public class MockController implements StockModel {
  final StringBuilder log;

  /**
   * Creates an instance of the controller that takes in a log for testing.
   *
   * @param log string to contain inputs
   */
  public MockController(StringBuilder log) {
    this.log = Objects.requireNonNull(log);
  }

  @Override
  public double gainOrLoss(Map<String, Stock> stock, String startDate, String endDate) {
    log.append(String.format("Start = %s, End = %s", startDate, endDate));
    return 0;
  }

  @Override
  public double xDayMovingAverage(Map<String, Stock> stock, String date, int x) {
    log.append(String.format("Date = %s, x = %d", date, x));
    return 0;
  }

  @Override
  public ArrayList<String> crossovers(Map<String, Stock> stock, String startDate,
                                      String endDate, int x) {
    log.append(String.format("Start Date = %s, End Date = %s, x = %d", startDate, endDate, x));
    return null;
  }
}
