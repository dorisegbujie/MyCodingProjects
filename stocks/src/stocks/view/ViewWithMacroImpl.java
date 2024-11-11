package stocks.view;

import java.text.ParseException;

/**
 * Represents a view that accepts a macro and implements the method on the view.
 */
public class ViewWithMacroImpl extends StocksView implements ViewWithMacro {

  /**
   * Creates an instance of the view.
   *
   * @param appendable the output string
   */
  public ViewWithMacroImpl(Appendable appendable) {
    super(appendable);
  }

  @Override
  public void execute(ViewMacro v) throws ParseException {
    v.executeMacro(this);
  }
}
