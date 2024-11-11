package stocks.view;

/**
 * Adds methods to the view.
 * Adds an updated list of functions supported by the program.
 */
public class MacroStocksView extends StocksView {

  /**
   * Creates an instance of the view.
   *
   * @param appendable the output string
   */
  public MacroStocksView(Appendable appendable) {
    super(appendable);
  }

  @Override
  public void printMenu() throws IllegalStateException {
    writeMessage("1. Calculate gain/loss of a stock"
            + System.lineSeparator());
    writeMessage("2. Calculate x-day moving average of a stock"
            + System.lineSeparator());
    writeMessage("3. Find x-day crossovers for a stock"
            + System.lineSeparator());
    writeMessage("4. Create an empty portfolio"
            + System.lineSeparator());
    writeMessage("5. Calculate the value of an existing portfolio"
            + System.lineSeparator());
    writeMessage("6. Get bar chart of portfolio or stocks value over period of time"
            + System.lineSeparator());
    writeMessage("7. Buy shares\n");
    writeMessage("8. Sell shares\n");
    writeMessage("9. Get portfolio composition\n");
    writeMessage("10. Get value distribution\n");
    writeMessage("11. Save portfolio\n");
    writeMessage("12. Load portfolio\n");
    writeMessage("13. Rebalance portfolio\n");
    writeMessage("14. Print supported instruction list" + System.lineSeparator());
    writeMessage("15. Quit the program " + System.lineSeparator());
  }
}
