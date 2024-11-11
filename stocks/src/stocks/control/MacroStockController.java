package stocks.control;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import stocks.model.APICall;
import stocks.model.Portfolio;
import stocks.model.PortfolioImpl;
import stocks.model.Stock;
import stocks.model.StockModel;
import stocks.view.MacroBarChartView;
import stocks.view.View;
import stocks.view.ViewMacro;

/**
 * Adds several methods to the controller.
 * The controller now supports finding the composition, value, and distribution of a portfolio
 * Supports saving an existing portfolio and retrieving a saved portfolio
 * Supports balancing an existing portfolio
 * Supports plotting a bar chart to plot the portfolio or stock performance
 */
public class MacroStockController extends StockController {
  /**
   * Creates an instance of the controller.
   *
   * @param readable the input string
   * @param model    the model containing methods to be performed
   * @param view     prints values to the user
   * @throws IllegalArgumentException if the input or output are null
   */
  public MacroStockController(Readable readable, StockModel model, View view, APICall ap)
          throws IllegalArgumentException {
    super(readable, model, view, ap);
  }

  @Override
  protected void completeMethod(Scanner sc, String userInstruction, Map<String, Portfolio>
          portfolios) throws IOException, ParseException {
    switch (userInstruction) {
      case "6":
        handleCreateBarChart(sc, portfolios);
        view.writeMessage(System.lineSeparator());
        view.printMenu();
        break;
      case "7": // Buy shares
        handleBuyShares(sc, portfolios);
        view.writeMessage(System.lineSeparator());
        view.printMenu();
        break;
      case "8": // Sell shares
        handleSellShares(sc, portfolios);
        view.writeMessage(System.lineSeparator());
        view.printMenu();
        break;
      case "9": // Get portfolio composition
        handleGetComposition(sc, portfolios);
        view.writeMessage(System.lineSeparator());
        view.printMenu();
        break;
      case "10": // Get value distribution
        handleGetValueDistribution(sc, portfolios);
        view.writeMessage(System.lineSeparator());
        view.printMenu();
        break;
      case "11": // Save portfolio
        handleSavePortfolio(sc, portfolios);
        view.writeMessage(System.lineSeparator());
        view.printMenu();
        break;
      case "12": // Load portfolio
        handleLoadPortfolio(sc, portfolios);
        view.writeMessage(System.lineSeparator());
        view.printMenu();
        break;
      case "13": // Rebalance portfolio
        handleRebalancePortfolio(sc, portfolios);
        view.writeMessage(System.lineSeparator());
        view.printMenu();
        break;
      default:
        super.completeMethod(sc, userInstruction, portfolios);
    }
  }

  /**
   * Turns the date produced by the user into a string of the format YYYY-MM-DD.
   * @param choice the format the user wants
   * @param sc the scanner
   * @param date the date chosen by the user
   * @return the date in a string formatted YYYY-MM-DD
   */
  private String date(String choice, Scanner sc, String date) {
    while (!checkDateFormatForPortfolioPerformance(date)) {
      if (choice.equals("c")) {
        date = formatMonthDayYear(sc);
      } else if (choice.equals("b")) {
        date = formatMonthYear(sc);
      } else {
        view.writeMessage("\nEnter year: ");
        date = sc.next();
        try {
          Integer.parseInt(date);
        } catch (NumberFormatException e) {
          view.writeMessage("Error: " + e.getMessage() + System.lineSeparator());
        }
      }
      if (!checkDateFormatForPortfolioPerformance(date)) {
        view.writeMessage("Incorrect start date try again.\n");
      }
    }
    return date;
  }

  /**
   * Delegates to the create bar chart method.
   * @param sc the scanner
   * @param portfolios the portfolios the user has created
   * @throws ParseException if the date is incorrect format
   */
  private void handleCreateBarChart(Scanner sc, Map<String, Portfolio> portfolios)
          throws ParseException {
    String name;
    String startDate = "";
    String endDate = "";

    view.writeMessage("Enter portfolio name or stock ticker: ");
    name = sc.next();
    Portfolio p = portfolios.get(name);

    if (p == null) {
      p = new PortfolioImpl();
      try {
        p.loadFromFile(name);
        portfolios.put(name, p);
      } catch (IllegalArgumentException | IOException e) {
        Map<String, Stock> sM = api.lookUpStock(name);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] sDates = sM.keySet().toArray(new String[sM.size()]);
        String firstDate = "";
        for (int i = 0; i + 1 < sDates.length - 1; i++) {
          if (sdf.parse(sDates[i]).before(sdf.parse(sDates[i + 1]))) {
            firstDate = sDates[i];
          }
        }
        p.buyStock(name, sM, 1, firstDate);
        portfolios.put(name, p);
      }
    }
    view.writeMessage("\nFor start date:\n");
    String choice = dateFormat(sc);
    startDate = date(choice, sc, startDate);

    view.writeMessage("\nFor end date:\n");
    String choiceForEnd = dateFormat(sc);

    endDate = date(choiceForEnd, sc, endDate);

