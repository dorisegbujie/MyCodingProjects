package stocks.view;

/**
 * Includes methods to show the results and interface to the user.
 */
public interface View {
  /**
   * Prints a message with info about the program.
   */
  public void welcomeMessage();

  /**
   * Prints a specified message.
   * @param message the message the user wants printed
   */
  public void writeMessage(String message);

  /**
   * Prints a message to alert the user the program is done.
   */
  public void farewellMessage();

  /**
   * Prints the selection options for the user to use the program.
   */
  public void printMenu();

}
