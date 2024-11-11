package stocks.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.event.ListSelectionListener;

/**
 * Represents an interface for a GUI.
 * Supports creating a new portfolio, buying and selling stocks,
 * getting the value and composition of the portfolio, and saving and
 * retrieving portfolios to and from files
 */
public interface ViewGUI extends View, ItemListener, ListSelectionListener {

  /**
   * Gets the text displayed on the radio display.
   * @return a string representing the text on the radio display
   */
  public String getRadioDisplayText();

  /**
   * Gets the date from the users submission.
   * @return a string representing the date the user submitted
   */
  public String getDate();

  /**
   * Represents the filename submitted by the user.
   * @return a string representing the filename submitted by the user
   */
  public String saveFileGetText();

  /**
   * Represents the filename submitted by the user.
   * @return a string representing the filename submitted by the user
   */
  public String savePortGetText();

  /**
   * Represents the portfolio name entered by the user.
   * @return a string representing the portfolio name submitted by the user
   */
  public String portfolioNameComboGetText();

  /**
   * Represents the portfolio name entered by the user.
   * @return a string representing the portfolio name submitted by the user
   */
  public String pNameGetText();

  /**
   * Represents the quantity entered by the user.
   * @return a string representing the quantity submitted by the user
   */
  public String tGetText();

  /**
   * Represents the stock ticker entered by the user.
   * @return a string representing the stock ticker submitted by the user
   */
  public String t1GetText();

  /**
   * Checks to see if a portfolio has already been created and if
   * the user has selected to create a portfolio.
   * @param actionE the action event performed by the user
   */
  public void checkPortfolio(ActionEvent actionE);

  /**
   * Checks to see if a stock has already been bought or sold and if
   * the user has selected to buy or sell a stock.
   * @param actionE the action event performed by the user
   */
  public void checkBuySell(ActionEvent actionE);

  /**
   * Checks to see if getting the composition or value of a portfolio has already been selected
   * and if the user has selected to get the composition or value of a portfolio.
   * @param actionE the action event performed by the user
   */
  public void checkComposition(ActionEvent actionE);

  /**
   * Checks to see if a portfolio has already been saved or loaded and if
   * the user has selected to save or load a portfolio.
   * @param actionE the action event performed by the user
   */
  public void checkSaveLoadFile(ActionEvent actionE);

  /**
   * Sets the name of the selected portfolio.
   * @param name the portfolio name
   */
  public void portfoliosSetNames(String name);

  /**
   * Sets the listener for the GUI.
   * @param listener the listener needed to run the GUI
   */
  public void setListener(ActionListener listener);

  /**
   * Adds a new portfolio to the list of portfolios.
   * @param name the name of the new portfolio to be added
   */
  public void addItemToPortfolioNames(String name);
}
