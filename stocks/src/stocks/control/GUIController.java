package stocks.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import stocks.model.APICall;
import stocks.model.Portfolio;
import stocks.model.PortfolioImpl;
import stocks.view.GUIView;
import stocks.view.ViewGUI;


/**
 * Represents a controller that delegates to the specified methods chosen by the user.
 * Creates the view and calls the specific view methods based on the users input.
 * Supports creating a new portfolio, buying and selling stocks,
 * getting the value and composition of the portfolio, and saving and
 * retrieving portfolios to and from files
 */
public class GUIController implements ActionListener, Controller {
  private ViewGUI view;
  private Map<String, Portfolio> portfolios;
  private APICall api;

  /**
   * Creates an instance of the GUI controller.
   * @param a the api selected to be used with the program
   */
  public GUIController(APICall a, ViewGUI v) {
    api = a;
    portfolios = new HashMap<String, Portfolio>();
    view = v;
    view.setListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent actionE) {
    view.checkPortfolio(actionE);
    view.checkBuySell(actionE);
    view.checkComposition(actionE);
    view.checkSaveLoadFile(actionE);

    switch (actionE.getActionCommand()) {
      case "Submit Information to Buy Stock":
        try {
          performBuySellStock("submit date buy");
        } catch (ParseException e) {
          view.writeMessage("Error: " + e.getMessage());
          throw new RuntimeException(e);
        }
        break;
      case "Submit Information to Sell Stock":
        try {
          performBuySellStock("submit date sell");
        } catch (ParseException e) {
          view.writeMessage("Error: " + e.getMessage());
          throw new RuntimeException(e);
        }
        break;
      case "Submit Portfolio":
        handleCreatePortfolio();
        break;
      case "Submit Date":
        handleGetComposition();
        break;
      case "Save Portfolio":
        handleSavePortfolio("Portfolio saved");
        break;
      case "Load Portfolio":
        handleSavePortfolio("Portfolio loaded");
        break;
      default:
        // do nothing
    }
  }

  /**
   * Calls the method to save or load a portfolio.
   * @param text if a portfolio is being saved or loaded
   */
  private void handleSavePortfolio(String text) {
    String filename = view.saveFileGetText();
    String portfolio = view.savePortGetText();
    Portfolio p1;
    Set<String> pNames1 = portfolios.keySet();
    boolean isName = pNames1.contains(portfolio);
    if (pNames1.contains(portfolio)) {
      p1 = portfolios.get(portfolio);
    } else {
      if (text.equals("Portfolio saved")) {
        view.writeMessage("Portfolio doesn't exist.");
        throw new IllegalArgumentException("Portfolio doesn't exist.");
      } else {
        p1 = new PortfolioImpl();
        portfolios.put(portfolio, p1);
      }
    }
    try {
      if (text.equals("Portfolio saved")) {
        p1.saveToFile(filename);
        view.writeMessage(text);
      } else {
        try {
          p1.loadFromFile(filename);
          if (!isName) {
            view.portfoliosSetNames(portfolio);
            view.addItemToPortfolioNames(portfolio);
            view.writeMessage(text);
          } else {
            view.writeMessage("Portfolio already exists");
          }
        } catch (FileNotFoundException | IllegalArgumentException e) {
          view.writeMessage("Error: " + e.getMessage());
          throw new IllegalArgumentException("Error: " + e.getMessage());
        }
      }
    } catch (IOException e) {
      view.writeMessage("File does not exist.");
    }
  }

  /**
   * Calls the method to get a portfolio composition or value.
   */
  private void handleGetComposition() {
    String date = view.getDate();
    String portfolioName;
    try {
      portfolioName = view.portfolioNameComboGetText();
    } catch (NullPointerException e) {
      view.writeMessage("Error: " + e.getMessage());
      throw new IllegalArgumentException("Error: " + e.getMessage());
    }
    Portfolio p;
    Set<String> pNames = portfolios.keySet();
    if (pNames.contains(portfolioName)) {
      p = portfolios.get(portfolioName);
    } else {
      view.writeMessage("Portfolio doesn't exist.");
      throw new IllegalArgumentException("Portfolio doesn't exist.");
    }
    if (view.getRadioDisplayText().equals("Get composition was selected")) {
      try {
        Map<String, Double> comp = p.getComposition(date);
        view.writeMessage(comp.toString());
      } catch (IllegalArgumentException e) {
        view.writeMessage("Error: " + e.getMessage());
      }
    } else {
      try {
        view.writeMessage("Value of " + portfolioName + " is $" + p.calcVal(date));
      } catch (IllegalArgumentException e) {
        view.writeMessage("Error: " + e.getMessage());
      }
    }
  }

  /**
   * Calls the method to create a new portfolio.
   */
  private void handleCreatePortfolio() {
    String currentP = view.pNameGetText();
    if (!portfolios.containsKey(currentP)) {
      portfolios.put(currentP, new PortfolioImpl());
      view.writeMessage("Portfolio created");
      view.portfoliosSetNames(currentP);
      view.addItemToPortfolioNames(currentP);
    } else {
      view.writeMessage("Portfolio already exists");
    }
  }

  /**
   * Calls the method to buy or sell stocks.
   * @param text if a stock is being bought or sold
   * @throws ParseException if the user has entered incorrect input
   */
  public void performBuySellStock(String text) throws ParseException {
    String quantity = view.tGetText();
    String stockTicker = view.t1GetText();
    String portfolioName;
    try {
      portfolioName = view.portfolioNameComboGetText();
    } catch (NullPointerException e) {
      view.writeMessage("Error: " + e.getMessage());
      throw new IllegalArgumentException("Error: " + e.getMessage());
    }    Portfolio p;
    Set<String> pNames = portfolios.keySet();
    if (pNames.contains(portfolioName)) {
      p = portfolios.get(portfolioName);
    } else {
      view.writeMessage("Portfolio doesn't exist.");
      throw new IllegalArgumentException("Portfolio doesn't exist.");
    }
    String selectedDate = view.getDate();
    if (text.equals("submit date buy")) {
      try {
        p.buyStock(stockTicker, api.lookUpStock(stockTicker), Double.parseDouble(quantity),
                selectedDate);
        view.writeMessage("Stock purchased successfully.");
      } catch (ParseException | IllegalArgumentException e) {
        view.writeMessage("Error: " + e.getMessage());
      }
    } else {
      try {
        p.sellStock(stockTicker, api.lookUpStock(stockTicker), Double.parseDouble(quantity),
                selectedDate);
        view.writeMessage("Stock sold successfully.");
      } catch (ParseException | IllegalArgumentException e) {
        view.writeMessage("Error: " + e.getMessage());
      }
    }
  }

  @Override
  public void control() throws IOException, ParseException {
    ((GUIView) view).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ((GUIView) view).setVisible(true);

    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (UnsupportedLookAndFeelException e) {
      // handle exception
    } catch (ClassNotFoundException e) {
      // handle exception
    } catch (InstantiationException e) {
      // handle exception
    } catch (IllegalAccessException e) {
      // handle exception
    } catch (Exception e) {
      // handle exception
    }
  }
}

