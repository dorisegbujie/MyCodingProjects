package calculator;

/**
 * A Calculator interface that represents a single calculator.
 * It processes inputs one character at a time and produces results accordingly.
 */
public interface Calculator {
  /**
   * Process the given input character and return the current Calculator instance.
   *
   * @param ch the input character
   * @return the current Calculator instance after processing the input
   * @throws IllegalArgumentException if the input is invalid
   */
  Calculator input(char ch) throws IllegalArgumentException;

  /**
   * Get the current result of the calculator as a string.
   *
   * @return the current result
   */
  String getResult();
}
