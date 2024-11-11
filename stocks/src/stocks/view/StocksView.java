package stocks.view;

import java.io.IOException;

/**
 * Represents an implementation of the view that provides methods to show values to the user.
 */
public class StocksView implements View {
  private Appendable appendable;

  /**
   * Creates an instance of the view.
   * @param appendable the output string
   */
  public StocksView(Appendable appendable) {
    this.appendable = appendable;
  }

  @Override
  public void writeMessage(String message) throws IllegalStateException {
    try {
      appendable.append(message);

    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  @Override
  public void printMenu() throws IllegalStateException {
    writeMessage("1. Calculate gain/loss of a stock"
            + System.lineSeparator());
    writeMessage("2. Calculate x-day moving average of a stock"
            + System.lineSeparator());
    writeMessage("3. Find x-day crossovers for a stock"
            + System.lineSeparator());
    writeMessage("4. Create portfolio and calculate its value"
            + System.lineSeparator());
    writeMessage("5. Print supported instruction list" + System.lineSeparator());
    writeMessage("6. Quit the program " + System.lineSeparator());
  }

  @Override
  public void welcomeMessage() throws IllegalStateException {
    writeMessage("Welcome to the stocks program!" + System.lineSeparator());
    printMenu();
  }

  @Override
  public void farewellMessage() throws IllegalStateException {
    writeMessage("Thank you for using this program!");
  }

}
