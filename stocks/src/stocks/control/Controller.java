package stocks.control;

import java.io.IOException;
import java.text.ParseException;

/**
 * The Controller interface represents the contract for managing user interactions
 * in the Stock Market Application. It handles user input, performs necessary
 * operations by interacting with the model, and updates the view accordingly.
 */
public interface Controller {
  /**
   * Runs the program, delegating to the various methods.
   */
  public void control() throws IOException, ParseException;
}