    try {
      try {
        Map<String, Integer> pPerform = p.portfolioPerformance(startDate, endDate);
        view.writeMessage("Performance of portfolio " + name + " from " + startDate + " to "
                + endDate + System.lineSeparator());
        ViewMacro m = new MacroBarChartView(pPerform);
        m.executeMacro(view);
        view.writeMessage(System.lineSeparator());
      } catch (IllegalArgumentException e) {
        view.writeMessage("Error: " + e.getMessage() + System.lineSeparator());
      }
    } catch (ParseException e) {
      view.writeMessage("Error: " + e.getMessage() + System.lineSeparator());
    }
  }

  /**
   * Formats the month and year string.
   * @param sc scanner
   * @return the string formatted YYYY-MM-DD
   */
  private String formatMonthYear(Scanner sc) {
    view.writeMessage("\nEnter month: ");
    String month = sc.next();
    try {
      month = String.format("%02d", Integer.parseInt(month));
    } catch (NumberFormatException e) {
      view.writeMessage("Error: " + e.getMessage() + System.lineSeparator());
    }

    view.writeMessage("Enter year: ");
    String year = sc.next();
    try {
      Integer.parseInt(year);
    } catch (NumberFormatException e) {
      view.writeMessage("Error: " + e.getMessage() + System.lineSeparator());
    }

    return year + "-" + month;
  }

  /**
   * Provides the user with several date format options.
   * @param sc scanner
   * @return the format for the date the user would like to choose
   */
  private String dateFormat(Scanner sc) {
    String choice = "";
    while (!(choice.equals("a") || choice.equals("b") || choice.equals("c"))) {
      view.writeMessage("Select date format\n");
      view.writeMessage("A. YYYY\n");
      view.writeMessage("B. YYYY-MM\n");
      view.writeMessage("C. YYYY-MM-DD\n");
      choice = sc.next().toLowerCase();
    }
    return choice;
  }

  /**
   * Adds a portfolio to the existing portfolios list.
   * @param name name of the portfolio
   * @param portfolios the already existing portfolios
   * @return the new portfolio that has been added
   */
  private Portfolio addPortfolio(String name, Map<String, Portfolio> portfolios) {
    Portfolio p = portfolios.get(name);

    if (p == null) {
      p = new PortfolioImpl();
      try {
        p.loadFromFile(name);
        portfolios.put(name, p);
      } catch (IllegalArgumentException | IOException e) {
        // do nothing
      }
    }

    return p;
  }

  /**
   * Delegates to the buy shares method.
   * @param sc scanner
   * @param portfolios the existing portfolios
   */
  private void handleBuyShares(Scanner sc, Map<String, Portfolio> portfolios) {
    try {
      view.writeMessage("Enter portfolio name: ");
      String name = sc.next();
      Portfolio p = addPortfolio(name, portfolios);

      view.writeMessage("Enter ticker symbol: ");
      String symbol = sc.next();
      Map<String, Stock> stocks = api.lookUpStock(symbol);

      view.writeMessage("Enter quantity: ");
      int quantity = sc.nextInt();

      String date = formatMonthDayYear(sc);

      p.buyStock(symbol, stocks, (double) quantity, date);
      portfolios.put(name, p);

      view.writeMessage("Shares bought successfully.\n");
    } catch (Exception e) {
      view.writeMessage("Error: " + e.getMessage() + "\n");
    }
  }

  /**
   * Delegates to the sell shares method.
   * @param sc scanner
   * @param portfolios the existing portfolios
   */
  private void handleSellShares(Scanner sc, Map<String, Portfolio> portfolios) {
    try {
      view.writeMessage("Enter portfolio name: ");
      String name = sc.next();
      Portfolio p = addPortfolio(name, portfolios);

      view.writeMessage("Enter ticker symbol: ");
      String symbol = sc.next();
      Map<String, Stock> stocks = api.lookUpStock(symbol);

      view.writeMessage("Enter quantity: ");
      int quantity = sc.nextInt();

      String date = formatMonthDayYear(sc);

      p.sellStock(symbol, stocks, quantity, date);

      view.writeMessage("Shares sold successfully.\n");
    } catch (Exception e) {
      view.writeMessage("Error: " + e.getMessage() + "\n");
    }
  }

  /**
   * Delegates to the get composition method.
   * @param sc scanner
   * @param portfolios the existing portfolios
   * @throws IOException if the portfolio does not exist
   */
  private void handleGetComposition(Scanner sc, Map<String, Portfolio> portfolios)
          throws IOException {
    view.writeMessage("Enter portfolio name: ");
    String name = sc.next();
    Portfolio p = addPortfolio(name, portfolios);

    String date = formatMonthDayYear(sc);

    Map<String, Double> composition = p.getComposition(date);

    if (composition.isEmpty()) {
      view.writeMessage("No stocks in the portfolio on this date.\n");
    } else {
      view.writeMessage("Composition on " + date + ":\n");
      for (Map.Entry<String, Double> entry : composition.entrySet()) {
        view.writeMessage(entry.getKey() + ": " + entry.getValue() + " shares\n");
      }
    }
  }

  /**
   * Delegates to the value distribution method.
   * @param sc scanner
   * @param portfolios the existing portfolios
   */
  private void handleGetValueDistribution(Scanner sc, Map<String, Portfolio> portfolios) {
    view.writeMessage("Enter portfolio name: ");
    String name = sc.next();
    Portfolio p = addPortfolio(name, portfolios);

    String date = formatMonthDayYear(sc);

    try {
      Map<String, Double> distribution = p.getValueDistribution(date);
      double totalValue = distribution.values().stream().mapToDouble(Double::doubleValue).sum();
      view.writeMessage("Value distribution on " + date + ":\n");
      for (Map.Entry<String, Double> entry : distribution.entrySet()) {
        view.writeMessage(entry.getKey() + ": $" + String.format("%.2f", entry.getValue()) + "\n");
      }
      view.writeMessage("Total value: $" + String.format("%.2f", totalValue) + "\n");
    } catch (Exception e) {
      view.writeMessage("Error: " + e.getMessage() + "\n");
    }
  }

  /**
   * Delegates to the save portfolio method.
   * @param sc scanner
   * @param portfolios the existing portfolios
   */
  private void handleSavePortfolio(Scanner sc, Map<String, Portfolio> portfolios) {
    view.writeMessage("Enter portfolio name: ");
    String name = sc.next();

    Portfolio p = portfolios.get(name);

    if (p == null) {
      p = new PortfolioImpl();
      view.writeMessage("Saving an empty portfolio.");
    }

    view.writeMessage("Enter filename to save: ");
    String filename = sc.next();

    try {
      p.saveToFile(filename);
      view.writeMessage("Portfolio saved successfully.\n");
    } catch (IOException e) {
      view.writeMessage("Error saving portfolio: " + e.getMessage() + "\n");
    }
  }

  /**
   * Delegates to the load portfolio method.
   * @param sc scanner
   * @param portfolios the existing portfolios
   */
  private void handleLoadPortfolio(Scanner sc, Map<String, Portfolio> portfolios) {
    view.writeMessage("Enter portfolio name: ");
    String name = sc.next();
    Portfolio p = portfolios.getOrDefault(name, new PortfolioImpl());

    view.writeMessage("Enter filename to load: ");
    String filename = sc.next();

    try {
      p.loadFromFile(filename);
      portfolios.put(name, p);
      view.writeMessage("Portfolio loaded successfully.\n");
    } catch (IOException e) {
      view.writeMessage("Error loading portfolio: " + e.getMessage() + "\n");
    } catch (IllegalArgumentException e) {
      view.writeMessage("Error: " + e.getMessage() + System.lineSeparator());
    }
  }

  /**
   * Delegates to the rebalance portfolio method.
   * @param sc scanner
   * @param portfolios the existing portfolios
   */
  private void handleRebalancePortfolio(Scanner sc, Map<String, Portfolio> portfolios) {
    view.writeMessage("Enter portfolio name: ");
    String name = sc.next();
    Portfolio p = portfolios.get(name);
    if (p == null) {
      p = new PortfolioImpl();
      try {
        p.loadFromFile(name);
        portfolios.put(name, p);
      } catch (IOException e) {
        // do nothing
      }
    }

    String date = formatMonthDayYear(sc);

    Map<String, Double> targetDistribution = new HashMap<>();
    List<String> sStocks = new ArrayList<String>();
    List<String> stocksNames = p.getStock();
    Collections.sort(stocksNames);
    for (String s : stocksNames) {
      if (!sStocks.contains(s)) {
        view.writeMessage("Enter target distribution for " + s + ": ");
        double targetDistStr = sc.nextDouble();
        targetDistribution.put(s, targetDistStr);
        sStocks.add(s);
      }
    }

    try {
      p.rebalance(name, targetDistribution, date);
      view.writeMessage("Portfolio rebalanced successfully.\n");
    } catch (IllegalArgumentException e) {
      view.writeMessage("Error: " + e.getMessage() + "\n");
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Checks that the user has entered the date in correctly.
   * @param date date entered by user
   * @return whether or not the date is in the correct format
   */
  private boolean checkDateFormatForPortfolioPerformance(String date) {
    String[] dateArr = date.split("-");
    if (dateArr.length == 3 && dateArr[0].length() == 4
            && dateArr[1].length() == 2
            && dateArr[2].length() == 2
            || (dateArr.length == 2
            && dateArr[0].length() == 4
            && dateArr[1].length() == 2)
            || (dateArr.length == 1 && dateArr[0].length() == 4)) {
      try {
        Integer.parseInt(dateArr[0]);
        if (dateArr.length == 2) {
          Integer.parseInt(dateArr[1]);
        }
        if (dateArr.length == 3) {
          Integer.parseInt(dateArr[2]);
        }
        return true;
      } catch (NumberFormatException e) {
        return false;
      }
    } else {
      return false;
    }
  }
}
