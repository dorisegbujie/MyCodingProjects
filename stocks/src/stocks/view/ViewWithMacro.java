package stocks.view;

import java.text.ParseException;

/**
 * Represents a view that accepts a macro.
 */
public interface ViewWithMacro extends View {
  /**
   * Executes the method on the view.
   * @param v the view as a macro
   * @throws ParseException if there is an error
   */
  public void execute(ViewMacro v) throws ParseException;
}
