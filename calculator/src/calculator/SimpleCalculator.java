package calculator;

/**
 * SimpleCalculator class implementing the Calculator interface.
 */
public class SimpleCalculator implements Calculator {
  protected StringBuilder currentInput;
  protected StringBuilder currentOperand;
  protected int firstOperand;
  protected int secondOperand;
  protected char operator;
  protected boolean isFirstOperandSet;
  protected boolean isSecondOperandSet;
  protected boolean isOperatorSet;
  protected boolean isResultSet;

  /**
   * Constructor for SimpleCalculator.
   * Initializes the calculator state.
   */
  public SimpleCalculator() {
    clear();
  }

  /**
   * Processes a single character input and updates the calculator state.
   *
   * @param ch the input character, which can be a digit, an operator (+, -, *), '=', or 'C' to
   *           clear
   * @return the updated Calculator object
   * @throws IllegalArgumentException if the input character is invalid or in an incorrect sequence
   */
  @Override
  public Calculator input(char ch) {
    if (ch == 'C') {
      clear();
    } else if (ch >= '0' && ch <= '9') {
      handleNumber(ch);
    } else if (ch == '+' || ch == '-' || ch == '*') {
      handleOperator(ch);
    } else if (ch == '=') {
      calculateResult();
    } else {
      throw new IllegalArgumentException("Invalid input");
    }
    return this;
  }

  /**
   * Clears the calculator state to the initial state.
   */
  protected void clear() {
    currentInput = new StringBuilder();
    currentOperand = new StringBuilder();
    firstOperand = 0;
    secondOperand = 0;
    operator = '\0';
    isFirstOperandSet = false;
    isSecondOperandSet = false;
    isOperatorSet = false;
    isResultSet = false;
  }

  /**
   * Handles numerical input characters and updates the operand values.
   *
   * @param ch the input character representing a digit
   * @throws IllegalArgumentException if the operand overflows
   */
  protected void handleNumber(char ch) {
    if (isResultSet && !isOperatorSet) {
      clear();
      isResultSet = false;
    }
    currentOperand.append(ch);
    if (currentOperand.length() > 10) {
      throw new IllegalArgumentException("Operand overflow");
    }
    currentInput.append(ch);
    if (!isOperatorSet) {
      firstOperand = firstOperand * 10 + Character.getNumericValue(ch);
      isFirstOperandSet = true;
    } else {
      secondOperand = secondOperand * 10 + Character.getNumericValue(ch);
      isSecondOperandSet = true;
    }
  }

  /**
   * Handles operator input characters and updates the operator value.
   *
   * @param ch the input character representing an operator (+, -, *)
   * @throws IllegalArgumentException if the operator is in an incorrect position
   */
  protected void handleOperator(char ch) {
    if (isFirstOperandSet && !isOperatorSet) {
      currentInput.append(ch);
      operator = ch;
      isOperatorSet = true;
      currentOperand.setLength(0);
    } else if (isResultSet) {
      currentInput.append(ch);
      operator = ch;
      isOperatorSet = true;
      isResultSet = false;
      currentOperand.setLength(0);
      isFirstOperandSet = true;
      isSecondOperandSet = false;
    } else if (isFirstOperandSet && isOperatorSet && isSecondOperandSet) {
      calculateResult();
      currentInput.append(ch);
      operator = ch;
      isOperatorSet = true;
      currentOperand.setLength(0);
      isResultSet = false;
    } else {
      throw new IllegalArgumentException("Invalid operator position");
    }
  }

  /**
   * Calculates the result based on the current operands and operator.
   * Updates the calculator state with the result.
   *
   * @throws IllegalArgumentException if the calculation sequence is invalid
   */
  protected void calculateResult() {
    if (isFirstOperandSet && isOperatorSet && isSecondOperandSet) {
      long result = 0;
      switch (operator) {
        case '+':
          result = (long) firstOperand + secondOperand;
          break;
        case '-':
          result = (long) firstOperand - secondOperand;
          break;
        case '*':
          result = (long) firstOperand * secondOperand;
          break;
        default:
          // No action needed
          throw new IllegalArgumentException("Invalid operator");
      }
      if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
        result = 0;
      }
      currentInput = new StringBuilder(Long.toString(result));
      firstOperand = (int) result;
      secondOperand = 0;
      operator = '\0';
      isOperatorSet = false;
      isSecondOperandSet = false;
      isResultSet = true;
      currentOperand.setLength(0);
    } else if (isResultSet) {
      // No operation needed, just show the current result
    } else {
      throw new IllegalArgumentException("Invalid calculation sequence");
    }
  }


  /**
   * Returns the current result or the state of the input.
   *
   * @return a String representing the current input or result
   */
  @Override
  public String getResult() {
    return currentInput.toString();
  }


}
