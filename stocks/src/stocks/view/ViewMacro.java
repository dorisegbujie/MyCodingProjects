package stocks.view;

import java.text.ParseException;

/**
 * Represents a view as a macro to support addition functionality in the view.
 */
public interface ViewMacro {
  /**
   * Executes the method on the view.
   * @param v the view
   * @throws ParseException if there is an error
   */
  public void executeMacro(View v) throws ParseException;
}
