import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import stocks.model.APICall;
import stocks.model.AlphaVantAPI;
import stocks.model.Portfolio;
import stocks.model.PortfolioImpl;
import stocks.model.Stock;
import stocks.model.StockImpl;
import stocks.model.StockModel;
import stocks.model.StockModelImpl;
import stocks.view.MacroBarChartView;
import stocks.view.MacroStocksView;
import stocks.view.StocksView;
import stocks.view.View;
import stocks.view.ViewMacro;

import static org.junit.Assert.assertEquals;

/**
 * Tests the view.
 */
public class StocksViewTest {
  APICall api = new AlphaVantAPI();

  String stockPerformance = "Jan 02 2018: ******************\n" +
          "Feb 16 2018: ******************\n" +
          "May 21 2018: ******************\n" +
          "Aug 21 2018: ********************\n" +
          "Nov 20 2018: *****************\n" +
          "Jan 09 2019: ******************\n" +
          "Feb 26 2019: *******************\n" +
          "Apr 11 2019: ********************\n" +
          "May 29 2019: *******************\n" +
          "Jul 15 2019: *******************\n" +
          "Aug 28 2019: ********************\n" +
          "Oct 14 2019: ********************\n" +
          "Nov 27 2019: **********************\n" +
          "Jan 15 2020: ************************\n" +
          "Mar 03 2020: **********************\n" +
          "Apr 17 2020: *********************\n" +
          "Jun 03 2020: ************************\n" +
          "Jul 20 2020: **************************\n" +
          "Sep 02 2020: *****************************\n" +
          "Oct 19 2020: **************************\n" +
          "Dec 03 2020: *******************************\n" +
          "Jan 21 2021: ********************************\n" +
          "Mar 09 2021: ***********************************\n" +
          "Apr 23 2021: ***************************************\n" +
          "Jun 09 2021: ******************************************\n" +
          "Jul 26 2021: ***********************************************\n" +
          "Sep 09 2021: *************************************************\n" +
          "Oct 25 2021: ***********************************************\n" +
          "Dec 09 2021: **************************************************\n" +
          "Dec 31 2021: *************************************************\n" +
          "\n" +
          "Scale: * = 117";

  @Test
  public void testWelcomeMessage() {
    Appendable out = new StringWriter();
    View view = new StocksView(out);
    view.welcomeMessage();
    assertEquals("Welcome to the stocks program!\n" +
                    "1. Calculate gain/loss of a stock\n" +
                    "2. Calculate x-day moving average of a stock\n" +
                    "3. Find x-day crossovers for a stock\n" +
                    "4. Create portfolio and calculate its value\n" +
                    "5. Print supported instruction list\n" +
                    "6. Quit the program \n",
            out.toString());
  }

  @Test
  public void testNewWelcomeMessage() {
    Appendable out = new StringWriter();
    View view = new MacroStocksView(out);
    view.welcomeMessage();
    assertEquals("Welcome to the stocks program!\n" +
                    "1. Calculate gain/loss of a stock\n" +
                    "2. Calculate x-day moving average of a stock\n" +
                    "3. Find x-day crossovers for a stock\n" +
                    "4. Create an empty portfolio\n" +
                    "5. Calculate the value of an existing portfolio\n" +
                    "6. Get bar chart of portfolio or stocks value over period of time\n" +
                    "7. Buy shares\n" +
                    "8. Sell shares\n" +
                    "9. Get portfolio composition\n" +
                    "10. Get value distribution\n" +
                    "11. Save portfolio\n" +
                    "12. Load portfolio\n" +
                    "13. Rebalance portfolio\n" +
                    "14. Print supported instruction list\n" +
                    "15. Quit the program \n",
            out.toString());
  }

  @Test
  public void testFarewellMessage() {
    Appendable out = new StringWriter();
    View view = new StocksView(out);
    view.farewellMessage();
    assertEquals("Thank you for using this program!", out.toString());
  }

