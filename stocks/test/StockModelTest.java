import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import stocks.model.APICall;
import stocks.model.AlphaVantAPI;
import stocks.model.Stock;
import stocks.model.StockImpl;
import stocks.model.StockModelImpl;
import stocks.model.StockModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the methods in the model.
 */
public class StockModelTest {
  StockModel stockModel = new StockModelImpl();
  APICall api = new AlphaVantAPI();

  @Test
  public void testGainLoss() {
    assertEquals(-3.44, stockModel.gainOrLoss(api.lookUpStock("GOOG"),
            "2024-05-29", "2024-05-31"), 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGainLossInvalidStartDate() {
    stockModel.gainOrLoss(api.lookUpStock("GOOG"),
            "2025-05-29", "2024-05-31");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGainLossInvalidEndDate() {
    stockModel.gainOrLoss(api.lookUpStock("GOOG"),
            "2024-05-29", "2025-05-31");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGainLossInvalidDateRange() {
    stockModel.gainOrLoss(api.lookUpStock("GOOG"),
            "2024-05-29", "2023-05-31");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGainLossIncorrectDateFormat() {
    assertEquals(-3.44, stockModel.gainOrLoss(api.lookUpStock("GOOG"),
            "2024-5-29", "2024-05-31"), 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGainLossIncorrectEndDateFormat() {
    assertEquals(-3.44, stockModel.gainOrLoss(api.lookUpStock("GOOG"),
            "2024-05-29", "2024-05-1"), 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGainLossIncorrectEndDateFormat2() {
    assertEquals(-3.44, stockModel.gainOrLoss(api.lookUpStock("GOOG"),
            "2024-05-29", "a"), 0.01);
  }

  @Test
  public void testGain() {
    assertEquals(6.86, stockModel.gainOrLoss(api.lookUpStock("GOOG"),
            "2024-05-08", "2024-05-28"), 0.01);
  }

  @Test
  public void testLossOver5Days() {
    assertEquals(-2.14, stockModel.gainOrLoss(api.lookUpStock("GOOG"),
            "2024-05-21", "2024-05-29"), 0.01);
  }

  @Test
  public void testGainOver5Days() {
    assertEquals(102.77, stockModel.gainOrLoss(api.lookUpStock("GOOG"),
            "2022-06-13", "2022-06-21"), 0.01);
  }

  @Test
  public void testXDayMovinAvg() {
    assertEquals(177.53, stockModel.xDayMovingAverage(
            api.lookUpStock("GOOG"), "2024-05-23", 3), 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testXDayMovinAvgInvalidDate() {
    assertEquals(177.53, stockModel.xDayMovingAverage(
            api.lookUpStock("GOOG"), "2025-05-23", 3), 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testXDayMovinAvgInvalidX() {
    assertEquals(177.53, stockModel.xDayMovingAverage(
            api.lookUpStock("GOOG"), "2024-05-23", -3), 0.01);
  }

  @Test
  public void testXDayMovinAvg1() {
    assertEquals(558.4600, stockModel.xDayMovingAverage(
            api.lookUpStock("GOOG"), "2014-03-27", 3), 0.01);
  }

  @Test
  public void test5DayMovinAvg1() {
    assertEquals(176.962, stockModel.xDayMovingAverage(
            api.lookUpStock("GOOG"), "2024-05-29", 5), 0.01);
  }

  @Test
  public void test5DayMovinAvg() {
    assertEquals(927.63, stockModel.xDayMovingAverage(
            api.lookUpStock("GOOG"), "2017-08-08", 5), 0.01);
  }

  @Test
  public void test30DayMovinAvg() {
    assertEquals(558.46, stockModel.xDayMovingAverage(
            api.lookUpStock("GOOG"), "2014-03-27", 30), 0.01);
  }

  @Test
  public void test30DayMovinAvg2() {
    assertEquals(2736.329667, stockModel.xDayMovingAverage(
            api.lookUpStock("GOOG"), "2022-02-16", 30), 0.01);
  }


  @Test
  public void test30DayMovinAvg1() {
    assertEquals(1357.61, stockModel.xDayMovingAverage(
            api.lookUpStock("GOOG"), "2020-06-02", 30), 0.01);
  }

  @Test
  public void testCrossOvers() {
    ArrayList<String> days = new ArrayList<String>();
    assertEquals(days, stockModel.crossovers(api.lookUpStock("GOOG"),
            "2024-05-29","2024-05-30", 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCrossOversInvalidStartDate() {
    ArrayList<String> days = new ArrayList<String>();
    assertEquals(days, stockModel.crossovers(api.lookUpStock("GOOG"),
            "2025-05-29","2024-05-30", 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCrossOversInvalidendDate() {
    ArrayList<String> days = new ArrayList<String>();
    assertEquals(days, stockModel.crossovers(api.lookUpStock("GOOG"),
            "2024-05-29","2025-05-30", 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCrossOversInvalidDateRange() {
    ArrayList<String> days = new ArrayList<String>();
    assertEquals(days, stockModel.crossovers(api.lookUpStock("GOOG"),
            "2024-05-29","2023-05-30", 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCrossOversInvalidX() {
    ArrayList<String> days = new ArrayList<String>();
    assertEquals(days, stockModel.crossovers(api.lookUpStock("GOOG"),
            "2024-05-29","2024-05-30", -2));
  }

  @Test
  public void testCrossOversAgain() {
    ArrayList<String> days = new ArrayList<String>();
    days.add("2024-05-02");
    days.add("2024-05-03");
    assertEquals(days, stockModel.crossovers(api.lookUpStock("GOOG"),
            "2024-05-02","2024-05-03", 2));
  }

  @Test
  public void testLookUp() {
    Map<String, Stock> s = api.lookUpStock("GOOG");
    String arr = s.keySet().toString().replace("[", "")
            .replace("]", "").replace(",", "");
    assertTrue(arr.contains("2024-06-04"));
  }

  @Test
  public void testGainLossMSFT() {
    assertEquals(-13.91, stockModel.gainOrLoss(api.lookUpStock("MSFT"),
            "2024-05-21", "2024-05-31"), 0.01);
  }

  @Test
  public void testXDayMovinAvgAAPL() {
    assertEquals(175.24, stockModel.xDayMovingAverage(
            api.lookUpStock("AAPL"), "2023-05-30", 3), 0.01);
  }

  @Test
  public void testCrossOversMSFT() {
    ArrayList<String> days = new ArrayList<>();
    assertEquals(days, stockModel.crossovers(api.lookUpStock("MSFT"),
            "2024-05-29", "2024-05-30", 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLookUpNonExistentStock() {
    Map empty = new HashMap();
    assertEquals(empty,api.lookUpStock("FAKE"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGainLossDistantPastDate() {
    stockModel.gainOrLoss(api.lookUpStock("GOOG"),
            "1980-01-01", "1980-01-02");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGainLossFutureDate() {
    stockModel.gainOrLoss(api.lookUpStock("GOOG"),
            "2050-01-01", "2050-01-02");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testXDayMovingAvgDistantPastDate() {
    stockModel.xDayMovingAverage(api.lookUpStock("GOOG"), "1980-01-01", 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testXDayMovingAvgFutureDate() {
    stockModel.xDayMovingAverage(api.lookUpStock("GOOG"), "2050-01-01", 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCrossoversDistantPastDate() {
    stockModel.crossovers(api.lookUpStock("GOOG"),
            "1980-01-01", "1980-01-02", 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCrossoversFutureDate() {
    stockModel.crossovers(api.lookUpStock("GOOG"),
            "2050-01-01", "2050-01-02", 3);
  }

  @Test
  public void testCrossOversGOOG() {
    ArrayList<String> days = new ArrayList<>();
    days.add("2022-02-10");
    days.add("2022-02-16");
    assertEquals(days, stockModel.crossovers(api.lookUpStock("GOOG"),
            "2022-02-10", "2022-03-03", 30));
  }

  @Test
  public void testCrossOversGOOG1() {
    ArrayList<String> days = new ArrayList<>();
    days.add("2023-06-07");
    days.add("2023-06-08");
    days.add("2023-06-09");
    days.add("2023-06-12");
    days.add("2023-06-13");
    days.add("2023-06-14");
    days.add("2023-06-15");
    days.add("2023-06-16");
    assertEquals(days, stockModel.crossovers(api.lookUpStock("GOOG"),
            "2023-06-07", "2023-06-16", 30));
  }

  @Test
  public void testGetTotalShares() {
    Stock s = new StockImpl("GOOG", 22.4, 1000);
    assertEquals(1000, s.getTotalShares());
  }

}