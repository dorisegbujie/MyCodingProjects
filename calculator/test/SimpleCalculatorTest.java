import calculator.SimpleCalculator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test for the SimpleCalculator class.
 */
public class SimpleCalculatorTest extends AbstractCalculatorTest {

  @Before
  public void setUp() {
    calc = new SimpleCalculator();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidSequence() {
    calc.input('+');
  }

  @Test
  public void testMultipleEquals() {
    calc.input('3').input('2').input('+').input('2').input('4').input('=')
            .input('=').input('=');
    assertEquals("56", calc.getResult());
  }

  @Test
  public void testOperatorOperandAfterEquals() {
    calc.input('3').input('2').input('+').input('1').input('4').input('=')
            .input('-').input('6').input('=');
    assertEquals("40", calc.getResult());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidOperatorPosition() {
    calc.input('3').input('+').input('+');
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

  @Test(expected = IllegalArgumentException.class)
  public void testMissingSecondOperand() {
    calc.input('3');
    calc.input('2');
    calc.input('+');
    calc.input('=');
  }

  @Test
  public void testGetResultAfterFirstOperand() {
    calc.input('3');
    calc.input('2');
    assertEquals("32", calc.getResult());
  }

  @Test
  public void testGetResultAfterOperator() {
    calc.input('3');
    calc.input('2');
    calc.input('+');
    assertEquals("32+", calc.getResult());
  }

  @Test
  public void testGetResultAfterSecondOperand() {
    calc.input('3');
    calc.input('2');
    calc.input('+');
    calc.input('2');
    calc.input('4');
    assertEquals("32+24", calc.getResult());
  }

  @Test
  public void testGetResultAfterOperatorAfterEquals() {
    calc.input('3');
    calc.input('2');
    calc.input('+');
    calc.input('2');
    calc.input('4');
    calc.input('=');
    assertEquals("56", calc.getResult());
    calc.input('-');
    assertEquals("56-", calc.getResult());
  }
}
