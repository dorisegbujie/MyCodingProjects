package calculator;

/**
 * SmartCalculator class implementing the Calculator interface.
 * This class represents a smart calculator that supports additional features
 * like handling multiple equals, skipping the second operand, and ignoring
 * consecutive operators or operators at the beginning.
 */
public class SmartCalculator extends SimpleCalculator {
  private int lastOperand;
  private char lastOperator;
  private boolean isLastOperationEquals;

  /**
   * Constructor for SmartCalculator.
   * Initializes the calculator state.
   */
  @Override
  public Calculator input(char ch) {
    if (ch == 'C') {
      super.input(ch);
      lastOperand = 0;
      lastOperator = '\0';
      isLastOperationEquals = false;
    } else if (ch >= '0' && ch <= '9' || ch == '+' || ch == '-' || ch == '*' || ch == '=') {
      super.input(ch);
      updateLastOperation(ch);
    } else {
      throw new IllegalArgumentException("Invalid input");
    }
    return this;
  }

  @Override
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
    } else if (isFirstOperandSet && isOperatorSet && !isSecondOperandSet) {
      // Ignore the first operator and update the operator to the latest one
      operator = ch;
      currentInput.setCharAt(currentInput.length() - 1, ch);
    } else if (ch == '+') {
      // Ignore '+' at the beginning or consecutive operators
    } else {
      throw new IllegalArgumentException("Invalid operator position");
    }
  }

  @Override
  protected void calculateResult() {
    if (isFirstOperandSet && isOperatorSet && !isSecondOperandSet) {
      secondOperand = firstOperand;
      isSecondOperandSet = true;
    }

    try {
      super.calculateResult();
    } catch (IllegalArgumentException e) {
      // If an invalid calculation sequence exception is thrown,
      // set second operand to the first operand
      if (!isSecondOperandSet) {
        secondOperand = firstOperand;
        isSecondOperandSet = true;
        super.calculateResult();
      } else {
        throw e;
      }
    }

    if (isResultSet) {
      lastOperand = secondOperand;
      lastOperator = operator;
      isLastOperationEquals = true;
    }
  }

  private void updateLastOperation(char ch) {
    if (ch == '=') {
      if (isResultSet) {
        if (lastOperator != '\0') {
          firstOperand = Integer.parseInt(getResult());
          operator = lastOperator;
          secondOperand = lastOperand;
          super.calculateResult();
        }
      } else {
        calculateResult();
        if (isResultSet) {
          lastOperand = secondOperand;
          lastOperator = operator;
          isLastOperationEquals = true;
        }
      }
    } else {
      isLastOperationEquals = false;
    }
  }
}
