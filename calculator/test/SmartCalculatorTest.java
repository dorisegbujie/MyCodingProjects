import calculator.SmartCalculator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test for the SmartCalculator class.
 */
public class SmartCalculatorTest extends AbstractCalculatorTest {

  @Before
  public void setUp() {
    calc = new SmartCalculator();
  }

  @Test
  public void testMultipleEquals() {
    calc.input('3').input('2').input('+').input('2').input('4').input('=')
            .input('=').input('=');
    assertEquals("104", calc.getResult());
  }

  @Test
  public void testSkipSecondOperand() {
    calc.input('3').input('2').input('+').input('=').input('=');
    assertEquals("96", calc.getResult());
  }

  @Test
  public void testTwoConsecutiveOperators() {
    calc.input('3').input('2').input('+').input('-').input('2').input('4')
            .input('=');
    assertEquals("8", calc.getResult());
  }

  @Test
  public void testBeginWithOperator() {
    calc.input('+').input('3').input('2').input('-').input('2').input('4')
            .input('=');
    assertEquals("8", calc.getResult());
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

  @Test
  public void testMultipleOperations() {
    calc.input('3').input('+').input('2').input('=').input('+').input('4')
            .input('=');
    assertEquals("9", calc.getResult());
  }

  @Test
  public void testMultipleSubtractions() {
    calc.input('5').input('0').input('-').input('2').input('0').input('=')
            .input('-').input('1').input('0').input('=');
    assertEquals("20", calc.getResult());
  }

  @Test
  public void testMultiplicationAfterAddition() {
    calc.input('2').input('+').input('3').input('=').input('*').input('4')
            .input('=');
    assertEquals("20", calc.getResult());
  }

  @Test
  public void testClearAfterEquals() {
    calc.input('2').input('+').input('3').input('=').input('C');
    assertEquals("", calc.getResult());
  }

  @Test
  public void testLargeNumbersOverflow() {
    calc.input('2').input('1').input('4').input('7').input('4').input('8')
            .input('3').input('6').input('4').input('7').input('*')
            .input('2').input('=');
    assertEquals("0", calc.getResult()); // Result should be 0 due to overflow
  }

  @Test
  public void testArithmeticOverflow() {
    calc.input('2').input('1').input('4').input('7').input('4').input('8')
            .input('3').input('6').input('4').input('7').input('+')
            .input('1').input('=');
    assertEquals("0", calc.getResult()); // Result should be 0 due to overflow
  }

  @Test
  public void testInvalidOperatorSequence() {
    calc.input('3').input('+').input('+').input('2').input('=');
    assertEquals("5", calc.getResult());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeInput() {
    calc.input('-').input('3').input('+').input('2').input('=');
  }

  @Test
  public void testMultipleNegativeResults() {
    calc.input('5').input('-').input('1').input('0').input('=').input('-')
            .input('5').input('=');
    assertEquals("-10", calc.getResult());
  }
}
