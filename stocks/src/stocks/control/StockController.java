package stocks.control;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import stocks.model.APICall;
import stocks.model.Portfolio;
import stocks.model.PortfolioImpl;
import stocks.model.Stock;
import stocks.view.View;
import stocks.model.StockModel;

/**
 * Implements the control to run the program.
 * Supports calculating the loss or gain of a stock
 * Supports computing the x-day moving average of a stock
 * Supports finding the x-day crossovers of a stock
 */
public class StockController implements Controller {
  protected Readable readable;
  protected Appendable appendable;
  protected View view;
  protected StockModel stockModel;
  protected Map<String, Stock[]> stocks;
  protected APICall api;

  /**
   * Creates an instance of the controller.
   *
   * @param readable the input string
   * @param model    the model containing methods to be performed
   * @param view     prints values to the user
   * @throws IllegalArgumentException if the input or output are null
   */
  public StockController(Readable readable, StockModel model, View view, APICall ap)
          throws IllegalArgumentException {
    if ((readable == null)) {
      throw new IllegalArgumentException("Readable is null");
    }
    this.readable = readable;
    this.view = view;
    this.stockModel = model;
    this.api = ap;
  }

  @Override
  public void control() throws IllegalStateException, IOException, ParseException {
    Scanner sc = new Scanner(readable);
    boolean quit = false;
    Map<String, Portfolio> portfolios = new HashMap<String, Portfolio>();

    //print the welcome message
    view.welcomeMessage();
    Map<String, Stock> stocks;
    while (!quit) {
      //continue until the user quits
      view.writeMessage("Type instruction: "); //prompt for the instruction name
      String userInstruction = sc.next(); //take an instruction name
      if (userInstruction.equals("15")) {
        quit = true;
        break;
      }
      if (!(userInstruction.equals("1") || userInstruction.equals("2")
              || userInstruction.equals("3") || userInstruction.equals("4")
              || userInstruction.equals("5") || userInstruction.equals("6")
              || userInstruction.equals("7") || userInstruction.equals("8")
              || userInstruction.equals("9") || userInstruction.equals("10")
              || userInstruction.equals("11") || userInstruction.equals("12")
              || userInstruction.equals("13") || userInstruction.equals("14"))) {
        view.writeMessage("Invalid choice. Please try again.\n");
      } else {
        completeMethod(sc, userInstruction, portfolios);
      }
    }

    view.farewellMessage();
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

  /**
   * Delegates to the model, performing the method specified by the user.
   *
   * @param sc              the user input
   * @param userInstruction the current user input
   */
  protected void completeMethod(Scanner sc,
                              String userInstruction, Map<String, Portfolio> portfolios)
          throws IOException, ParseException {
    switch (userInstruction) {
      case "1":
        handleGainOrLoss(sc, portfolios);
        break;
      case "2":
        handleXDayMovingAvg(sc, portfolios);
        break;
      case "3":
        handleCrossovers(sc, portfolios);
        break;
      case "4":
        handleCreatePortfolio(sc, portfolios);
        break;
      case "5":
        handleCalcPortfolioVal(sc, portfolios);
        break;
      case "14":
        view.writeMessage(System.lineSeparator());
        view.printMenu();
        break;
      default:
        view.writeMessage("Invalid choice. Please try again.\n");
    }
    view.writeMessage(System.lineSeparator());
    view.printMenu();
  }

  /**
   * Delegates to the gain or loss method.
   * @param sc scanner
   * @param portfolios the existing portfolios
   */
  private void handleGainOrLoss(Scanner sc, Map<String, Portfolio> portfolios) {
    String startDate = "";
    String endDate = "";
    Map<String, Stock> stocks;

    try {
      view.writeMessage("Enter ticker symbol: ");
      String symbol = sc.next();
      stocks = api.lookUpStock(symbol);
      while (checkDateFormat(startDate)) {
        view.writeMessage("\nFor start date: ");
        startDate = formatMonthDayYear(sc);
        if (checkDateFormat(startDate)) {
          view.writeMessage("Incorrect start date try again.\n");
        }
      }
      while (checkDateFormat(endDate)) {
        view.writeMessage("\nFor end date: ");
        endDate = formatMonthDayYear(sc);
        if (checkDateFormat(endDate)) {
          view.writeMessage("Incorrect end date try again.\n");
        }
      }
      String result = String.format("$%.2f", stockModel.gainOrLoss(stocks, startDate, endDate));
      view.writeMessage(result + System.lineSeparator());
    } catch (IllegalArgumentException e) {
      view.writeMessage("Error: " + e.getMessage() + System.lineSeparator());
    }
  }

  /**
   * Delegates to the x-day moving average method.
   * @param sc scanner
   * @param portfolios the existing portfolios
   */
  private void handleXDayMovingAvg(Scanner sc, Map<String, Portfolio> portfolios) {
    String startDate = "";
    Map<String, Stock> stocks;
    int x;

    try {
      view.writeMessage("Enter ticker symbol: ");
      String symbol = sc.next();
      stocks = api.lookUpStock(symbol);
      while (checkDateFormat(startDate)) {
        startDate = formatMonthDayYear(sc);
        if (checkDateFormat(startDate)) {
          view.writeMessage("Incorrect start date try again.\n");
        }
      }
      view.writeMessage("Enter x value for moving average: ");
      x = sc.nextInt();
      String result = String.format("$%.2f", stockModel.xDayMovingAverage(stocks, startDate, x));
      view.writeMessage(result + System.lineSeparator());
    } catch (IllegalArgumentException e) {
      view.writeMessage("Error: " + e.getMessage() + System.lineSeparator());
    }
  }

  /**
   * Delegates to the crossovers method.
   * @param sc scanner
   * @param portfolios the existing portfolios
   */
  private void handleCrossovers(Scanner sc, Map<String, Portfolio> portfolios) {
    String startDate = "";
    String endDate = "";
    Map<String, Stock> stocks;
    int x;

    try {
      view.writeMessage("Enter ticker symbol: ");
      String symbol = sc.next();
      stocks = api.lookUpStock(symbol);
      while (checkDateFormat(startDate)) {
        view.writeMessage("\nFor start date: ");
        startDate = formatMonthDayYear(sc);
        if (checkDateFormat(startDate)) {
          view.writeMessage("Incorrect start date try again.\n");
        }
      }
      while (checkDateFormat(endDate)) {
        view.writeMessage("\nFor end date: ");
        endDate = formatMonthDayYear(sc);
        if (checkDateFormat(endDate)) {
          view.writeMessage("Incorrect end date try again.\n");
        }
      }
      view.writeMessage("Enter x value for moving average: ");
      x = sc.nextInt();
      view.writeMessage(stockModel.crossovers(stocks, startDate, endDate, x) +
              System.lineSeparator());
    } catch (IllegalArgumentException e) {
      view.writeMessage("Error: " + e.getMessage() + System.lineSeparator());
    }
  }

  /**
   * Delegates to the create portfolio method.
   * @param sc scanner
   * @param portfolios the existing portfolios
   */
  private void handleCreatePortfolio(Scanner sc, Map<String, Portfolio> portfolios) {
    try {
      view.writeMessage("Enter portfolio name: ");
      String name = sc.next();
      Portfolio p;

      if (portfolios.containsKey(name)) {
        view.writeMessage("Portfolio already exists." + System.lineSeparator());
      } else {
        p = new PortfolioImpl();
        portfolios.put(name, p);
        view.writeMessage("Portfolio successfully created." + System.lineSeparator());
      }
    } catch (IllegalArgumentException e) {
      view.writeMessage("Error: " + e.getMessage() + System.lineSeparator());
    }
  }

  /**
   * Delegates to the calculate portfolio value method.
   * @param sc scanner
   * @param portfolios the existing portfolios
   */
  private void handleCalcPortfolioVal(Scanner sc, Map<String, Portfolio> portfolios) {
    String startDate = "";

    view.writeMessage("Enter portfolio name to calculate value of: ");
    String name = sc.next();
    if (!portfolios.containsKey(name)) {
      view.writeMessage("Portfolio does not exist.\n");
    } else {
      while (checkDateFormat(startDate)) {
        startDate = formatMonthDayYear(sc);
        if (checkDateFormat(startDate)) {
          view.writeMessage("Incorrect start date try again.\n");
        }
      }
      try {
        Portfolio p = portfolios.get(name);
        view.writeMessage("Value of portfolio: $" + String.format("%.2f", p.calcVal(startDate))
                + System.lineSeparator());
      } catch (IllegalArgumentException e) {
        view.writeMessage("Error: " + e.getMessage() + System.lineSeparator());
      }
    }
  }

  /**
   * Formats the month, date, year of the chosen date.
   * @param sc scanner
   * @return the formatted date
   */
  protected String formatMonthDayYear(Scanner sc) {
    view.writeMessage("\nEnter month (MM): ");
    String month = sc.next();
    try {
      month = String.format("%02d", Integer.parseInt(month));
    } catch (NumberFormatException e) {
      view.writeMessage("Error: " + e.getMessage() + System.lineSeparator());
    }

    view.writeMessage("Enter day (DD): ");
    String day = sc.next();
    try {
      day = String.format("%02d", Integer.parseInt(day));
    } catch (NumberFormatException e) {
      view.writeMessage("Error: " + e.getMessage() + System.lineSeparator());
    }

    view.writeMessage("Enter year (YYYY): ");
    String year = sc.next();

    try {
      Integer.parseInt(year);
    } catch (NumberFormatException e) {
      view.writeMessage("Error: " + e.getMessage() + System.lineSeparator());
    }

    return year + "-" + month + "-" + day;
  }
}
