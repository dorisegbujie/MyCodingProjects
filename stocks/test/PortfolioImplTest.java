import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import stocks.model.APICall;
import stocks.model.AlphaVantAPI;
import stocks.model.Portfolio;
import stocks.model.PortfolioImpl;
import stocks.model.Stock;
import stocks.model.StockImpl;

import static org.junit.Assert.assertEquals;

/**
 * Tests the portfolio methods.
 */
public class PortfolioImplTest {

  @Test
  public void calculateValueDifferentDate() throws ParseException {
    Stock s = new StockImpl("GOOG", 3, 100);
    HashMap<String, Stock> stock = new HashMap<>();
    PortfolioImpl portfolio = new PortfolioImpl();
    HashMap<String, Double> stock1 = new HashMap<>();
    stock1.put("GOOG", 2.0);
    Stock s1 = new StockImpl("GOOG", 4, 100);
    stock.put("2024-12-06", s1);

    stock.put("2024-12-05", s);
    portfolio.buyStock("GOOG", stock, 2, "2024-12-05");
    assertEquals(stock1, portfolio.getComposition("2024-12-05"));

    portfolio.buyStock("GOOG", stock, 2, "2024-12-05");

    assertEquals(12, portfolio.calcVal("2024-12-05"), 0.01);
    assertEquals(16, portfolio.calcVal("2024-12-06"), 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPortfolioValueOnDateWithNoStockEntries() {
    PortfolioImpl portfolio = new PortfolioImpl();
    assertEquals(0.0, portfolio.calcVal("2025-12-05"), 0.01);
  }

  @Test
  public void calculateValueDifferentDate1() throws ParseException {
    Stock s = new StockImpl("GOOG", 3, 100);
    HashMap<String, Stock> stock = new HashMap<>();
    PortfolioImpl portfolio = new PortfolioImpl();
    HashMap<String, Double> stock1 = new HashMap<>();
    stock1.put("GOOG", 2.0);

    stock.put("2024-12-05", s);
    Stock s1 = new StockImpl("GOOG", 4, 100);
    stock.put("2024-12-06", s1);

    portfolio.buyStock("GOOG", stock, 2, "2024-12-05");
    assertEquals(stock1, portfolio.getComposition("2024-12-06"));
    assertEquals(stock1, portfolio.getComposition("2024-12-05"));

    assertEquals(6, portfolio.calcVal("2024-12-05"), 0.01);
    assertEquals(8, portfolio.calcVal("2024-12-06"), 0.01);
  }

  @Test
  public void testBuySameStockMultipleTimes() throws ParseException {
    Stock s = new StockImpl("GOOG", 3, 100);
    HashMap<String, Stock> stock = new HashMap<>();
    stock.put("2023-12-05", s);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", stock, 2, "2023-12-05");
    portfolio.buyStock("GOOG", stock, 3, "2023-12-05");
    assertEquals(15, portfolio.calcVal("2023-12-05"), 0.01);
  }

  @Test
  public void testCalculateValueForMultipleDates() throws ParseException {
    Stock s = new StockImpl("GOOG", 3, 100);
    HashMap<String, Stock> stock = new HashMap<>();
    stock.put("2023-12-05", s);
    stock.put("2023-12-06", s);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", stock, 2, "2023-12-05");
    assertEquals(6, portfolio.calcVal("2023-12-05"), 0.01);
    assertEquals(6, portfolio.calcVal("2023-12-06"), 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateValueForDistantPastDate() {
    Stock s = new StockImpl("GOOG", 3, 100);
    HashMap<String, Stock> stock = new HashMap<>();
    stock.put("2023-12-05", s);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.calcVal("1980-01-01");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateValueForFutureDate() {
    Stock s = new StockImpl("GOOG", 3, 100);
    HashMap<String, Stock> stock = new HashMap<>();
    stock.put("2023-12-05", s);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.calcVal("2050-01-01");
  }

  @Test
  public void testPortfolioPerformanceIncorrectDateFormat() throws ParseException {
    Stock s1 = new StockImpl("GOOG", 3, 100);
    Stock s = new StockImpl("GOOG", 10, 100);
    HashMap<String, Stock> stock = new HashMap<>();
    stock.put("2023-12-05", s1);
    stock.put("2023-12-06", s);
    Portfolio portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", stock, 2, "2023-12-05");
    Map<String, Integer> stock1;
    stock1 = portfolio.portfolioPerformance("2023-12-05", "2023-12-06");
    int val = (int) Math.round(portfolio.calcVal("2023-12-05"));
    int sol = stock1.get("2023-12-05");
    assertEquals(val, sol);

    int val1 = (int) Math.round(portfolio.calcVal("2023-12-06"));
    int sol1 = stock1.get("2023-12-06");
    assertEquals(val1, sol1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPortfolioPerformanceDateFormat() throws ParseException {
    Stock s1 = new StockImpl("GOOG", 3, 100);
    Stock s = new StockImpl("GOOG", 10, 100);
    Map<String, Stock> stock = new HashMap<>();
    stock.put("2024-12-05", s1);
    stock.put("2024-12-06", s);
    Portfolio portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", stock, 2, "2024-12-05");
    Map<String, Integer> stock1;
    stock1 = portfolio.portfolioPerformance("2024-12-05", "2024-12-06");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPortfolioPerformanceDateIncorrectDate() throws ParseException {
    Stock s1 = new StockImpl("GOOG", 3, 100);
    Stock s = new StockImpl("GOOG", 10, 100);
    Map<String, Stock> stock = new HashMap<>();
    stock.put("2024-12-05", s1);
    stock.put("2024-12-06", s);
    Portfolio portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", stock, 2, "2024-12-05");
    portfolio.portfolioPerformance("2024-12-05", "20-12-06");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPortfolioPerformanceDateIncorrectDateNotNums() throws ParseException {
    Stock s1 = new StockImpl("GOOG", 3, 100);
    Stock s = new StockImpl("GOOG", 10, 100);
    Map<String, Stock> stock = new HashMap<>();
    stock.put("2024-12-05", s1);
    stock.put("2024-12-06", s);
    Portfolio portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", stock, 2, "2024-12-05");
    portfolio.portfolioPerformance("2024-12-05", "aaaa-12-06");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPortfolioPerformanceDateIncorrectDateNotNums2() throws ParseException {
    Stock s1 = new StockImpl("GOOG", 3, 100);
    Stock s = new StockImpl("GOOG", 10, 100);
    Map<String, Stock> stock = new HashMap<>();
    stock.put("2024-12-05", s1);
    stock.put("2024-12-06", s);
    Portfolio portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", stock, 2, "2024-12-05");
    portfolio.portfolioPerformance("aaaa-12-05", "2024-12-06");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPortfolioPerformanceDateInFuture() throws ParseException {
    Stock s1 = new StockImpl("GOOG", 3, 100);
    Stock s = new StockImpl("GOOG", 10, 100);
    Map<String, Stock> stock = new HashMap<>();
    stock.put("2024-12-05", s1);
    stock.put("2024-12-06", s);
    Portfolio portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", stock, 2, "2024-12-05");
    portfolio.portfolioPerformance("2030", "2050");
  }

  @Test
  public void testBuyStock() throws ParseException {
    Stock s = new StockImpl("GOOG", 3, 100);
    Map<String, Stock> stock = new HashMap<>();
    stock.put("2024-12-05", s);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.buyStock(s.getTicker(), stock, 2, "2024-12-05");
    assertEquals(6.0, portfolio.calcVal("2024-12-05"), 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyStockNegativeQuantity() throws ParseException {
    Stock s = new StockImpl("GOOG", 3, 100);
    Map<String, Stock> stock = new HashMap<>();
    stock.put("GOOG", s);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.buyStock(s.getTicker(), stock, -1, "2024-12-05");
  }

  @Test
  public void testSellStock() throws ParseException {
    Stock s = new StockImpl("GOOG", 3, 100);
    Map<String, Stock> stock = new HashMap<>();
    stock.put("2024-12-05", s);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.buyStock(s.getTicker(), stock, 5.0, "2024-12-05");
    portfolio.sellStock(s.getTicker(), stock, 2.0, "2024-12-05");
    assertEquals(9.0, portfolio.calcVal("2024-12-05"), 0.01);
  }

  @Test
  public void testSellStockMoreThanOwned() throws ParseException {
    Stock s = new StockImpl("GOOG", 3, 100);
    Map<String, Stock> stock = new HashMap<>();
    stock.put("2024-12-05", s);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.buyStock(s.getTicker(), stock, 2, "2024-12-05");
    portfolio.sellStock(s.getTicker(), stock, 3, "2024-12-05");
    assertEquals(0.0, portfolio.calcVal("2024-12-05"), 0.01);
  }

  @Test
  public void testGetComposition() throws ParseException {
    Stock s1 = new StockImpl("GOOG", 3, 100);
    Stock s2 = new StockImpl("RACH", 5, 200);
    Map<String, Stock> stock1 = new HashMap<>();
    Map<String, Stock> stock2 = new HashMap<>();
    stock1.put("2024-06-06", s1);
    stock2.put("2024-06-06", s2);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", stock1, 2, "2024-06-06");
    portfolio.buyStock("RACH", stock2, 3, "2024-06-06");
    Map<String, Double> composition = portfolio.getComposition("2024-06-06");
    double compVal = composition.get("GOOG");
    assertEquals(2.0, compVal, 0.01);
    double compValRach = composition.get("RACH");
    assertEquals(3.0, compValRach, 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCompositionIncorrectDate() throws ParseException {
    Stock s1 = new StockImpl("GOOG", 3, 100);
    Stock s2 = new StockImpl("RACH", 5, 200);
    Map<String, Stock> stock1 = new HashMap<>();
    Map<String, Stock> stock2 = new HashMap<>();
    stock1.put("2024-06-06", s1);
    stock2.put("2024-06-06", s2);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", stock1, 2, "2024-06-06");
    portfolio.buyStock("RACH", stock2, 3, "2024-06-06");
    Map<String, Double> composition = portfolio.getComposition("2024-0606");
  }

  @Test
  public void testGetValueDistribution() throws ParseException {
    Stock s1 = new StockImpl("GOOG", 3, 100);
    Stock s2 = new StockImpl("RACH", 5, 200);
    Map<String, Stock> stock1 = new HashMap<>();
    Map<String, Stock> stock2 = new HashMap<>();
    stock1.put("2023-12-05", s1);
    stock2.put("2023-12-05", s2);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", stock1, 2, "2023-12-05");
    portfolio.buyStock("RACH", stock2, 3, "2023-12-05");
    Map<String, Double> distribution = portfolio.getValueDistribution("2023-12-05");
    double val = distribution.get("GOOG");
    assertEquals(6.0, val, 0.01);
    double val1 = distribution.get("RACH");
    assertEquals(15.0, val1, 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetValueDistributionIncorrectDate() throws ParseException {
    Stock s1 = new StockImpl("GOOG", 3, 100);
    Stock s2 = new StockImpl("RACH", 5, 200);
    Map<String, Stock> stock1 = new HashMap<>();
    Map<String, Stock> stock2 = new HashMap<>();
    stock1.put("2023-12-05", s1);
    stock2.put("2023-12-05", s2);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", stock1, 2, "2023-12-05");
    portfolio.buyStock("RACH", stock2, 3, "2023-12-05");
    portfolio.getValueDistribution("2023--05");
  }

  @Test
  public void testSaveAndLoadPortfolio() throws IOException, ParseException {
    Stock s1 = new StockImpl("GOOG", 3, 100);
    Stock s2 = new StockImpl("GOOG", 5, 200);
    Map<String, Stock> stock1 = new HashMap<>();
    Map<String, Stock> stock2 = new HashMap<>();
    stock1.put("2023-12-05", s1);
    stock2.put("2023-12-05", s2);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", stock1, 2, "2023-12-05");
    portfolio.buyStock("GOOG", stock2, 3, "2023-12-05");

    String filename = "test_portfolio.txt";
    portfolio.saveToFile(filename);
    PortfolioImpl loadedPortfolio = new PortfolioImpl();
    loadedPortfolio.loadFromFile(filename);
    assertEquals(portfolio.calcVal("2023-12-05"), loadedPortfolio.calcVal("2023-12-05"), 0.01);
  }

  @Test
  public void testLoadFromFile() throws IOException {
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.loadFromFile("test_portfolio.txt.txt");
    assertEquals(250.0, portfolio.calcVal("2023-12-05"), 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadFromFileDoesntExist() throws IOException {
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.loadFromFile("test_portfo");
    assertEquals(31.0, portfolio.calcVal("2023-12-05"), 0.01);
  }

  @Test
  public void testRebalancePortfolio() throws IOException, ParseException {
    Stock s1 = new StockImpl("GOOG", 2, 100);
    Stock s2 = new StockImpl("RACH", 4, 200);
    Map<String, Stock> stock1 = new HashMap<>();
    Map<String, Stock> stock2 = new HashMap<>();
    stock1.put("2023-12-05", s1);
    stock2.put("2023-12-05", s2);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", stock1, 2, "2023-12-05");
    portfolio.buyStock("RACH", stock2, 4, "2023-12-05");

    Map<String, Double> newComp = new HashMap<>();
    newComp.put("GOOG", 5.0);
    newComp.put("RACH", 2.5);

    Map<String, Double> targetDistribution = new HashMap<>();
    targetDistribution.put("GOOG", 0.5);
    targetDistribution.put("RACH", 0.5);
    portfolio.rebalance("test_portfolio_rebalance2For.txt", targetDistribution,
            "2023-12-05");
    assertEquals(20.0, portfolio.calcVal("2023-12-05"), 0.01);
    assertEquals(newComp, portfolio.getComposition("2023-12-05"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRebalancePortfolioIncorrectDate() throws IOException, ParseException {
    Stock s1 = new StockImpl("GOOG", 2, 100);
    Stock s2 = new StockImpl("RACH", 4, 200);
    Map<String, Stock> stock1 = new HashMap<>();
    Map<String, Stock> stock2 = new HashMap<>();
    stock1.put("2023-12-05", s1);
    stock2.put("2023-12-05", s2);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", stock1, 2, "2023-12-05");
    portfolio.buyStock("RACH", stock2, 4, "2023-12-05");

    Map<String, Double> targetDistribution = new HashMap<>();
    targetDistribution.put("GOOG", 0.5);
    targetDistribution.put("RACH", 0.5);
    portfolio.rebalance("test_portfolio_rebalance2For.txt", targetDistribution,
            "2023-1205");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyStockWithInvalidDate() throws ParseException {
    Stock s = new StockImpl("GOOG", 3, 100);
    Map<String, Stock> stock = new HashMap<>();
    stock.put("2024-12-05", s);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.buyStock(s.getTicker(), stock, 2, "2024-13-05");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStockWithInvalidDate() throws ParseException {
    Stock s = new StockImpl("GOOG", 3, 100);
    Map<String, Stock> stock = new HashMap<>();
    stock.put("2024-12-05", s);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.buyStock(s.getTicker(), stock, 2, "2024-12-05");
    portfolio.sellStock(s.getTicker(), stock, 2, "2024-13-05");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyStockWithZeroQuantity() throws ParseException {
    Stock s = new StockImpl("GOOG", 3, 100);
    Map<String, Stock> stock = new HashMap<>();
    stock.put("2024-12-05", s);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.buyStock(s.getTicker(), stock, 0, "2024-12-05");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyStockWithNegativeQuantity() throws ParseException {
    Stock s = new StockImpl("GOOG", 3, 100);
    Map<String, Stock> stock = new HashMap<>();
    stock.put("2024-12-05", s);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.buyStock(s.getTicker(), stock, -5, "2024-12-05");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStockWithZeroQuantity() throws ParseException {
    Stock s = new StockImpl("GOOG", 3, 100);
    Map<String, Stock> stock = new HashMap<>();
    stock.put("2024-12-05", s);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.buyStock(s.getTicker(), stock, 5, "2024-12-05");
    portfolio.sellStock(s.getTicker(), stock, 0, "2024-12-05");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStockWithNegativeQuantity() throws ParseException {
    Stock s = new StockImpl("GOOG", 3, 100);
    Map<String, Stock> stock = new HashMap<>();
    stock.put("2024-12-05", s);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.buyStock(s.getTicker(), stock, 5, "2024-12-05");
    portfolio.sellStock(s.getTicker(), stock, -5, "2024-12-05");
  }

  @Test
  public void testBuyStockWithEarliestValidDate() throws ParseException {
    Stock s = new StockImpl("GOOG", 3, 100);
    Map<String, Stock> stock = new HashMap<>();
    stock.put("1900-01-01", s);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.buyStock(s.getTicker(), stock, 2, "1900-01-01");
    assertEquals(6, portfolio.calcVal("1900-01-01"), 0.01);
  }

  @Test
  public void testBuyStockWithLatestValidDate() throws ParseException {
    Stock s = new StockImpl("GOOG", 3, 100);
    Map<String, Stock> stock = new HashMap<>();
    stock.put("2099-12-31", s);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.buyStock(s.getTicker(), stock, 2, "2099-12-31");
    assertEquals(6, portfolio.calcVal("2099-12-31"), 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyStockWithExcessiveQuantity() throws ParseException {
    Stock s = new StockImpl("GOOG", 3, 100);
    Map<String, Stock> stock = new HashMap<>();
    stock.put("2024-12-05", s);
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.buyStock(s.getTicker(), stock, Integer.MAX_VALUE + 1, "2024-12-05");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPortfolioValueForDateWithNoTransactions() throws ParseException {
    PortfolioImpl portfolio = new PortfolioImpl();
    assertEquals(0.0, portfolio.calcVal("2025-12-05"), 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAPICallWithInvalidStockSymbol() {
    APICall api = new AlphaVantAPI();
    api.lookUpStock("INVALID");
  }

  @Test(expected = FileNotFoundException.class)
  public void testLoadNonExistentPortfolioFile() throws IOException {
    PortfolioImpl portfolio = new PortfolioImpl();
    portfolio.loadFromFile("non_existent_file.txt");
  }


  @Test(expected = IllegalArgumentException.class)
  public void testSaveAndLoadEmptyPortfolio() throws IOException {
    PortfolioImpl portfolio = new PortfolioImpl();
    String filename = "empty_portfolio.txt";
    portfolio.saveToFile(filename);
    PortfolioImpl loadedPortfolio = new PortfolioImpl();
    loadedPortfolio.loadFromFile(filename);
    assertEquals(0.0, loadedPortfolio.calcVal("2023-12-05"), 0.01);
  }

}