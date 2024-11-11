import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;

import stocks.control.MacroStockController;
import stocks.control.StockController;
import stocks.model.APICall;
import stocks.model.AlphaVantAPI;
import stocks.model.StockModelImpl;
import stocks.model.StockModel;
import stocks.view.StocksView;
import stocks.view.View;
import stocks.control.Controller;
import stocks.view.MacroStocksView;

import static org.junit.Assert.assertEquals;

/**
 * Tests the controller, ensuring it delegates to the correct methods.
 */
public class StockControllerTest {
  APICall api = new AlphaVantAPI();
  String oldMenu = "\n1. Calculate gain/loss of a stock\n" +
          "2. Calculate x-day moving average of a stock\n" +
          "3. Find x-day crossovers for a stock\n" +
          "4. Create portfolio and calculate its value\n" +
          "5. Print supported instruction list\n" +
          "6. Quit the program \n";

  String menu = "\n1. Calculate gain/loss of a stock\n" +
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
          "15. Quit the program \n";

  String oldWelcomeMessage = "Welcome to the stocks program!\n"
          + "1. Calculate gain/loss of a stock\n" +
          "2. Calculate x-day moving average of a stock\n" +
          "3. Find x-day crossovers for a stock\n" +
          "4. Create portfolio and calculate its value\n" +
          "5. Print supported instruction list\n" +
          "6. Quit the program \n";

  String welcomeMessage = "Welcome to the stocks program!\n"
          + "1. Calculate gain/loss of a stock\n" +
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
          "15. Quit the program \n";

  String farewellMessage = "Thank you for using this program!";

