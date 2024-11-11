package stocks.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * Includes a method to create a view for a bar chart based on the performance of a portfolio.
 */
public class MacroBarChartView implements ViewMacro {
  Map<String, Integer> portfolioPerformance;

  /**
   * Creates an instance of the bar chart view.
   *
   * @param pPerform the performance of a portfolio
   */
  public MacroBarChartView(Map<String, Integer> pPerform) {
    portfolioPerformance = pPerform;
  }

  /**
   * Formats the date based on the given date pattern.
   *
   * @param keyArray array of dates
   * @param pattern  the pattern to be used to format the dates
   * @return a list of the formatted dates
   * @throws ParseException if the date is incorrect
   */
  private String[] yearMonthDateFormat(String[] keyArray, String pattern) throws ParseException {
    String[] formattedDates = new String[keyArray.length];
    int i = 0;
    for (String key : keyArray) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

      Date date = simpleDateFormat.parse(key);
      String d = date.toString().substring(4, 10) + date.toString().substring(23, 28);
      formattedDates[i] = d;
      i += 1;
    }

    return formattedDates;
  }

  @Override
  public void executeMacro(View v) throws ParseException {
    int startingAmount = portfolioPerformance.get("Scale");
    portfolioPerformance.remove("Scale");
    String[] keyArray = portfolioPerformance.keySet().toArray(
            new String[portfolioPerformance.size()]);
    Arrays.sort(keyArray);
    String[] formattedDates = new String[portfolioPerformance.size()];
    if (keyArray[0].length() == 10) {
      formattedDates = yearMonthDateFormat(keyArray, "yyyy-MM-dd");
    } else if (keyArray[0].length() == 7) {
      formattedDates = yearMonthDateFormat(keyArray, "yyyy-MM");
    } else if (keyArray[0].length() == 4) {
      formattedDates = yearMonthDateFormat(keyArray, "yyyy");
    }
    for (int i = 0; i < keyArray.length; i++) {
      double value = portfolioPerformance.get(keyArray[i]);
      String asterisks = "";
      for (int j = 0; j < value; j++) {
        asterisks += "*";
      }
      v.writeMessage(formattedDates[i] + ": " + asterisks + System.lineSeparator());
    }
    v.writeMessage(System.lineSeparator() + "Scale: * = " + startingAmount);
  }

}