  @Test
  public void testWriteMessage() {
    Appendable out = new StringWriter();
    View view = new StocksView(out);
    view.writeMessage("Test message");
    assertEquals("Test message", out.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testWriteMessageIOException() {
    Appendable out = new Appendable() {
      @Override
      public Appendable append(CharSequence csq) throws IOException {
        throw new IOException("Forced IOException");
      }

      @Override
      public Appendable append(CharSequence csq, int start, int end) throws IOException {
        throw new IOException("Forced IOException");
      }

      @Override
      public Appendable append(char c) throws IOException {
        throw new IOException("Forced IOException");
      }
    };
    View view = new StocksView(out);
    view.writeMessage("Test message");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testView() throws ParseException, IOException {
    Appendable out = new StringWriter();
    Stock s1 = new StockImpl("GOOG", 3, 100);
    Stock s = new StockImpl("GOOG", 10, 100);
    HashMap<String, Stock> stock = new HashMap<>();
    stock.put("2024-12-05", s1);
    stock.put("2024-12-06", s);
    Portfolio portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", stock, 2, "2024-12-05");
    HashMap<String, Integer> stock1 = new HashMap<>();

    portfolio.portfolioPerformance("2024-12-05", "2024-12-06");

    View v = new StocksView(out);
    ViewMacro m = new MacroBarChartView(portfolio.portfolioPerformance("2024-12-05", "2024-12-06"));
    m.executeMacro(v);
    assertEquals("Dec 05 2024: ******\n" +
            "Dec 06 2024: ********************\n" +
            "\n" +
            "Scale: * = 1", out.toString());
  }

  @Test
  public void testView1() throws ParseException {
    Appendable out = new StringWriter();
    StockModel m = new StockModelImpl();
    Portfolio portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", api.lookUpStock("GOOG"), 2, "2020-04-02");

    View v = new StocksView(out);
    Map<String, Integer> performance =
            portfolio.portfolioPerformance("2023-01-03", "2023-03-30");
    HashMap<String, Integer> hash = new HashMap<String, Integer>();
    ViewMacro mV = new MacroBarChartView(performance);
    mV.executeMacro(v);
    assertEquals("Jan 03 2023: ***********************************\n" +
            "Jan 05 2023: **********************************\n" +
            "Jan 11 2023: *************************************\n" +
            "Jan 18 2023: ************************************\n" +
            "Jan 20 2023: ***************************************\n" +
            "Jan 24 2023: ***************************************\n" +
            "Jan 26 2023: ***************************************\n" +
            "Jan 30 2023: ***************************************\n" +
            "Feb 01 2023: ****************************************\n" +
            "Feb 03 2023: ******************************************\n" +
            "Feb 07 2023: *******************************************\n" +
            "Feb 09 2023: **************************************\n" +
            "Feb 13 2023: **************************************\n" +
            "Feb 15 2023: **************************************\n" +
            "Feb 17 2023: *************************************\n" +
            "Feb 22 2023: ************************************\n" +
            "Feb 24 2023: ***********************************\n" +
            "Feb 28 2023: ************************************\n" +
            "Mar 02 2023: *************************************\n" +
            "Mar 06 2023: **************************************\n" +
            "Mar 08 2023: *************************************\n" +
            "Mar 10 2023: ************************************\n" +
            "Mar 14 2023: *************************************\n" +
            "Mar 16 2023: ****************************************\n" +
            "Mar 20 2023: ****************************************\n" +
            "Mar 22 2023: *****************************************\n" +
            "Mar 24 2023: ******************************************\n" +
            "Mar 28 2023: ****************************************\n" +
            "Mar 30 2023: ****************************************\n" +
            "\n" +
            "Scale: * = 5", out.toString());
  }

  @Test
  public void testViewMonthYear() throws ParseException {
    Appendable out = new StringWriter();
    StockModel m = new StockModelImpl();
    Portfolio portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", api.lookUpStock("GOOG"), 2, "2015-03-03");

    View v = new StocksView(out);
    Map<String, Integer> performance = portfolio.portfolioPerformance("2018-01", "2021-12");
    Map<String, Integer> hash = new HashMap<String, Integer>();
    ViewMacro mV = new MacroBarChartView(performance);
    mV.executeMacro(v);
    assertEquals(stockPerformance, out.toString());
  }

  @Test
  public void testViewYear() throws ParseException {
    Appendable out = new StringWriter();
    StockModel m = new StockModelImpl();
    Portfolio portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", api.lookUpStock("GOOG"), 2, "2017-01-03");

    View v = new StocksView(out);
    Map<String, Integer> performance = portfolio.portfolioPerformance("2018", "2021");
    Map<String, Integer> hash = new HashMap<String, Integer>();
    ViewMacro mV = new MacroBarChartView(performance);
    mV.executeMacro(v);
    assertEquals(stockPerformance, out.toString());
  }

  @Test
  public void testView5() throws ParseException {
    Appendable out = new StringWriter();
    StockModel m = new StockModelImpl();
    Portfolio portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", api.lookUpStock("GOOG"), 2, "2017-01-03");

    View v = new StocksView(out);
    Map<String, Integer> performance = portfolio.portfolioPerformance("2018-01-02", "2021-12-31");
    Map<String, Integer> hash = new HashMap<String, Integer>();
    ViewMacro mV = new MacroBarChartView(performance);
    mV.executeMacro(v);
    assertEquals(stockPerformance, out.toString());
  }

  @Test
  public void testView2() throws ParseException {
    Appendable out = new StringWriter();
    StockModel m = new StockModelImpl();
    Portfolio portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", api.lookUpStock("GOOG"), 2, "2020-01-02");

    View v = new StocksView(out);
    Map<String, Integer> performance = portfolio.portfolioPerformance("2021", "2023");
    Map<String, Integer> hash = new HashMap<String, Integer>();
    ViewMacro mV = new MacroBarChartView(performance);
    mV.executeMacro(v);
    assertEquals("Jan 04 2021: *****************************\n" +
            "Jan 06 2021: *****************************\n" +
            "Feb 23 2021: ***********************************\n" +
            "Apr 09 2021: ***************************************\n" +
            "May 25 2021: *****************************************\n" +
            "Jul 12 2021: *********************************************\n" +
            "Aug 25 2021: *************************************************\n" +
            "Oct 11 2021: ***********************************************\n" +
            "Nov 24 2021: **************************************************\n" +
            "Jan 11 2022: ************************************************\n" +
            "Feb 28 2022: **********************************************\n" +
            "Apr 13 2022: ********************************************\n" +
            "May 31 2022: ***************************************\n" +
            "Jul 18 2022: *\n" +
            "Aug 31 2022: *\n" +
            "Oct 17 2022: *\n" +
            "Dec 01 2022: *\n" +
            "Jan 19 2023: *\n" +
            "Mar 07 2023: *\n" +
            "Apr 21 2023: *\n" +
            "Jun 07 2023: **\n" +
            "Jun 30 2023: **\n" +
            "Jul 25 2023: **\n" +
            "Aug 16 2023: **\n" +
            "Sep 08 2023: **\n" +
            "Oct 02 2023: **\n" +
            "Oct 24 2023: **\n" +
            "Nov 15 2023: **\n" +
            "Dec 08 2023: **\n" +
            "Dec 29 2023: **\n" +
            "\n" +
            "Scale: * = 116", out.toString());
  }

  @Test
  public void testView3() throws ParseException {
    Appendable out = new StringWriter();
    StockModel m = new StockModelImpl();
    Portfolio portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", api.lookUpStock("GOOG"), 2, "2017-01-03");

    View v = new StocksView(out);
    Map<String, Integer> performance = portfolio.portfolioPerformance("2018-01", "2023-12");
    Map<String, Integer> hash = new HashMap<String, Integer>();
    ViewMacro mV = new MacroBarChartView(performance);
    mV.executeMacro(v);
    assertEquals("Jan 02 2018: ******************\n" +
            "Jan 17 2018: *******************\n" +
            "Apr 19 2018: ******************\n" +
            "Jul 20 2018: ********************\n" +
            "Oct 19 2018: ******************\n" +
            "Jan 24 2019: ******************\n" +
            "Apr 26 2019: *********************\n" +
            "Jul 29 2019: *********************\n" +
            "Oct 28 2019: *********************\n" +
            "Jan 30 2020: ************************\n" +
            "May 01 2020: **********************\n" +
            "Aug 03 2020: ************************\n" +
            "Nov 02 2020: ***************************\n" +
            "Feb 04 2021: **********************************\n" +
            "May 07 2021: ****************************************\n" +
            "Aug 09 2021: **********************************************\n" +
            "Nov 08 2021: **************************************************\n" +
            "Feb 09 2022: ***********************************************\n" +
            "May 12 2022: **************************************\n" +
            "Aug 15 2022: **\n" +
            "Nov 14 2022: *\n" +
            "Dec 30 2022: *\n" +
            "Feb 16 2023: *\n" +
            "Apr 04 2023: *\n" +
            "May 19 2023: **\n" +
            "Jul 07 2023: **\n" +
            "Aug 22 2023: **\n" +
            "Oct 06 2023: **\n" +
            "Nov 21 2023: **\n" +
            "Dec 29 2023: **\n" +
            "\n" +
            "Scale: * = 118", out.toString());
  }

  @Test
  public void testView6() throws ParseException {
    Appendable out = new StringWriter();
    StockModel m = new StockModelImpl();
    Portfolio portfolio = new PortfolioImpl();
    portfolio.buyStock("GOOG", api.lookUpStock("GOOG"), 2, "2014-10-01");

    View v = new StocksView(out);
    Map<String, Integer> performance = portfolio.portfolioPerformance("2014-10", "2015");
    Map<String, Integer> hash = new HashMap<String, Integer>();
    ViewMacro mV = new MacroBarChartView(performance);
    mV.executeMacro(v);
    assertEquals("Oct 01 2014: *************************************\n" +
            "Oct 13 2014: ***********************************\n" +
            "Nov 04 2014: ************************************\n" +
            "Nov 26 2014: ************************************\n" +
            "Dec 19 2014: **********************************\n" +
            "Jan 14 2015: *********************************\n" +
            "Feb 06 2015: ***********************************\n" +
            "Mar 03 2015: **************************************\n" +
            "Mar 25 2015: *************************************\n" +
            "Apr 17 2015: **********************************\n" +
            "May 11 2015: ***********************************\n" +
            "Jun 03 2015: ************************************\n" +
            "Jun 25 2015: ***********************************\n" +
            "Jul 08 2015: **********************************\n" +
            "Jul 20 2015: ********************************************\n" +
            "Jul 30 2015: ******************************************\n" +
            "Aug 11 2015: ********************************************\n" +
            "Aug 21 2015: ****************************************\n" +
            "Sep 02 2015: ****************************************\n" +
            "Sep 15 2015: ******************************************\n" +
            "Sep 25 2015: ****************************************\n" +
            "Oct 07 2015: ******************************************\n" +
            "Oct 19 2015: ********************************************\n" +
            "Oct 29 2015: ***********************************************\n" +
            "Nov 10 2015: ************************************************\n" +
            "Nov 20 2015: **************************************************\n" +
            "Dec 03 2015: **************************************************\n" +
            "Dec 15 2015: *************************************************\n" +
            "Dec 28 2015: **************************************************\n" +
            "Dec 31 2015: **************************************************\n" +
            "\n" +
            "Scale: * = 30", out.toString());
  }
}