  @Test
  public void testGainOrLoss() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("1 GOOG 05 29 2024 05 31 2024 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);

    Controller stockController = new StockController(input, model, view, api);
    stockController.control();

    assertEquals( oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "For start date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): \n" +
            "For end date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): $-3.44\n" +
            oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testGainOrLossMSFT() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("1 MSFT 05 29 2024 05 31 2024 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);
    Controller stockController = new StockController(input, model, view, api);
    stockController.control();
    assertEquals(oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "For start date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): \n" +
            "For end date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): $-14.04\n" + oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testGainOrLossInvalidStartDate() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("1 GOOG 12 29 2025 05 31 2024 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);

    Controller stockController = new StockController(input, model, view, api);
    stockController.control();

    assertEquals( oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "For start date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): \n" +
            "For end date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Error: Date does not exist\n" +
            oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testGainOrLossInvalidEndDate() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("1 GOOG 05 31 2024 05 31 2025 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);

    Controller stockController = new StockController(input, model, view, api);
    stockController.control();

    assertEquals( oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "For start date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): \n" +
            "For end date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Error: Date does not exist\n" +
            oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testGainOrLossInvalidDateRange() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("1 GOOG 05 31 2024 05 30 2024 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);

    Controller stockController = new StockController(input, model, view, api);
    stockController.control();

    assertEquals( oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "For start date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): \n" +
            "For end date: \n" +
            "Enter month (MM): Enter day (DD): " +
            "Enter year (YYYY): Error: Start date cannot be after end date\n" +
            oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testGainOrLossIncorrectDateFormat() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("1 GOOG 5 31 2024 05 29 2024 05 30 2024 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);

    Controller stockController = new StockController(input, model, view, api);
    stockController.control();

    assertEquals( oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "For start date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): \n" +
            "For end date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): " +
            "Error: Start date cannot be after end date\n" +
            oldMenu +
            "Type instruction: Invalid choice. Please try again.\n" +
            "Type instruction: Invalid choice. Please try again.\n" +
            "Type instruction: Invalid choice. Please try again.\n" +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testGainOrLossIncorrectDateFormat1() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("1 GOOG 05 31 202 05 31 2020 01 21 2022 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);

    Controller stockController = new StockController(input, model, view, api);
    stockController.control();

    assertEquals( oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "For start date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): " +
            "Incorrect start date try again.\n" +
            "\n" +
            "For start date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): \n" +
            "For end date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Error: Date does not exist\n" +
            oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testGainOrLossIncorrectDateFormat2() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("1 GOOG 05 1 2024 05 30 2024 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);

    Controller stockController = new StockController(input, model, view, api);
    stockController.control();

    assertEquals( oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "For start date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): \n" +
            "For end date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): $7.99\n" +
            oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testGainOrLossIncorrectDateFormat3() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("1 GOOG 05 31 2024 5 30 2024 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);

    Controller stockController = new StockController(input, model, view, api);
    stockController.control();

    assertEquals( oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "For start date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): \n" +
            "For end date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): " +
            "Error: Start date cannot be after end date\n" +
            oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testGainOrLossIncorrectDateFormat4() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("1 GOOG 05 31 2024 05 30 204 5 30 2024 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);

    Controller stockController = new StockController(input, model, view, api);
    stockController.control();

    assertEquals( oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "For start date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): \n" +
            "For end date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Incorrect end date try again.\n" +
            "\n" +
            "For end date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Error: " +
            "Start date cannot be after end date\n" +
            oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testGainOrLossIncorrectFileType() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);

    Controller stockController = new StockController(input, model, view, api);
    stockController.control();

    assertEquals( oldWelcomeMessage + "Type instruction: "
            + farewellMessage, output.toString());
  }

  @Test
  public void testXMovingDayAvg() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("2 GOOG 05 23 2024 3 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);

    Controller stockController = new StockController(input, model, view, api);
    stockController.control();

    assertEquals( oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): " +
            "Enter x value for moving average: $177.53\n" +
            oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testXMovingDayAvgInvalidDate() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("2 GOOG 05 23 2025 3 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);

    Controller stockController = new StockController(input, model, view, api);
    stockController.control();

    assertEquals( oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): " +
            "Enter x value for moving average: Error: Date does not exist\n" +
            oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testXMovingDayAvgInvalidX() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("2 GOOG 05 23 2024 -3 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);

    Controller stockController = new StockController(input, model, view, api);
    stockController.control();

    assertEquals( oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): " +
            "Enter x value for moving average: Error: X value cannot be negative\n" +
            oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testCrossovers() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("3 GOOG 05 02 2024 05 03 2024 2 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);

    Controller stockController = new StockController(input, model, view, api);
    stockController.control();

    assertEquals( oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "For start date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): \n" +
            "For end date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Enter x value for moving " +
            "average: [2024-05-02, 2024-05-03]\n" + oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testCrossoversMSFT() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("3 MSFT 05 02 2024 05 03 2024 2 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);
    Controller stockController = new StockController(input, model, view, api);
    stockController.control();
    assertEquals(oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "For start date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): \n" +
            "For end date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Enter x value for " +
            "moving average: [2024-05-02, 2024-05-03]\n" + oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testCrossoversInvalidStart() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("3 GOOG 05 02 2025 05 03 2024 2 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);

    Controller stockController = new StockController(input, model, view, api);
    stockController.control();

    assertEquals( oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "For start date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): \n" +
            "For end date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Enter x value " +
            "for moving average: Error: Date does not exist\n" + oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testCrossoversInvalidX() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("3 GOOG 05 02 2024 05 03 2024 -2 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);

    Controller stockController = new StockController(input, model, view, api);
    stockController.control();

    assertEquals( oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "For start date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): \n" +
            "For end date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Enter x value for " +
            "moving average: Error: X value cannot be negative\n" + oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testCrossoversInvalidEnd() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("3 GOOG 05 02 2024 05 03 2025 2 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);

    Controller stockController = new StockController(input, model, view, api);
    stockController.control();

    assertEquals( oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "For start date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): \n" +
            "For end date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Enter x value " +
            "for moving average: Error: Date does not exist\n" + oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testCrossoversInvalidDateRange() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("3 GOOG 05 02 2024 05 03 2023 2 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);

    Controller stockController = new StockController(input, model, view, api);
    stockController.control();

    assertEquals( oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "For start date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): \n" +
            "For end date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Enter x " +
            "value for moving average: Error: Start date cannot be after end date\n" + oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testInvalidInstruction() throws IOException, ParseException {
    Appendable out = new StringWriter();
    Readable in = new StringReader("20 GOOG 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(out);
    Controller controller = new StockController(in, model, view, api);

    controller.control();
    assertEquals( oldWelcomeMessage +
            "Type instruction: Invalid choice. Please try again.\n" +
            "Type instruction: Invalid choice. Please try again.\n" +
            "Type instruction: " + farewellMessage, out.toString());
  }

  @Test
  public void testInvalidNum() throws IOException, ParseException {
    Appendable out = new StringWriter();
    Readable in = new StringReader("20 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(out);
    Controller controller = new StockController(in, model, view, api);

    controller.control();
    assertEquals( oldWelcomeMessage +
            "Type instruction: Invalid choice. Please try again.\n" +
            "Type instruction: " + farewellMessage, out.toString());
  }

  @Test
  public void testXDayMovingAvgAAPL() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("2 AAPL 05 23 2023 3 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);
    Controller stockController = new StockController(input, model, view, api);
    stockController.control();
    assertEquals(oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): " +
            "Enter x value for moving average: $173.64\n" + oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testInvalidDateFormat() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("1 GOOG 05 32 2024 05 31 2024 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);
    Controller stockController = new StockController(input, model, view, api);
    stockController.control();
    assertEquals(oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "For start date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): \n" +
            "For end date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Error: Date does not exist\n" +
            oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testNonExistentStockSymbol() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("1 FAKE 05 29 2024 05 31 2024 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);
    Controller stockController = new StockController(input, model, view, api);
    stockController.control();
    assertEquals(oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: Error: File doesn't exist\n" +
            oldMenu +
            "Type instruction: Invalid choice. Please try again.\n" +
            "Type instruction: Invalid choice. Please try again.\n" +
            "Type instruction: Invalid choice. Please try again.\n" +
            "Type instruction: Invalid choice. Please try again.\n" +
            "Type instruction: Invalid choice. Please try again.\n" +
            "Type instruction: Invalid choice. Please try again.\n" +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testDistantPastDate() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("1 GOOG 01 01 1980 01 02 1980 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);
    Controller stockController = new StockController(input, model, view, api);
    stockController.control();
    assertEquals(oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "For start date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): \n" +
            "For end date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Error: Date does not exist\n" +
            oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testFutureDate() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("1 GOOG 01 01 2050 01 02 2050 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);
    Controller stockController = new StockController(input, model, view, api);
    stockController.control();
    assertEquals(oldWelcomeMessage +
            "Type instruction: Enter ticker symbol: \n" +
            "For start date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): \n" +
            "For end date: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Error: Date does not exist\n" +
            oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testPortfolio() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("4 rach 5 rach 06 03 2024 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);
    Controller stockController = new StockController(input, model, view, api);
    stockController.control();
    assertEquals(oldWelcomeMessage +
            "Type instruction: Enter portfolio name: Portfolio successfully created.\n" + oldMenu +
            "Type instruction: " +
            "Enter portfolio name to calculate value of: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Error: Date does not exist.\n" +
            oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testPortfolioAddToExistingPortfolio() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("4 rach 7 rach GOOG 5 06 02 2024 5 rach 06 03 2024 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);
    Controller stockController = new MacroStockController(input, model, view, api);
    stockController.control();
    assertEquals(oldWelcomeMessage +
            "Type instruction: Enter portfolio name: " +
            "Portfolio successfully created.\n" + oldMenu +
            "Type instruction: Enter portfolio name: Enter ticker symbol: Enter quantity: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Error: Date does not exist.\n" +
            oldMenu + "Type instruction: Enter portfolio name to calculate value of: \n"
            + "Enter month (MM): Enter day (DD): Enter year (YYYY): " +
            "Error: Date does not exist.\n" + oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testPortfolioCalcValOfNonExistingPortfolio() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("5 rach 15");
    StockModel model = new StockModelImpl();
    View view = new StocksView(output);
    Controller stockController = new StockController(input, model, view, api);
    stockController.control();
    assertEquals(oldWelcomeMessage +
            "Type instruction: Enter portfolio name to calculate value of: " +
            "Portfolio does not exist.\n" +
            oldMenu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testPortfolioBuyDecimalStockAmount() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("4 rach 7 rach GOOG 2.5 15");
    StockModel model = new StockModelImpl();
    View view = new MacroStocksView(output);
    Controller stockController = new MacroStockController(input, model, view, api);
    stockController.control();
    assertEquals(welcomeMessage +
            "Type instruction: Enter portfolio name: Portfolio successfully created.\n" + menu +
            "Type instruction: Enter portfolio name: Enter ticker symbol: Enter quantity: " +
            "Error: null\n" + menu +
            "Type instruction: Invalid choice. Please try again.\n" +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testSellShares() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("8 test_portfolio.txt.txt GOOG 2 12 05 2023 15");
    StockModel model = new StockModelImpl();
    View view = new MacroStocksView(output);

    Controller stockController = new MacroStockController(input, model, view, api);
    stockController.control();

    assertEquals( welcomeMessage +
            "Type instruction: Enter portfolio name: Enter ticker symbol: Enter quantity: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Shares sold successfully.\n" +
            menu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testSellSharesNoDate() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("8 test_portfolio.txt GOOG 2 12 15 2024 15");
    StockModel model = new StockModelImpl();
    View view = new MacroStocksView(output);

    Controller stockController = new MacroStockController(input, model, view, api);
    stockController.control();

    assertEquals( welcomeMessage +
            "Type instruction: Enter portfolio name: Enter ticker symbol: Enter quantity: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): " +
            "Error: Date does not exist.\n" + menu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testBuyShares() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("7 test_portfolio.txt GOOG 2 12 05 2023 15");
    StockModel model = new StockModelImpl();
    View view = new MacroStocksView(output);

    Controller stockController = new MacroStockController(input, model, view, api);
    stockController.control();

    assertEquals( welcomeMessage +
            "Type instruction: Enter portfolio name: Enter ticker symbol: Enter quantity: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Shares bought successfully.\n" +
            menu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testBuySharesNone() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("7 test_portfolio.txt GOOG 2 12 05 2023 15");
    StockModel model = new StockModelImpl();
    View view = new MacroStocksView(output);

    Controller stockController = new MacroStockController(input, model, view, api);
    stockController.control();

    assertEquals( welcomeMessage +
            "Type instruction: Enter portfolio name: Enter ticker symbol: Enter quantity: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Shares bought successfully.\n" +
            menu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testGetComposition() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("9 test_portfolio.txt.txt 12 05 2023 15");
    StockModel model = new StockModelImpl();
    View view = new MacroStocksView(output);

    Controller stockController = new MacroStockController(input, model, view, api);
    stockController.control();

    assertEquals( welcomeMessage +
            "Type instruction: Enter portfolio name: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Composition on 2023-12-05:\n" +
            "GOOG: 25.0 shares\n" + menu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testGetValueDistribution() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("10 test_portfolio_for_val_distr.txt 12 05 2023 15");
    StockModel model = new StockModelImpl();
    View view = new MacroStocksView(output);

    Controller stockController = new MacroStockController(input, model, view, api);
    stockController.control();

    assertEquals( welcomeMessage +
            "Type instruction: Enter portfolio name: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Value distribution on " +
            "2023-12-05:\n" +
            "GOOG: $15.00\n" +
            "RACH: $6.00\n" +
            "Total value: $21.00\n" + menu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testSavePortfolio() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("11 r testPortfolio.txt 15");
    StockModel model = new StockModelImpl();
    View view = new MacroStocksView(output);

    Controller stockController = new MacroStockController(input, model, view, api);
    stockController.control();

    assertEquals( welcomeMessage +
            "Type instruction: Enter portfolio name: Saving an empty portfolio." +
            "Enter filename to save: Portfolio saved successfully.\n" + menu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testLoadPortfolio() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("12 testPortfolio testPortfolio.txt 15");
    StockModel model = new StockModelImpl();
    View view = new MacroStocksView(output);

    Controller stockController = new MacroStockController(input, model, view, api);
    stockController.control();

    assertEquals( welcomeMessage +
            "Type instruction: Enter portfolio name: Enter filename to load: " +
            "Portfolio loaded successfully.\n" + menu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void loadPort1() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("12 r rebalance_test_copy.txt 15");
    StockModel model = new StockModelImpl();
    View view = new MacroStocksView(output);

    Controller stockController = new MacroStockController(input, model, view, api);
    stockController.control();

    assertEquals( welcomeMessage +
            "Type instruction: Enter portfolio name: Enter filename to load: " +
            "Portfolio loaded successfully.\n" + menu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void buyStocks() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("4 rach 7 rach GOOG 5 06 06 2024 9 rach 06 06 2024 9 " +
            "rach 06 07 2024 15");
    StockModel model = new StockModelImpl();
    View view = new MacroStocksView(output);

    Controller stockController = new MacroStockController(input, model, view, api);
    stockController.control();

    assertEquals( welcomeMessage +
            "Type instruction: Enter portfolio name: Portfolio successfully created.\n" + menu +
            "Type instruction: Enter portfolio name: Enter ticker symbol: Enter quantity: \n" +
                    "Enter month (MM): Enter day (DD): Enter year (YYYY): Shares bought " +
            "successfully.\n" + menu +
            "Type instruction: Enter portfolio name: \n" +
                    "Enter month (MM): Enter day (DD): Enter year (YYYY): Composition on " +
            "2024-06-06:\n" +
                    "GOOG: 5.0 shares\n" + menu + "Type instruction: Enter portfolio name: \n" +
                    "Enter month (MM): Enter day (DD): Enter year (YYYY): Composition on " +
            "2024-06-07:\n" +
                    "GOOG: 5.0 shares\n" + menu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testBarChart() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("6 GOOG a 2020 a 2022 15");
    StockModel model = new StockModelImpl();
    View view = new MacroStocksView(output);

    Controller stockController = new MacroStockController(input, model, view, api);
    stockController.control();

    assertEquals( welcomeMessage +
            "Type instruction: Enter portfolio name or stock ticker: \n" +
            "For start date:\n" +
            "Select date format\n" +
            "A. YYYY\n" +
            "B. YYYY-MM\n" +
            "C. YYYY-MM-DD\n" +
            "\n" +
            "Enter year: \n" +
            "For end date:\n" +
            "Select date format\n" +
            "A. YYYY\n" +
            "B. YYYY-MM\n" +
            "C. YYYY-MM-DD\n" +
            "\n" +
            "Enter year: Performance of portfolio GOOG from 2020 to 2022\n" +
            "Aug 18 2021: **********************************************\n" +
            "Aug 24 2021: ************************************************\n" +
            "Sep 16 2021: ************************************************\n" +
            "Oct 08 2021: ***********************************************\n" +
            "Nov 01 2021: ************************************************\n" +
            "Nov 23 2021: *************************************************\n" +
            "Dec 16 2021: *************************************************\n" +
            "Jan 10 2022: **********************************************\n" +
            "Feb 02 2022: **************************************************\n" +
            "Feb 25 2022: *********************************************\n" +
            "Mar 21 2022: **********************************************\n" +
            "Apr 12 2022: *******************************************\n" +
            "May 05 2022: ***************************************\n" +
            "May 27 2022: **************************************\n" +
            "Jun 22 2022: *************************************\n" +
            "Jul 15 2022: **************************************\n" +
            "Aug 08 2022: **\n" +
            "Aug 18 2022: **\n" +
            "Aug 30 2022: *\n" +
            "Sep 12 2022: *\n" +
            "Sep 22 2022: *\n" +
            "Oct 04 2022: *\n" +
            "Oct 14 2022: *\n" +
            "Oct 26 2022: *\n" +
            "Nov 07 2022: *\n" +
            "Nov 17 2022: *\n" +
            "Nov 30 2022: *\n" +
            "Dec 12 2022: *\n" +
            "Dec 22 2022: *\n" +
            "Dec 30 2022: *\n" +
            "\n" +
            "Scale: * = 59\n" + menu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testBarChartSameIncorrectMonth() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("6 GOOG b 0- 2022 01 2022 a 2023 15");
    StockModel model = new StockModelImpl();
    View view = new MacroStocksView(output);

    Controller stockController = new MacroStockController(input, model, view, api);
    stockController.control();

    assertEquals( welcomeMessage +
            "Type instruction: Enter portfolio name or stock ticker: \n" +
            "For start date:\n" +
            "Select date format\n" +
            "A. YYYY\n" +
            "B. YYYY-MM\n" +
            "C. YYYY-MM-DD\n" +
            "\n" +
            "Enter month: Error: For input string: \"0-\"\n" +
            "Enter year: Incorrect start date try again.\n" +
            "\n" +
            "Enter month: Enter year: \n" +
            "For end date:\n" +
            "Select date format\n" +
            "A. YYYY\n" +
            "B. YYYY-MM\n" +
            "C. YYYY-MM-DD\n" +
            "\n" +
            "Enter year: Performance of portfolio GOOG from 2022-01 to 2023\n" +
            "Jan 03 2022: **************************************************\n" +
            "Jan 18 2022: ***********************************************\n" +
            "Mar 04 2022: **********************************************\n" +
            "Apr 20 2022: *********************************************\n" +
            "Jun 06 2022: *****************************************\n" +
            "Jun 29 2022: ***************************************\n" +
            "Jul 22 2022: *\n" +
            "Aug 15 2022: **\n" +
            "Sep 07 2022: *\n" +
            "Sep 29 2022: *\n" +
            "Oct 21 2022: *\n" +
            "Nov 14 2022: *\n" +
            "Dec 07 2022: *\n" +
            "Dec 30 2022: *\n" +
            "Jan 25 2023: *\n" +
            "Feb 16 2023: *\n" +
            "Mar 13 2023: *\n" +
            "Apr 04 2023: *\n" +
            "Apr 27 2023: *\n" +
            "May 19 2023: **\n" +
            "Jun 13 2023: **\n" +
            "Jul 07 2023: **\n" +
            "Jul 31 2023: **\n" +
            "Aug 22 2023: **\n" +
            "Sep 14 2023: **\n" +
            "Oct 06 2023: **\n" +
            "Oct 30 2023: **\n" +
            "Nov 21 2023: **\n" +
            "Dec 14 2023: **\n" +
            "Dec 29 2023: **\n" +
            "\n" +
            "Scale: * = 57\n" + menu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void sellStocks() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("4 rach 7 rach GOOG 5 06 06 2024 8 rach GOOG 3 06 07 " +
            "2024 15 9 rach 06 07 2024 15");
    StockModel model = new StockModelImpl();
    View view = new MacroStocksView(output);

    Controller stockController = new MacroStockController(input, model, view, api);
    stockController.control();

    assertEquals( welcomeMessage +
            "Type instruction: Enter portfolio name: Portfolio successfully created.\n" + menu +
            "Type instruction: Enter portfolio name: Enter ticker symbol: Enter quantity: \n" +
                    "Enter month (MM): Enter day (DD): Enter year (YYYY): Shares bought " +
            "successfully.\n" + menu +
            "Type instruction: Enter portfolio name: Enter ticker symbol: Enter quantity: \n" +
                    "Enter month (MM): Enter day (DD): Enter year (YYYY): Shares sold " +
            "successfully.\n" + menu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void testRebalance() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("4 rach 7 rach GOOG 5 06 06 2024 " +
            "7 rach AAPL 10 06 07 2024 13 rach 06 10 2024 .5 .5 9 rach 06 10 2024 15");

    StockModel model = new StockModelImpl();
    View view = new MacroStocksView(output);

    Controller stockController = new MacroStockController(input, model, view, api);
    stockController.control();

    assertEquals( welcomeMessage +
            "Type instruction: Enter portfolio name: Portfolio successfully created.\n" + menu +
            "Type instruction: Enter portfolio name: Enter ticker symbol: Enter quantity: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Shares bought " +
            "successfully.\n" + menu +
            "Type instruction: Enter portfolio name: Enter ticker symbol: Enter quantity: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Shares bought " +
            "successfully.\n" + menu +
            "Type instruction: Enter portfolio name: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Enter target distribution " +
            "for AAPL: Enter target distribution for GOOG: Portfolio rebalanced successfully.\n" +
            menu + "Type instruction: Enter portfolio name: \n" +
                    "Enter month (MM): Enter day (DD): Enter year (YYYY): " +
            "Composition on 2024-06-10:\n" +
                    "GOOG: 7.96679499518768 shares\n" +
                    "AAPL: 7.286531690140844 shares\n" + menu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void rebalancePortfolio() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("12 rachel rebalance_test.txt 13 rachel 12 06 " +
            "2023 .25 .25 .25 .25 10 rachel 12 06 2023 15");

    StockModel model = new StockModelImpl();
    View view = new MacroStocksView(output);

    Controller stockController = new MacroStockController(input, model, view, api);
    stockController.control();

    assertEquals(welcomeMessage + "Type instruction: Enter portfolio name: " +
            "Enter filename to load: Portfolio loaded successfully.\n" + menu +
            "Type instruction: Enter portfolio name: \n" +
                    "Enter month (MM): Enter day (DD): Enter year (YYYY): " +
            "Enter target distribution for Apple: Enter target distribution for Google: " +
            "Enter target distribution for Microsoft: Enter target distribution for Netflix: " +
            "Portfolio rebalanced successfully.\n" + menu +
            "Type instruction: Enter portfolio name: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Value distribution " +
            "on 2023-12-06:\n" +
            "Netflix: $268.75\n" +
            "Google: $268.75\n" +
            "Apple: $268.75\n" +
            "Microsoft: $268.75\n" +
            "Total value: $1075.00\n" + menu +
            "Type instruction: " + farewellMessage, output.toString());
  }

  @Test
  public void rebalancePortfolioAgain() throws IOException, ParseException {
    Appendable output = new StringWriter();
    Readable input = new StringReader("12 rachel rebalance_test.txt 13 rachel 12 06 " +
            "2023 .4 .2 .2 .2 10 rachel 12 06 2023 15");

    StockModel model = new StockModelImpl();
    View view = new MacroStocksView(output);

    Controller stockController = new MacroStockController(input, model, view, api);
    stockController.control();

    assertEquals(welcomeMessage + "Type instruction: Enter portfolio name: " +
            "Enter filename to load: Portfolio loaded successfully.\n" + menu +
            "Type instruction: Enter portfolio name: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): " +
            "Enter target distribution for Apple: Enter target distribution for Google: " +
            "Enter target distribution for Microsoft: Enter target distribution for Netflix: " +
            "Portfolio rebalanced successfully.\n" + menu +
            "Type instruction: Enter portfolio name: \n" +
            "Enter month (MM): Enter day (DD): Enter year (YYYY): Value distribution " +
            "on 2023-12-06:\n" +
            "Netflix: $215.00\n" +
            "Google: $215.00\n" +
            "Apple: $430.00\n" +
            "Microsoft: $215.00\n" +
            "Total value: $1075.00\n" + menu +
            "Type instruction: " + farewellMessage, output.toString());
  }
}