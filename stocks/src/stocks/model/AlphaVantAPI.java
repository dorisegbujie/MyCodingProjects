package stocks.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

/**
 * Supports the alpha vantage api, allowing the selected stock to be looked up.
 */
public class AlphaVantAPI implements APICall {
  @Override
  public Map<String, Stock> lookUpStock(String name) {
    String apiKey = "W0M1JOKC82EZEQA8"; //"P01FORW78TM6BCZB";
    URL url = null;
    File file = new File(name + ".csv");
    if (file.exists()) {
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -1);
      SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

      String formatted = format1.format(cal.getTime());
      try {
        Readable fr = new FileReader(file);
        Readable br = new BufferedReader((Reader) fr);
        String line;
        while ((line = ((BufferedReader) br).readLine()) != null) {
          if (line.split(",")[0].contains(formatted)) {
            return csv(name, file);
          }
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    try {
      /*
      create the URL. This is the query to the web service. The query string
      includes the type of query (DAILY stock prices), stock symbol to be
      looked up, the API key and the format of the returned
      data (comma-separated values:csv). This service also supports JSON
      which you are welcome to use.
       */
      url = new URL("https://www.alphavantage"
              + ".co/query?function=TIME_SERIES_DAILY"
              + "&outputsize=full"
              + "&symbol"
              + "=" + name + "&apikey=" + apiKey + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
              + "no longer works");
    }

    InputStream in = null;
    StringBuilder output = new StringBuilder();

    try {
      /*
      Execute this query. This returns an InputStream object.
      In the csv format, it returns several lines, each line being separated
      by commas. Each line contains the date, price at opening time, highest
      price for that date, lowest price for that date, price at closing time
      and the volume of trade (no. of shares bought/sold) on that date.

      This is printed below.
       */
      in = url.openStream();
      int b;

      while ((b = in.read()) != -1) {
        output.append((char) b);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + name);
    }
    if (output.toString().contains("Error")) {
      throw new IllegalArgumentException("File doesn't exist");
    }
    try {
      FileWriter newFile = new FileWriter(name + ".csv");
      PrintWriter write = new PrintWriter(newFile);
      write.println(output.toString());
      write.close();
    } catch (IOException var8) {
      System.out.println("Cannot create file");
    }

    String[] line = output.toString().split("\r\n");
    Map<String, Stock> stock = new HashMap<>();
    int i = 1;
    while (line.length != i) {

      String[] commaSep = line[i].split(",");

      stock.put(commaSep[0], new StockImpl(name, Double.parseDouble(commaSep[4]),
              parseInt(commaSep[5])));

      i++;
    }
    return stock;
  }

  /**
   * Turns the csv file into a hashmap of dates as keys and stocks as values.
   *
   * @param file the selected csv file containing stocks info
   * @return a hashmap with dates as values and stocks as keys
   */
  private Map<String, Stock> csv(String name, File file) {
    Map<String, Stock> stock = new HashMap<>();
    try {
      Readable fr = new FileReader(file);
      Readable br = new BufferedReader((Reader) fr);
      String line;
      while ((line = ((BufferedReader) br).readLine()) != null) {
        String[] commaSep = line.split(",");
        if (commaSep[0].equals("timestamp")) {
          continue;
        }
        if (commaSep.length == 6) {
          stock.put(commaSep[0], new StockImpl(name, Double.parseDouble(commaSep[4]),
                  Integer.parseInt(commaSep[5])));
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return stock;
  }
}
