import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import calculator.Calculator;

/**
 * Abstract Test for Calculators.
 */

public abstract class AbstractCalculatorTest {
  protected Calculator calc;

  @Before
  public abstract void setUp();

  @Test
  public void testBasicAddition() {
    calc.input('3').input('2').input('+').input('2').input('4').input('=');
    assertEquals("56", calc.getResult());
  }

  @Test
  public void testBasicSubtraction() {
    calc.input('5').input('0').input('-').input('2').input('0').input('=');
    assertEquals("30", calc.getResult());
  }

  @Test
  public void testBasicMultiplication() {
    calc.input('5').input('*').input('2').input('=');
    assertEquals("10", calc.getResult());
  }

  @Test
  public void testSimpleMultiplication() {
    calc.input('5').input('6').input('*').input('2').input('=');
    assertEquals("112", calc.getResult());
  }

  @Test
  public void testClear() {
    calc.input('2').input('+').input('3').input('=').input('C');
    assertEquals("", calc.getResult());
  }

  @Test
  public void testClearAfterOperation() {
    calc.input('5').input('6').input('+').input('3').input('4').input('C');
    assertEquals("", calc.getResult());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCharacter() {
    calc.input('a');
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOverflowOperand() {
    for (int i = 0; i < 20; i++) {
      calc.input('1');
    }
  }

  @Test
  public void testOverflowResult() {
    calc.input('2').input('0').input('2').input('3').input('3').input('7')
            .input('2').input('0').input('3').input('6').input('+')
            .input('1').input('2').input('2').input('3').input('3')
            .input('7').input('2').input('0').input('3').input('6')
            .input('=');
    assertEquals("0", calc.getResult());
  }

  @Test
  public void testAddOverflow() {
    calc.input('2').input('1').input('4').input('7').input('4')
            .input('8').input('3').input('6')
            .input('4').input('7')
            .input('+').input('1').input('=');
    assertEquals("0", calc.getResult());
  }

  @Test
  public void testSubtractOverflow() {
    calc.input('0').input('-').input('2').input('1').input('4').input('7')
            .input('4').input('8').input('3').input('6')
            .input('4').input('4')
            .input('-').input('9').input('=');
    assertEquals("0", calc.getResult());
  }

  @Test
  public void testNegativeResult() {
    calc.input('1').input('0').input('-').input('2').input('0').input('=');
    assertEquals("-10", calc.getResult());
  }

  @Test
  public void testSingleDigitOperations() {
    calc.input('3').input('+').input('2').input('=');
    assertEquals("5", calc.getResult());
  }

}
